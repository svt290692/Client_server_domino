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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author svt
 */
public class HeapState extends AbstractAppState{

    private Node myNode;
    private Node rootNode;
    private SimpleApplication app;
    private AssetManager assetManager;

    private ModelsLoader mLoader;
    private List<Spatial> listAllDices;

    private static final float MDominoHeight = 0.16f;
    private static final float MDominoWidth = 0.08f;
    private static final float TableHeight = 0;

    private static final Logger LOG = Logger.getLogger(HeapState.class.getName());

    public HeapState(ModelsLoader loader)throws IOException {
	GlobalLogConfig.initLoggerFromGlobal(LOG);
	this.mLoader = loader;

	this.myNode = new Node("Heap");
        this.myNode.setLocalTranslation(-1.6f, TableHeight, 0.29f);
	listAllDices = mLoader.load();
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
    }

    @Override
    public void cleanup() {
    }
    public Node getMyNode(){
        return myNode;
    }
    public Spatial getDice(int left,int right){
        for(Spatial d:myNode.getChildren()){
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

    public Spatial getRandomDice(){
        int size = myNode.getChildren().size();
        if(size == 0) return null;

        Random rand = new Random();
        int index = rand.nextInt(size);

        return myNode.getChildren().get(index);
    }

    private void initMyNode(){
        int countFigurInLine = 7;
        int numDice = 0;
        float widthDist = MDominoWidth;
        float heightDist = MDominoHeight;
        Vector3f dropPoint = new Vector3f(0,TableHeight,0);
        while(countFigurInLine >= 0){

        for(int i = 0; i < countFigurInLine;i++){

        Spatial DiceModel = listAllDices.get(numDice);

        DiceModel.scale(0.04f);
	DiceModel.setLocalRotation(new Quaternion().fromAngles(-90 * FastMath.DEG_TO_RAD, 0, 0));
        DiceModel.setLocalTranslation(dropPoint);

        myNode.attachChild(DiceModel);

        numDice++;
        dropPoint.z -= heightDist;
        }
        countFigurInLine--;
        dropPoint.x += widthDist;
        dropPoint.z = 0;
	}
    }
}