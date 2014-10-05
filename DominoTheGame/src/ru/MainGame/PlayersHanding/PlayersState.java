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
import com.jme3.scene.Node;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.HeapState;
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

    private MainPlayer mainPlayer;

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

    public PlayersState(HeapState heap, TableState table, Rules rules) {
	this.heap = heap;
	this.table = table;
	this.rules = rules;
        GlobalLogConfig.initLoggerFromGlobal(LOG);
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
            this.mainPlayer = new MainPlayerClient(heap, rules, PlayersPlaces.MAIN_PLAYER,table.getMyNode(), sApp,"127.0.0.1","5511");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "The Client can't connect to server because network problem. address: <<{0}>> port<<{1}>> ", new Object[]{"127.0.0.1","5511"});
        }

	initInput();
    }

    private void initInput(){
        PickingListener listener = new PickingListener();

	inputManager.addMapping(MappingsToInput.PICK.map,
		new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(MappingsToInput.CLEAR.map,
		new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

	inputManager.addListener(listener,MappingsToInput.PICK.map,MappingsToInput.CLEAR.map);
    }

    @Override
    public void update(float tpf) {
	if(mainPlayer.isCursorDiceExists() == true){
	    mainPlayer.setCursorDicePos(
		    inputManager.getCursorPosition());
	}
    }

    @Override
    public void cleanup() {
        mainPlayer.killPlayer();
    }

    private class PickingListener implements ActionListener{

	public void onAction(String name, boolean isPressed, float tpf) {
	    if(name.equals(MappingsToInput.PICK.map)){
		mainPlayer.mouseClick(isPressed);
	    }
            else if(name.equals(MappingsToInput.CLEAR.map)){
                if(true == isPressed)
                mainPlayer.clearCursor();
            }

	}

    }

}
