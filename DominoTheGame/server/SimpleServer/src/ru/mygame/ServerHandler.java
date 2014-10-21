/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mygame;

import ru.MainGame.Network.Server.AbleToPlay;
import ru.MainGame.Network.Server.QueuePlayersInServer;
import ru.MainGame.Network.Server.QueuePlayers;
import ru.MainGame.Network.Server.HostedPlayer;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filter;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.MessageSpecification;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StatusPlayer;

/**
 *
 * @author svt
 */
public class ServerHandler implements ConnectionListener, MessageListener<HostedConnection>{

    private final Server mServer;
    private final QueuePlayers queuePlayers;
    private List<HostedPlayer> mConnectedPlayers;
    private static final Logger LOG = Logger.getLogger(ServerHandler.class.getName());

    public ServerHandler(Server mServer) {
	this.mServer = mServer;
        this.queuePlayers = new QueuePlayersInServer();
        this.mConnectedPlayers = new LinkedList<>();

	GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
        resiveNewConnection(conn);
	LOG.log(Level.INFO, "Connection " + server.getGameName() + " was added to server: " + conn.getAddress() + " .");
//        System.out.println("Connection  "+ conn.getAddress()+"was added to server: "+ server.getGameName());
    }
    
    private void resiveNewConnection(HostedConnection conn){
        HostedPlayer player = new HostedPlayer(conn);

        queuePlayers.addPlayer(player);
        mConnectedPlayers.add(player);
    }
    
    private void connectionLost(HostedConnection conn){
        HostedPlayer player = findPlayerFromConnection(conn);
        mConnectedPlayers.remove(player);
        queuePlayers.removePlayer(player);
        ExtendedSpecificationMessage msg = new ExtendedSpecificationMessage(
                MessageSpecification.DISCONNECT, player.getName(),
                StatusPlayer.NOT_READY,null);
        mServer.broadcast(msg);
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
	connectionLost(conn);
	LOG.log(Level.WARNING, "Connection << "+server.getGameName()+" >> lost from server: << "+conn.getAddress()+" >>");
//        System.out.println("Connection << "+conn.getAddress()+">> lost from server: << "+server.getGameName()+" >>");
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        HostedPlayer player = findPlayerFromConnection(source);
//        //TEST
        System.out.println("NEW message yahoo! class:" + m.getClass().getName() +" string of message : "+ m);
//        //END
        if(m instanceof ExtendedSpecificationMessage){
            ExtendedSpecificationMessage msg = (ExtendedSpecificationMessage)m;

            switch(msg.getSpecification()){
                case INITIALIZATION:
		    onInitResv(player, msg);
		    sendInfoTo(source);break;
                case NEW_STATUS:
                    onNewStatusResv(player, msg);break;
                case STEP:
		    onStepResv(player, msg);break;
		default:
		    LOG.log(Level.WARNING,"Warning messaage is not readeble "
                            + "becaus it is hav not specification");
            }
        }
        else {
            LOG.log(Level.WARNING, "server resive undefined message :{0}", m.getClass().getName());
        }
    }
//    private void onRequestReceive(HostedPlayer from,ExtendedSpecificationMessage msg){
//        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage();
//        
//        message.setSpecification(MessageSpecification.ALLOW);
//        message.setWhoSend("Server");
//        
//        check:{
//            if(queuePlayers.asList().size() > 2){
//                message.setSpecification(MessageSpecification.DENIE);
//                message.setMessage("The game already Full");
//                break check;
//            }
//
//            for(HostedPlayer p : mConnectedPlayers){
//                if(p.getName().equals(msg.getWhoSend())){
//                    message.setSpecification(MessageSpecification.DENIE);
//                    message.setMessage("Player with this name already in game!"
//                            + " Please enter another name");
//                    break check;
//                }
//            }
//        }
//        from.getmConnection().send(message);
//    }
    
    private void sendInfoTo(HostedConnection conn){
//	List<String> names = new ArrayList<>();
	for(AbleToPlay p : queuePlayers.asList()){
//	    names.add(p.getName());
	    ExtendedSpecificationMessage message = new ExtendedSpecificationMessage();
	    message.setSpecification(MessageSpecification.INITIALIZATION);

	    if(p instanceof HostedPlayer){
		message.setStatusPlayer(((HostedPlayer)p).getStatus());
                message.setRestrictedObject(new Integer(p.getIndexOfAvatar()));
            }
	    else
		message.setStatusPlayer(StatusPlayer.READY_TO_PLAY);

	    message.setWhoSend(p.getName());
	    conn.send(message);
	}
    }

    private void onStepResv(HostedPlayer player,ExtendedSpecificationMessage msg){
	mServer.broadcast(msg);
    }
    
    private void onInitResv(HostedPlayer player,ExtendedSpecificationMessage msg){
        
        if(queuePlayers.asList().size() > 3){
            msg.setSpecification(MessageSpecification.KICK);
            msg.setMessage("The game already Full");
            player.getmConnection().send(msg);
            return;
        }
        
        for(HostedPlayer p : mConnectedPlayers){
            if(p.getName() != null && p.getName().equals(msg.getWhoSend())){
                msg.setSpecification(MessageSpecification.KICK);
                msg.setMessage("Player with this name already in game!"
                        + " Please enter another name");
                player.getmConnection().send(msg);
                return;
            }
        }
        
	player.setName(msg.getWhoSend());
        player.setIndexOfAvatar((Integer)msg.getRestrictedObject());
	if(msg.getStatusPlayer() != null)
	player.setStatus(msg.getStatusPlayer());
	mServer.broadcast(msg);
    }
    
    private void onNewStatusResv(HostedPlayer player,ExtendedSpecificationMessage msg){
	player.setStatus(msg.getStatusPlayer());
	printCountReadyClients();

	mServer.broadcast(msg);
	if(isAllPlayersReady()){
         mServer.broadcast(createStartGameMessage());
         for(HostedPlayer p : mConnectedPlayers){
             p.setStatus(StatusPlayer.IN_GAME);
         }
	   LOG.log(Level.INFO, "The game was running");
	}
    }
    
    private void printCountReadyClients(){
	int countReady = 0;
	int countAll = 0;
	for(AbleToPlay a: queuePlayers.asList()){
	    if(a instanceof HostedPlayer){
		countAll++;
		HostedPlayer player = (HostedPlayer)a;
		if(player.getStatus() == StatusPlayer.READY_TO_PLAY)
		   countReady++;
	    }
	}
	System.out.println("The hosted Players: "+ countAll+"\nThePlayers are ready: " + countReady);
    }

    private StartGameMessage createStartGameMessage(){
        List<NumsOfDice> dices = new ArrayList<>();
	for(int i=0,j=0,k=0;i <= 27 ;i++){
            dices.add(new NumsOfDice(j, k));
	    System.out.print("<" + j + "> <" +k+ ">_");
            if(j == 6){
                k++;
		j = k;
            }else j++;
        }

        Collections.sort(dices, new Comparator<Object>() {

                Random rand = new Random();
            @Override
            public int compare(Object o1, Object o2) {
                return (1 - rand.nextInt(3));
            }
        });
        StartGameMessage message = new StartGameMessage();

        int count = 0;
        for(HostedPlayer p : mConnectedPlayers){
            List<NumsOfDice> list = new ArrayList<>();
            for(int j = 0 ; j < 6; j++){
		list.add(dices.get(count++));
	    }
            message.addNewEntry(p.getName(), list);
        }
        
        String firstPlayer = null;
        
        for(Map.Entry<String, List<NumsOfDice> > e : message.getStartGamePart().entrySet()){
            int biggest = -1;
            for(NumsOfDice d: e.getValue()){
                if(d.getLeft() == d.getRight()){
                    if(d.getLeft() > biggest){
                        biggest =d.getLeft();
                        firstPlayer = e.getKey();
                    }
                }
            }
            if(null == firstPlayer){
                biggest = 0;
                for(NumsOfDice d: e.getValue()){
                    int summ = (d.getLeft() + d.getRight());
                    
                    if(summ  > biggest){
                        biggest = summ;
                        firstPlayer = e.getKey();
                    }
                }
            }
            
        }
        List<String> queue = new LinkedList<>();
        queue.add(firstPlayer);
        
        for(AbleToPlay p : queuePlayers.asList()){
            if(!(p.getName().equals(firstPlayer))){
                queue.add(p.getName());
            }
        }
        message.setQueueToSteps(queue);
        
        return message;
    }

    private boolean isAllPlayersReady(){
        if(mConnectedPlayers.isEmpty() || mConnectedPlayers.size() < 2) return false;
	try{
        for(HostedPlayer p : mConnectedPlayers){
            if(!(p.getStatus().equals(StatusPlayer.READY_TO_PLAY)))return false;
        }
	}catch(NullPointerException ex){
	    return false;
	}
        return true;
    }

    private void broadcastAllExceptOne(final HostedPlayer who,Message m){
        mServer.broadcast(new Filter<HostedConnection>() {

            @Override
            public boolean apply(HostedConnection input) {
                if(input.equals(who))
                    return false;
                else return true;
            }
        }, m);
    }

    private HostedPlayer findPlayerFromConnection(HostedConnection connection){
	for(HostedPlayer p : mConnectedPlayers){
            HostedPlayer player = (HostedPlayer)p;
            if(player.getmConnection().equals(connection))
                return player;
	}
	throw new NoSuchElementException("No such Player of connection " + connection);
    }
}