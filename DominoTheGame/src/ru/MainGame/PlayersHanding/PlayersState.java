/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.MainGame.CurrentPlayer;
import ru.MainGame.DiceNumbers;
import ru.MainGame.Events.StepEvent;
import ru.MainGame.GameStarter;
import ru.MainGame.GameState;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Gui.GuiInterfaceHandler;
import ru.MainGame.Gui.MenuState;
import ru.MainGame.HeapState;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.MessageSpecification;
import ru.MainGame.Network.NumsOfDice;
import static ru.MainGame.Network.StatusPlayer.IN_GAME;
import static ru.MainGame.Network.StatusPlayer.NOT_READY;
import static ru.MainGame.Network.StatusPlayer.READY_TO_PLAY;
import ru.MainGame.Network.StepToSend;
import ru.MainGame.TableHanding.ClassicRules;
import ru.MainGame.TableHanding.Rules;
import ru.MainGame.TableHanding.TableState;

/**
 *
 * @author svt
 */
public class PlayersState extends AbstractAppState{

    SimpleApplication sApp;

    private Node guiNode;
    private InputManager inputManager;
    private FlyByCamera flyCam;

    private final HeapState heap;
    private final TableState table;
    private final Rules rules;
    private final GameState gameState;
    private MainPlayer mainPlayer;
    
    PickingListener mMouseListener;

    private static final Logger LOG = Logger.getLogger(PlayersState.class.getName());

    private static enum MappingsToInput{
	PICK("Left mouse pick"),
        CLEAR("clear");

	private MappingsToInput(String val) {
	    this.map = val;
	}
	String map;

	@Override
	public String toString() {
	    return map;
	}
    }

    private boolean isNetGameStarted = false;
    private boolean isMainPlayerStepWait = false;
    
    private static List<PlayersPlaces> busyPlaces = new ArrayList<>();
    private final Queue<Message> queueUnprocessedMessages = new ConcurrentLinkedQueue<>();
    private final List<AbstractPlayer> mAllPlayers = new LinkedList<>();
    private Queue<String> queuePlayersToAllowSteps = null;
    

    public PlayersState(HeapState heap, TableState table, Rules rules,GameState gameState) {
	this.heap = heap;
	this.table = table;
	this.rules = rules;
        this.gameState = gameState;
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }
    
    public static void registerPlace(PlayersPlaces place){
        busyPlaces.add(place);
    }

    public static void unregisterPlace(PlayersPlaces place){
        busyPlaces.remove(place);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	this.sApp = (SimpleApplication) app;
	this.guiNode = sApp.getGuiNode();
	this.inputManager = sApp.getInputManager();
	this.inputManager.setCursorVisible(true);
	this.flyCam = sApp.getFlyByCamera();
	this.flyCam.setDragToRotate(true);

        try {
            MainPlayerClient client = new MainPlayerClient(heap, rules,
                    PlayersPlaces.MAIN_PLAYER,table.getNode(), sApp,"127.0.0.1","5511",
                    CurrentPlayer.getInstance().getName(),new OnlineClientHandler());
            this.mainPlayer = client;
            this.mAllPlayers.add(client);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "The Client can't connect to server because network problem. address: <<{0}>> port<<{1}>> ",
                    new Object[]{"127.0.0.1","5511"});
        }

	initInput();
        mainPlayer.getInterface().addPlayerToTopPanel(
                CurrentPlayer.getInstance().getName(), "Not ready",
                CurrentPlayer.getInstance().getIndexOfAvatar(),true);
//        markToClearInterface.set(true);
    }
    
    private void initInput(){
        mMouseListener = new PickingListener();

	inputManager.addMapping(MappingsToInput.PICK.map,
		new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(MappingsToInput.CLEAR.map,
		new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

	inputManager.addListener(mMouseListener,MappingsToInput.PICK.map,MappingsToInput.CLEAR.map);
    }
    
    @Override
    public void update(float tpf) {
	if(mainPlayer.isCursorDiceExists() == true){
	    mainPlayer.setCursorDicePos(
		    inputManager.getCursorPosition());
	}
        for(AbstractPlayer player : mAllPlayers){
            player.UpdateFromApplication();
        }

        if(!queueUnprocessedMessages.isEmpty())
            resiveNewMessagesAppProcess();
        
        
    }

    private void resiveNewMessagesAppProcess(){
        Message message = queueUnprocessedMessages.remove();
        if(message instanceof ExtendedSpecificationMessage){
            ExtendedSpecificationMessage extendedMessage = (ExtendedSpecificationMessage)message;
            if(extendedMessage.getSpecification().equals(MessageSpecification.INITIALIZATION)){
                synchronized(this){
                    mAllPlayers.add(new DistancePlayer(findCorrectPlaceForDistancePlayer(),
                            sApp.getRootNode(), heap, extendedMessage.getWhoSend()));
                    
                    String status = null;
                    switch(extendedMessage.getStatusPlayer()){
                        case READY_TO_PLAY: status = "Ready";break;
                        case NOT_READY: status = "Not ready";break;
                        case IN_GAME: status = "";break;
                    }
                    int index = (Integer)extendedMessage.getRestrictedObject();
                    mainPlayer.getInterface().addPlayerToTopPanel(
                            extendedMessage.getWhoSend(),status,index,false);
                    notifyAll();
                }
            }
            else if(extendedMessage.getSpecification().equals(MessageSpecification.NEW_STATUS)){
                String status = null;
                switch(extendedMessage.getStatusPlayer()){
                    case READY_TO_PLAY: status = "Ready";break;
                    case NOT_READY: status = "Not ready";break;
                    case IN_GAME: status = "";break;
                }
                mainPlayer.getInterface().changeStatus(extendedMessage.getWhoSend(),status);
//                mainPlayer.getInterface().changeColor(status, new Color(255f, 0.5f, 0.7f, 0.5f));
            }
            else if(extendedMessage.getSpecification().equals(MessageSpecification.DISCONNECT)){
                mainPlayer.getInterface().removePlayer(extendedMessage.getWhoSend());
                queuePlayersToAllowSteps.remove(extendedMessage.getWhoSend());
                for(AbstractPlayer p : mAllPlayers){
                    if(p.getName().equals(extendedMessage.getWhoSend())){
                        mAllPlayers.remove(p);
                        busyPlaces.remove(p.getPlace());
                        break;
                    }
                }
            }
            else if(extendedMessage.getSpecification().equals(MessageSpecification.STEP)){
                makeResivedStepFromDistancePlayer(extendedMessage);
            }
        }
        else{
            StartGameMessage startMessage = (StartGameMessage)message;
//            mainPlayer.getInterface().cleanTopPanel();
            
            for(String name :startMessage.getStartGamePart().keySet()){
//                if( ! (CurrentPlayer.getInstance().getName().equals(name)) )
                mainPlayer.getInterface().changeStatus(name, " ");
            }
            
            queuePlayersToAllowSteps = new ConcurrentLinkedQueue<>(startMessage.getQueueToSteps());
            if(queuePlayersToAllowSteps.element().equals(CurrentPlayer.getInstance().getName())){
                allowMainStep();
            }
                mainPlayer.getInterface().changeStatus(queuePlayersToAllowSteps.element(),
                    "Move dominoes");
            mainPlayer.getInterface().removeCurButtonInMenu(null);
        }
    }
    
    private void turnNextPlayerStep(){
        if(queuePlayersToAllowSteps.element().equals(CurrentPlayer.getInstance().getName())){
            denieMainStep();
        }
        
        mainPlayer.getInterface().changeStatus(queuePlayersToAllowSteps.element(),
                    "");
        queuePlayersToAllowSteps.add(queuePlayersToAllowSteps.remove());
        mainPlayer.getInterface().changeStatus(queuePlayersToAllowSteps.element(),
                    "Move dominoes");
        if(queuePlayersToAllowSteps.element().equals(CurrentPlayer.getInstance().getName())){
                allowMainStep();
        }
    }
    
    private void allowMainStep(){
//        sApp.getInputManager().setCursorVisible(true);
        isMainPlayerStepWait = true;
    }
    
    private void denieMainStep(){
//        sApp.getInputManager().setCursorVisible(false);
        isMainPlayerStepWait = false;
    }
    
    @Override
    public void cleanup() {
//        mainPlayer.getInterface().cleanTopPanel();
        if(mainPlayer != null)
        mainPlayer.killPlayer();
        
        inputManager.deleteMapping(MappingsToInput.PICK.map);
        inputManager.deleteMapping(MappingsToInput.CLEAR.map);
        inputManager.removeListener(mMouseListener);
        
    }

    private class PickingListener implements ActionListener{

	public void onAction(String name, boolean isPressed, float tpf) {
	    if(name.equals(MappingsToInput.PICK.map) && isMainPlayerStepWait == true){
		mainPlayer.mouseClick(isPressed);
	    }
            else if(name.equals(MappingsToInput.CLEAR.map)){
                if(true == isPressed)
                mainPlayer.clearCursor();
            }
	}
    }
    
    private PlayersPlaces findCorrectPlaceForDistancePlayer(){
        for(PlayersPlaces p : PlayersPlaces.values()){
            if(busyPlaces.contains(p)) continue;
            else return p;
        }
        return null;
    }
    
    void makeResivedStepFromDistancePlayer(ExtendedSpecificationMessage message){
        String name = message.getWhoSend();
        StepToSend step = (StepToSend)message.getRestrictedObject();
            for(AbstractPlayer p :mAllPlayers ){
                
                if(p.getName().equals(name)){
                    if(message.getMessage() != null && message.getMessage().split(" ")[0].equals("start")){
                        rules.startGame(HeapState.findDiceInNode(p.getNode(),
                                step.getInHand().getLeft(),step.getInHand().getRight()));
                    }
                    else{
                        Spatial onHand = HeapState.findDiceInNode(p.getNode(),
                                step.getInHand().getLeft(), step.getInHand().getRight());

                        Spatial onTable = HeapState.findDiceInNode(table.getNode(),
                                step.getInTable().getLeft(), step.getInTable().getRight());

                        if(message.getMessage() != null){ 
                            String m = message.getMessage().split(" ")[0];
                            
                            switch (m) {
                                case "left":
                                    onHand.setUserData(ClassicRules.MAPPING_PREF_TO_LEFT, true);
                                    break;
                                case "right":
                                    onHand.setUserData(ClassicRules.MAPPING_PREF_TO_LEFT, false);
                                    break;
                            }
                        }
                        
                        StepEvent event = new StepEvent(onTable, onHand, step.getInTableNum(),step.getInHandNum());
                        
                        rules.doStep(event);
                    }
                    p.sortDices();
                    //
                    turnNextPlayerStep();
                    
                    break;
                }
            }
        }

    private class OnlineClientHandler implements MessageListener<Client>{

        @Override
        public void messageReceived(Client source, Message m) {
            System.out.println("I am resive message in Players state : "  + m);
            if(m instanceof ExtendedSpecificationMessage){
                
                ExtendedSpecificationMessage message = (ExtendedSpecificationMessage)m;
                if(message.getSpecification().equals(MessageSpecification.INITIALIZATION)||
                        message.getSpecification().equals(MessageSpecification.NEW_STATUS)||
                        message.getSpecification().equals(MessageSpecification.DISCONNECT)){
                    
                    if(!(message.getWhoSend().equals(CurrentPlayer.getInstance().getName())))
                    queueUnprocessedMessages.add(message);
                    
                }
                else if(message.getSpecification().equals(MessageSpecification.STEP)){
                    queueUnprocessedMessages.add(message);
                }else if(message.getSpecification().equals(MessageSpecification.KICK)){
                    JOptionPane.showMessageDialog(null, message.getMessage());
                    sApp.stop();
//                    timeToOut.set(true);
//                    GuiInterfaceHandler.getInstance().getNifty().exit();
//                    GuiInterfaceHandler.getInstance().getNifty().fromXml(
//                            "Interface/menu.xml", "start");

//                    sApp.getStateManager().detach(gameState);
//                    sApp.getStateManager().attach(new MenuState());
                }
            }
            else if(m instanceof StartGameMessage){
                
                synchronized(this){
                    StartGameMessage message = ((StartGameMessage)m);
                        queueUnprocessedMessages.add(message);
                    while(!queueUnprocessedMessages.isEmpty()){
                        try {
                            this.wait(10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PlayersState.class.getName()).log(Level.SEVERE, "interrupt when wait", ex);
                        }
                    }
                    for(AbstractPlayer p : mAllPlayers){
                        List<NumsOfDice> mPart = message.getPartOf(p.getName());

                        for(NumsOfDice n : mPart){
                         p.TakeFromHeap(n.getLeft(), n.getRight());
                        System.out.println(""+ p.getName() + " have got:" + n);
                        }
                    }
                    System.out.println("LETS START!!!!");
                    isNetGameStarted = true;
                }
            }
        }
        
    }
    
    public class DistancePlayer extends AbstractPlayer{

        public DistancePlayer(PlayersPlaces Place, Node rootNode, HeapState heap,String name) {
            super(Place, rootNode, heap,name);
            this.name = name;
        }


        @Override
        public void UpdateFromApplication() {
            if(!queueAddToScreenDices.isEmpty()){
                Spatial s = queueAddToScreenDices.remove();
                getNode().attachChild(s);
                sortNodeDices(getNode(), HeapState.getDicesWidth());
            }
        }

        @Override
        public void sortDices() {
            sortNodeDices(myNode, HeapState.getDicesWidth());

        }
    }
}