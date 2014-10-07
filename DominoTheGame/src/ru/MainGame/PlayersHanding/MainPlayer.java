/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.MainGame.DominoApp;
import ru.MainGame.Events.StepEvent;
import ru.MainGame.HeapState;
import ru.MainGame.TableHanding.Rules;
import ru.MainGame.TableHanding.TableState;

/**
 *
 * @author svt
 */
public abstract class MainPlayer extends AbstractPlayer{

    private final Node guiNode;
    private final Node myHandGuiNode;
    private final Node cursorSpecialNode;
    private final InputManager inputManager;
    private final Node tableNode;
    private Camera cam;
    private SimpleApplication sApp;

    private boolean isDicesBig = false;

    private final String DICE_IN_TABLE = "I am In Table";
    private Spatial cursorDice = null;
    private Spatial selectedGuiDice = null;


    private final Rules rules;

    private final float dicesWidth;



    public MainPlayer(HeapState heap, Rules rules, PlayersPlaces Place,Node tableNode, SimpleApplication sApp,String name) {
	super(Place, sApp.getRootNode(),heap,name);
        this.sApp = sApp;
	this.guiNode = sApp.getGuiNode();
	this.rules = rules;
	this.inputManager = sApp.getInputManager();
	this.tableNode = tableNode;
	this.cam = sApp.getCamera();
	this.myHandGuiNode = new Node("gui main player");
	this.cursorSpecialNode = new Node("cursor node");
	this.dicesWidth = HeapState.getDicesWidth();



	this.myHandGuiNode.setLocalTranslation(DominoApp.screenWidth / 2, DominoApp.screenHeight / 2, 0);
	this.guiNode.attachChild(myHandGuiNode);
	this.guiNode.attachChild(cursorSpecialNode);
	initDices();
    }

    void mouseClick(boolean isPressed){
	if(isPressed){

            inputManager.setCursorVisible(false);

	    Spatial collideGui = requestToGuiCollide();
	    if(collideGui != null){
		Spatial inTable = collideGui.getUserData(DICE_IN_TABLE);

		if(inTable != null){
		    if(rules.isGameStarted()){
			DiceTrigger(collideGui);
		    }
		    else{
			rules.startGame(inTable);
			myHandGuiNode.detachChild(collideGui);
			sortDices();
		    }
		}
	    }
	    else{
		tryMakeStep();
	    }
	}
	else{
            // camera will be rotateble when player release mouse once
            sApp.getFlyByCamera().setEnabled(true);
            inputManager.setCursorVisible(true);

	    tryMakeStep();
	}
    }
    /**
     * just remove cursor dice
     */
    void clearCursor(){
        if(null != cursorDice){
            cursorSpecialNode.detachChild(cursorDice);
            cursorDice = null;
        }
    }
    /**
     * do some actions when to gui is correct
     * @param wasClicked gui dice that was cliced
     */
    protected void DiceTrigger(Spatial wasClicked){
	Spatial inTable = wasClicked.getUserData(DICE_IN_TABLE);

	Spatial clone = wasClicked.clone();
	selectedGuiDice = wasClicked;

	clone.setLocalScale(wasClicked.getWorldScale().mult(0.6f));

	if(cursorDice != null)
	    cursorSpecialNode.detachChild(cursorDice);
	cursorDice = clone;
	cursorSpecialNode.attachChild(cursorDice);

        //cam will not rotate if cursor dice exists
        sApp.getFlyByCamera().setEnabled(false);

	rules.makeTips(inTable);
    }
    /**
     *
     * @return gui dice of current click
     */
    private Spatial requestToGuiCollide(){
	CollisionResults results = new CollisionResults();

	Vector2f cursPos = inputManager.getCursorPosition();
	Ray ray = new Ray(
		new Vector3f(cursPos.x,cursPos.y,-50),
		new Vector3f(0,0,1));
	myHandGuiNode.collideWith(ray, results);

	if(results.size() > 0){
	    return results.getClosestCollision().getGeometry().getParent().getParent().getParent();
	}
	return null;
    }
    /**
     * request collision in table dices
     * @return results of collision in table
     */
    protected CollisionResults getInTableCollide(){
        CollisionResults results = new CollisionResults();

	Vector2f cursor = inputManager.getCursorPosition();
	Vector3f origin = cam.getWorldCoordinates(cursor, 0);
	Vector3f direction = cam.getWorldCoordinates(cursor, 1).subtractLocal(origin);

	Ray ray = new Ray(origin, direction);
	tableNode.collideWith(ray, results);
        return results;
    }
    /**
     * do step in cur tip
     * @param who - whitch tip need to take and do step
     */
    protected void makeStep(Spatial who){
        StepEvent stepEvent;
	    stepEvent = rules.takeTip(who);

	    if(stepEvent != null){
	     endOfStep(stepEvent);

	     detachSelectedGuiDice();
	     clearCursor();

	     rules.removeTips();
	     sortDices();
	    }
    }
    /**
     * detach gui dice that player pused
     */
    protected void detachSelectedGuiDice(){
        if(selectedGuiDice != null){
		 myHandGuiNode.detachChild(selectedGuiDice);
		 selectedGuiDice = null;
	}
    }
    /**
     * try make step in current cursor location
     */
    private void tryMakeStep(){
	if(cursorDice != null){
	CollisionResults results = getInTableCollide();

	if(results.size() > 0){
	    makeStep(results.getClosestCollision().getGeometry().
                    getParent().getParent().getParent());
	}
	}
    }

    protected void endOfStep(StepEvent stepEvent){

    }

    Spatial getCursorDice() {
	return cursorDice;
    }

    public boolean isCursorDiceExists(){
	return cursorDice != null;
    }

    void setCursorDicePos(Vector2f curs){
	cursorDice.setLocalTranslation(curs.x, curs.y, 0);
    }

    Node getGuiNode() {
	return myHandGuiNode;
    }

    private void initDices(){
	initDicesInTable();
	sortHandDicesInTable();

	initDicesInGui();
	sortHandDicesInGui();
    }

    private void initDicesInTable(){
//	getNode().attachChild(
//		    heap.TakeFromHeap(6, 6));
//	getNode().attachChild(
//		    heap.TakeFromHeap(6, 1));
//	getNode().attachChild(
//		    heap.TakeFromHeap(6, 0));
//	getNode().attachChild(
//		    heap.TakeFromHeap(0, 0));
//	for(int i = 0 ; i < 6; i++){
//	    getNode().attachChild(
//		    heap.TakeFromHeapRandom());
//	}
    }

    public boolean TakeFromHeapRandom(){
        Spatial d = getHeap().getRandomDice();
        if(null == d) return false;

        synchronized(Mutex){
            queueAddToScreenDices.add(d);
        }
        return true;
    }

    private void makeGuiClone(Spatial dice){
        Spatial cloneToGui = dice.clone();
        cloneToGui.setLocalTranslation(Vector3f.ZERO);
        cloneToGui.scale(600);
        cloneToGui.setLocalRotation(new Quaternion().fromAngles(-15 * FastMath.DEG_TO_RAD, 0, 0));
        cloneToGui.setUserData(DICE_IN_TABLE, dice);
        myHandGuiNode.attachChild(cloneToGui);
    }

    protected void sortHandDicesInTable(){
	sortNodeDices(getNode(), dicesWidth);
    }
    /**
     * this method must be invoked after initDiceInTable because he take dices in Table Node
     */
    private void initDicesInGui(){
	for(Spatial s : getNode().getChildren()){
	    makeGuiClone(s);
	}
//	scaleCursDice = myHandGuiNode.getChild(0).getWorldScale().mult(0.6f);
    }

    private void changeSizeOfGuiDices(boolean toBig){
	final float inc = toBig ? 1.2f : 0.8f;
	for(Spatial s : myHandGuiNode.getChildren()){
	    s.scale(inc);
	}
    }

    protected void sortHandDicesInGui(){
	float width;
	if(myHandGuiNode.getChildren().size() > 10){
	    width = 60;
	    if(isDicesBig == false){
		changeSizeOfGuiDices(true);
		isDicesBig = !isDicesBig;
	    }
	}
	else{
	    width = 49;
	    if(isDicesBig == true){
		changeSizeOfGuiDices(false);
		isDicesBig = !isDicesBig;
	    }
       }
	float startPoint;
	float height = DominoApp.screenHeight / 4;
	final int size = myHandGuiNode.getChildren().size();
	if((size % 2) == 0)
	    startPoint = (-((size / 2) * width));
	else
	    startPoint = (-((((size+1) / 2) * width) - width / 2));

	for(Spatial s : myHandGuiNode.getChildren()){
//	    if(s.equals(cursorDice)) continue;
	    s.setLocalTranslation(startPoint,-height,0);
	    startPoint += width;
	}
    }

    @Override
    public void sortDices(){
	sortHandDicesInTable();
	sortHandDicesInGui();
    }
    /**
     * method for subclasses to do some actions when game come to end
     */
    public void killPlayer(){

    }

    @Override
    public void UpdateFromApplication() {
        synchronized (Mutex) {
            if (!queueAddToScreenDices.isEmpty()) {
                Spatial s = queueAddToScreenDices.remove();
                getNode().attachChild(s);
                makeGuiClone(s);
                sortDices();
            }
        }
    }
}
