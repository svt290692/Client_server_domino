/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import ru.MainGame.PlayersHanding.PlayersState;
import ru.MainGame.TableHanding.TableState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.TableHanding.GoatRules;
import ru.MainGame.TableHanding.Rules;

/**
 * state main game 
 * this state contain table loading and initialize game without gui
 * @author svt
 */
public class GameState extends AbstractAppState{

    private Node rootNode;
    private SimpleApplication app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private Camera cam;
    private Node guiNode;
    private Spatial table;

    private HeapState mHeap;
    private TableState mTable;
    private PlayersState mPlayers;

    private Node heapNode;
    private Node tableNode;
    
    DirectionalLight light;
    DirectionalLight guiLight;

    private Rules curRules;

    private static final Logger LOG = Logger.getLogger(GameState.class.getName());


    public GameState() {

	GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        this.app = (SimpleApplication) app;
        this.stateManager = stateManager;
        this.inputManager = this.app.getInputManager();
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
	this.guiNode = this.app.getGuiNode();
	try{
        this.mHeap = new HeapState(new SimpleDicesLoader(assetManager));
	}catch(IOException ex){
	    LOG.log(Level.SEVERE,"io error in {0} when models try load Exception: {1}",new Object[]{ModelsLoader.class.getName(),ex});
	    app.stop();
	}
	this.mTable = new TableState(mHeap);
	this.curRules = new GoatRules(mTable,assetManager);
	this.mPlayers = new PlayersState(mHeap, mTable,curRules,this);
        this.tableNode = mTable.getNode();
	this.heapNode = mHeap.getNode();
        cam.setLocation(new Vector3f(0.009792091f, 1.6326832f, 1.6419929f));
	cam.setRotation(new Quaternion(0.0010556657f, 0.9016319f, -0.4324973f, 0.0022009274f));

	LOG.log(Level.FINEST,"camera Init in pos and rotation : pos:{0}rot:{1}",
		new Object[]{ cam.getLocation(), cam.getDirection() });

        InitLight();
        InitScene();

        stateManager.attach(mHeap);
        stateManager.attach(mTable);
        stateManager.attach(mPlayers);

    }
    
    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        stateManager.detach(mHeap);
        stateManager.detach(mTable);
        stateManager.detach(mPlayers);
        rootNode.detachAllChildren();
        rootNode.removeLight(light);
        guiNode.removeLight(guiLight);
    }

    private void InitLight(){
    light = new DirectionalLight();
    light.setDirection((cam.getDirection()));
    light.setColor(ColorRGBA.White);
    rootNode.addLight(light);

    guiLight = new DirectionalLight();
    guiLight.setDirection(new Vector3f(0, 0, -1.0f));
    guiNode.addLight(guiLight);
    LOG.finest("Light was initialized");
    }
    
    private void InitScene(){
        table = assetManager.loadModel("Models/table/table.j3o");
        table.setLocalTranslation(0,-2.37f, 0);
        rootNode.attachChild(table);

        app.getViewPort().setBackgroundColor(new ColorRGBA(0.5f, 0.8f, 1f, 1f));

	LOG.finest("Scene was initialized");
    }
}
