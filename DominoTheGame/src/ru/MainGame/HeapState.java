/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.EndNotify;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.TableHanding.AnimationEventCounter;
import ru.MainGame.TableHanding.DiceAnimator;
import ru.MainGame.TableHanding.DiceHeapReplaceAnimator;

/**
 * state that contain all dices and has method to get and take dices to heap and from it
 * @author svt
 */
public class HeapState extends AbstractAppState{

    private Node myNode;
    private Node rootNode;
    private SimpleApplication app;
    private AssetManager assetManager;

    private ModelsLoader mLoader;
    private List<Spatial> listAllDices;
    private Transform[][] heapCoords;

    private static final float MDominoHeight = 0.16f;
    private static final float MDominoWidth = 0.08f;
    private static final float TableHeight = 0;
    
    private boolean awaitReturn = false;
    private boolean awaiitAnim = false;
    
    private Vector3f mLocation = new Vector3f(-1.6f, TableHeight, 0.29f);

    private static final Logger LOG = Logger.getLogger(HeapState.class.getName());

    public HeapState(ModelsLoader loader)throws IOException {
	GlobalLogConfig.initLoggerFromGlobal(LOG);
	this.mLoader = loader;

	this.myNode = new Node("Heap");
        this.myNode.setLocalTranslation(0,0,0);
	listAllDices = mLoader.load();
        heapCoords = new Transform[4][7];
    }

    public static float getDicesHeight() {
        return MDominoHeight;
    }

    public static float getDicesWidth() {
        return MDominoWidth;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication)app;
        this.assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();


        this.rootNode.attachChild(myNode);

	initMyNode();
	mLoader = null;
    }

    @Override
    public void update(float tpf) {
        if(awaitReturn == true && !AnimationEventCounter.getInstance().isAnimationInTableExists()){
            awaitReturn = false;
            replaceAllBack(awaiitAnim);
        }
    }

    @Override
    public void cleanup() {
        myNode.detachAllChildren();
    }
    
    public Node getNode(){
        return myNode;
    }
    
    public Spatial getDice(int left,int right){
        return findDiceIn(myNode.getChildren(),left,right);
    }
    
    /**
     * 
     * @param node where find dices
     * @param left num of dice
     * @param right num of dice
     * @return Spatial dice if it node contain that dice or null if in havenot it
     */
    public static Spatial findDiceIn(Collection<Spatial> node ,int left,int right){
        
        for(Spatial d:node){
            
            Dice dice = d.getControl(Dice.class);
            
            if(dice == null){
                System.err.println("error in HeapState Dice without controll name Dice");
                LOG.log(Level.WARNING, "request of dice {0} that dont have a Controll name Dice",new Object[]{d});
            }
            else
            if((dice.getLeftNum() == left && dice.getRightNum() == right))
                return d;
        }
        return null;
    }
    
    /**
     * 
     * @return random dice in heap node or null if heap is empty
     */
    public Spatial getRandomDice(){
        int size = myNode.getChildren().size();
        if(size == 0) return null;

        Random rand = new Random();
        int index = rand.nextInt(size);

        return myNode.getChildren().get(index);
    }
    
    /**
     * return and attach all dices to heap
     * @param anim activate animation or can be false if anim is not neeed fo now
     */
    public void returnAllDicesToHeap(boolean anim){
       awaitReturn = true;
       awaiitAnim = anim;
    }
    
    private void replaceAllBack(Boolean anim){
         List<Spatial> dices = getEntangledDices();
        
        int countDice = 0;
        for(int i = 0 ; i < 4; i++){
            for (int j = 0; j < 7; j++) {
                final Spatial dice = dices.get(countDice);
                Transform t = dice.getWorldTransform();
                rootNode.attachChild(dice);
                if(anim == true){
                    DiceAnimator animator = new DiceHeapReplaceAnimator(
                            dice, t,heapCoords[i][j]);
                    animator.setEndAction(new EndNotify() {

                        @Override
                        public void perform() {
                        }
                    });
                    animator.doAnimation(true);
                }
                else{
                    dice.setLocalTransform(heapCoords[i][j]);
                }
                countDice++;
                getNode().attachChild(dice);
            }
        }
    }
    
    private List<Spatial> getEntangledDices(){
        List<Spatial> newList = new ArrayList<>(listAllDices);
        Collections.shuffle(newList);
        return newList;
    }


    private void initMyNode(){
        int numDice = 0;
        float widthDist = MDominoWidth;
        float heightDist = MDominoHeight;
        Vector3f dropPoint = new Vector3f(mLocation.x,TableHeight,mLocation.z);
        
        for(int j = 0; j < 4;j++){

        for(int i = 0; i < 7;i++){

        Spatial DiceModel = listAllDices.get(numDice);

        DiceModel.scale(0.04f);
	DiceModel.setLocalRotation(new Quaternion().fromAngles(90 * FastMath.DEG_TO_RAD, 0, 0));
        DiceModel.setLocalTranslation(dropPoint);
        
        heapCoords[j][i] = DiceModel.getWorldTransform().clone();

        myNode.attachChild(DiceModel);

        numDice++;
        dropPoint.x += widthDist;
        }
        dropPoint.z -= heightDist;
        dropPoint.x = mLocation.x;
	}
    }
}
