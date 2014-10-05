/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mygame;

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
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
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
                case INITIALIZATION: player.setName(msg.getWhoSend()); break;
                case NEW_STATUS:
                    player.setStatus(msg.getStatusPlayer());
		    printCountReadyClients();
                    if(isAllPlayersReady()){
//                       mServer.broadcast(createStartGameMessage());
		       LOG.log(Level.INFO, "The game was running");
		    }
                    break;
                case STEP: mServer.broadcast(msg); break;
                    default:LOG.log(Level.WARNING,"Warning messaage is not readeble "
                            + "becaus it is hav not specification");
            }
        }
        else {
            LOG.log(Level.WARNING, "server resive undefined message :{0}", m.getClass().getName());
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
        for(int i=0,j=0;i < 7 ;j++){
            dices.add(new NumsOfDice(i, j));
	    System.out.print("<" + i + "> <" +j+ ">|");
            if(j == 6){
                i++;
                j = i;
            }
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
            list.add(dices.get(count++));
            message.addNewEntry(p.getMyName(), list);
        }
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
