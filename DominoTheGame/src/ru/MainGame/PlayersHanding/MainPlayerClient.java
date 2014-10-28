/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.EndNotify;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.MainGame.CurrentPlayer;
import ru.MainGame.Events.StepEvent;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Gui.HUDInterface;
import ru.MainGame.HeapState;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.MessageSpecification;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StatusPlayer;
import ru.MainGame.Network.StepToSend;
import ru.MainGame.TableHanding.GoatRules;

import ru.MainGame.TableHanding.Rules;

/**
 * player to play networking
 * @author svt
 */
public class MainPlayerClient extends MainPlayer{

    private final Client mClient;
    SimpleApplication sApp;
    private Handler netHandler = new Handler();
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
                
                if(mInterface.getCurrentReadyButtonText().equals("Ready")){
                    message.setStatusPlayer(StatusPlayer.READY_TO_PLAY);
                    mInterface.changeStatus(CurrentPlayer.getInstance().getName(), "Ready");
                    
                    mInterface.removeCurButtonInMenu(new EndNotify() {

                        @Override
                        public void perform() {
                            mInterface.makeButtonInButtonLayer("Not ready");
                        }
                    });
                    
                }else{
                    message.setStatusPlayer(StatusPlayer.NOT_READY);
                    mInterface.changeStatus(CurrentPlayer.getInstance().getName(), "Not ready");
                    
                    mInterface.removeCurButtonInMenu(new EndNotify() {

                        @Override
                        public void perform() {
                            mInterface.makeButtonInButtonLayer("Ready");
                        }
                    });
                    
                }
                mClient.send(message);
            }
        };
        mInterface.initialize();
//        mInterface.cleanTopPanel();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainPlayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        mInterface.makeButtonInButtonLayer("Ready");
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
        Spatial hand = stepEvent.getDiceInHand();
        ru.MainGame.Dice handControll = stepEvent.getDiceInHand().getControl(ru.MainGame.Dice.class);
        ru.MainGame.Dice inTableControll = stepEvent.getDiceInTable().getControl(ru.MainGame.Dice.class);
        
        
        
        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage(
                MessageSpecification.STEP,
                CurrentPlayer.getInstance().getName(),
                StatusPlayer.IN_GAME,
                new StepToSend(new NumsOfDice(handControll.getLeftNum(), handControll.getRightNum()),
                    new NumsOfDice(inTableControll.getLeftNum(),inTableControll.getRightNum()),
                    stepEvent.getInTableNum(),stepEvent.getInHandNum()));
        
        Boolean isPrefToLeft = hand.getUserData(GoatRules.MAPPING_PREF_TO_LEFT);
        
        if(isPrefToLeft != null)
            message.setMessage(isPrefToLeft ? "left" : "right");
        
        mClient.send(message);
        
//        try{Thread.sleep(10);}catch(InterruptedException ignore){}
        
        
    }

    @Override
    protected void startGame(Spatial diceToStart,Spatial inGui) {
        ru.MainGame.Dice hand = diceToStart.getControl(ru.MainGame.Dice.class);
        
        if( ! hand.equals(getLowestDuble())){
            mInterface.makePopupText("You can start game only lowest duble");
            return;
        }
        
        myHandGuiNode.detachChild(inGui);
        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage(
                MessageSpecification.STEP,
                CurrentPlayer.getInstance().getName(),
                StatusPlayer.IN_GAME,
                new StepToSend(new NumsOfDice(hand.getLeftNum(), hand.getRightNum()),
                   null,null,null));
        message.setMessage("start");
        mClient.send(message);
    }
    
    private ru.MainGame.Dice getLowestDuble(){
        int lowest = 7;
        ru.MainGame.Dice lowestDice = null;
        for(Spatial s : getHand()){
            ru.MainGame.Dice handDice = s.getControl(ru.MainGame.Dice.class);
            
            int bothNum = handDice.getBothNum();
            if(bothNum != -1 && bothNum < lowest){
                lowest = bothNum;
                lowestDice = handDice;
            }
        }
        return lowestDice;
    }
    
    private class Handler implements  ErrorListener<Client>, ClientStateListener{


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
            message.setRestrictedObject(new Integer(CurrentPlayer.getInstance().getIndexOfAvatar()));
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
