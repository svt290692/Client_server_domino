/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;
import ru.MainGame.CurrentPlayer;
import ru.MainGame.Events.StepEvent;
import ru.MainGame.GameState;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Gui.HUDInterface;
import ru.MainGame.Gui.MenuState;
import ru.MainGame.HeapState;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.MessageSpecification;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StatusPlayer;
import ru.MainGame.Network.StepToSend;

import ru.MainGame.TableHanding.Rules;

/**
 *
 * @author svt
 */
public class MainPlayerClient extends MainPlayer{

    private final Client mClient;
    SimpleApplication sApp;
    private Handler netHandler = new Handler();

    private HUDInterface mInterface;
    boolean isGameStillRuning = true;

    private static final Logger LOG = Logger.getLogger(MainPlayerClient.class.getName());

    static{
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    public MainPlayerClient(HeapState heap, Rules rules, PlayersPlaces Place,
            Node tableNode, SimpleApplication sApp,String ipAddress,String port,String name,MessageListener<Client> messageListener)
            throws IOException{
        super(heap, rules, Place, tableNode, sApp,name);
        this.sApp = sApp;

        mClient = CurrentPlayer.getInstance().getClientOfCurSession();

        if(mClient == null){
            throw new IOException();
        }
        mClient.addClientStateListener(netHandler);
        mClient.addErrorListener(netHandler);
        mClient.addMessageListener(netHandler);

        if(messageListener != null)
            mClient.addMessageListener(messageListener);

        mClient.start();

        mInterface = new HUDInterface(sApp){
            @Override
            public void readyPushed() {
                super.readyPushed();
                ExtendedSpecificationMessage message= new ExtendedSpecificationMessage();
                message.setSpecification(MessageSpecification.NEW_STATUS);
                message.setWhoSend(CurrentPlayer.getInstance().getName());
                message.setStatusPlayer(StatusPlayer.READY_TO_PLAY);
                mClient.send(message);
            }
        };
        mInterface.initialize();
        mInterface.makeReadyButton();
    }

    public void addErrorListener(ErrorListener<? super Client> listener){
        mClient.addErrorListener(listener);
    }

    public void addMessageListener(MessageListener<? super Client> listener){
        mClient.addMessageListener(listener);
    }

    public void addClientStateListener(ClientStateListener listener){
        mClient.addClientStateListener(listener);
    }
    
    
    
    @Override
    protected void endOfStep(StepEvent stepEvent) {
        ru.MainGame.Dice hand = stepEvent.getDiceInHand().getControl(ru.MainGame.Dice.class);
        ru.MainGame.Dice inTable = stepEvent.getDiceInTable().getControl(ru.MainGame.Dice.class);
        
        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage(
                MessageSpecification.STEP,
                CurrentPlayer.getInstance().getName(),
                StatusPlayer.IN_GAME,
                new StepToSend(new NumsOfDice(hand.getLeftNum(), hand.getRightNum()),
                    new NumsOfDice(inTable.getLeftNum(),inTable.getRightNum()),
                    stepEvent.getInTableNum(),stepEvent.getInHandNum()));
        mClient.send(message);
    }

    @Override
    protected void startGame(Spatial diceToStart) {
        ru.MainGame.Dice hand = diceToStart.getControl(ru.MainGame.Dice.class);
        
        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage(
                MessageSpecification.STEP,
                CurrentPlayer.getInstance().getName(),
                StatusPlayer.IN_GAME,
                new StepToSend(new NumsOfDice(hand.getLeftNum(), hand.getRightNum()),
                   null,null,null));
        message.setMessage("start");
        mClient.send(message);
    }

    private class Handler implements MessageListener<Client>, ErrorListener<Client>, ClientStateListener{

        @Override
        public void messageReceived(Client source, Message m) {
            System.out.println("Message resive :" + m);
        }

        @Override
        public void handleError(Client source, Throwable t) {
            LOG.log(Level.WARNING, "Some Error come from client, error : {0}", new Object[]{t});
        }

        @Override
        public void clientConnected(Client c) {
            ExtendedSpecificationMessage message = new ExtendedSpecificationMessage();
            message.setWhoSend(CurrentPlayer.getInstance().getName());
            message.setSpecification(MessageSpecification.INITIALIZATION);
            message.setStatusPlayer(StatusPlayer.NOT_READY);
            mClient.send(message);
        }

        @Override
        public void clientDisconnected(Client c, DisconnectInfo info) {
            if(isGameStillRuning == true)
            JOptionPane.showMessageDialog(null, "Connection lost","Error connection",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void killPlayer() {
        super.killPlayer();
        isGameStillRuning = false;
        mClient.close();
        mInterface.clean();
    }

}
