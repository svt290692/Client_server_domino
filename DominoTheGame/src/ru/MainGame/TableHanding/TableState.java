/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import ru.MainGame.DiceRotations;
import ru.MainGame.Events.ReplaceEvent;
import ru.MainGame.HeapState;
import ru.MainGame.PlayersHanding.PlacesToAttach;

/**
 *
 * @author svt
 */
public class TableState extends AbstractAppState {

    private Spatial firstDice = null;


    private Node mTableNode;
    private Node rootNode;

    private final HeapState heap;
    private final TableState.AttachManager tableManager = new TableState.AttachManager();

//    private boolean enableCinematic = false;
    private final float MDominoHeight;

    public Spatial getFirstDice() {
	return firstDice;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        SimpleApplication sApp = (SimpleApplication) app;
        this.rootNode = sApp.getRootNode();


        this.rootNode.attachChild(mTableNode);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
    }

    public TableState(HeapState heap) {
        this.heap = heap;
        MDominoHeight = heap.getDicesHeight();

	mTableNode = new Node("Table");
    }

    public Node getNode(){
        return mTableNode;
    }


    void attachDice(ReplaceEvent event,boolean Animate,boolean makePointers){

	Spatial diceInTable = event.getDiceInTable();
	Spatial diceInHand = event.getDiceInHand();
	PlacesToAttach inTable = event.getResiverPlace();
	PlacesToAttach inHand = event.getComePlace();

        DiceTableWrapper wrapper = (DiceTableWrapper)diceInTable.getUserData("wrapper");
        if(wrapper == null){
            System.err.println("Error null pointer geted please debug");
            return;
        }

        Transform newPlace;
        newPlace = tableManager.getNewTransformFrom(
                new ReplaceEvent(diceInTable, diceInHand,inTable, inHand));

        if(Animate){
            DiceAnimator animator = new DiceSimpleAnimator(
                    diceInHand,
                    diceInHand.getWorldTransform().clone(), newPlace.clone());
            animator.doAnimation(true);
        }
        else
            diceInHand.setLocalTransform(newPlace);

	if(makePointers){
	    DiceTableWrapper newWrap = new DiceTableWrapper();
	    diceInHand.setUserData("wrapper",newWrap);

	    makeAttachPointers(wrapper, newWrap, inTable, inHand);
	}
        mTableNode.attachChild(diceInHand);
    }

    void attachFirstDice(Spatial dice,boolean Animate){
            firstDice = dice;

            Transform center = tableManager.getCenterDicePlace(firstDice);
            if(Animate){ //ANIM
            DiceAnimator animator = new DiceSimpleAnimator(
                    dice,
                    dice.getWorldTransform().clone(), center);
            animator.doAnimation(true);
        }
        else
            dice.setLocalTransform(center);

            DiceTableWrapper newWrap = new DiceTableWrapper();
	    dice.setUserData("wrapper", newWrap);

            mTableNode.attachChild(dice);
    }

    private void makeAttachPointers(
            DiceTableWrapper resiver, DiceTableWrapper dice,
                PlacesToAttach resivePlace,PlacesToAttach howAttach){
        switch(resivePlace){
           case BOTTOM:
               resiver.BottomAdjacent = dice;break;
           case LEFT_LEFT:
               resiver.LeftNum_LeftAdjacent = dice;break;
           case LEFT_RIGHT:
               resiver.LeftNum_RightAdjacent = dice;break;
           case LEFT_TOP:
               resiver.LeftNum_TopAdjacent = dice;break;
           case RIGHT_LEFT:
               resiver.RightNum_LeftAdjacent = dice;break;
           case RIGHT_RIGHT:
               resiver.RightNum_RightAdjacent = dice;break;
           case RIGHT_TOP:
               resiver.RightNum_TopAdjacent = dice;break;
           case TOP:
               resiver.TopAdjacent = dice;break;
        }
        switch(howAttach){
           case BOTTOM:
               dice.BottomAdjacent = resiver;break;
           case LEFT_LEFT:
               dice.LeftNum_LeftAdjacent = resiver;break;
           case LEFT_RIGHT:
               dice.LeftNum_RightAdjacent = resiver;break;
           case LEFT_TOP:
               dice.LeftNum_TopAdjacent = resiver;break;
           case RIGHT_LEFT:
               dice.RightNum_LeftAdjacent = resiver;break;
           case RIGHT_RIGHT:
               dice.RightNum_RightAdjacent = resiver;break;
           case RIGHT_TOP:
               dice.RightNum_TopAdjacent = resiver;break;
           case TOP:
               dice.TopAdjacent = resiver;break;
        }
    }


    class AttachManager{
        static final float LeftToLeft = 90 * FastMath.DEG_TO_RAD;
        static final float LeftTotop = 0;
        static final float LeftToDown = 180 * FastMath.DEG_TO_RAD;
        static final float LeftToRight = 270 * FastMath.DEG_TO_RAD;



        Transform getNewTransformFrom(ReplaceEvent event){

            Vector3f dropPoint = new Vector3f(0,0,0);
            float dropAngle = 0;
            final float shift = MDominoHeight / 4;
	    PlacesToAttach resivePlace = event.getResiverPlace();
	    PlacesToAttach howAttach = event.getComePlace();
	    Spatial resiver = event.getDiceInTable();
	    Spatial dice = event.getDiceInHand();

            if(resivePlace == PlacesToAttach.LEFT_TOP){

                dropPoint.z -= MDominoHeight;
                if(howAttach == PlacesToAttach.LEFT_LEFT ||
                        howAttach == PlacesToAttach.RIGHT_LEFT){
                    dropPoint.x += shift;
                    dropPoint.z += shift;
                }
                else if(howAttach == PlacesToAttach.LEFT_RIGHT ||
                        howAttach == PlacesToAttach.RIGHT_RIGHT){
                    dropPoint.x -= shift;
                    dropPoint.z += shift;
                }
                else if(howAttach == PlacesToAttach.BOTTOM ||
                        howAttach == PlacesToAttach.TOP){
                    dropPoint.z += shift;
                }

                if(howAttach == PlacesToAttach.LEFT_LEFT ||
                        howAttach == PlacesToAttach.RIGHT_RIGHT ||
                        howAttach == PlacesToAttach.BOTTOM){
                    dropAngle = LeftToLeft;
                }
                else if(howAttach == PlacesToAttach.LEFT_RIGHT ||
                        howAttach == PlacesToAttach.RIGHT_LEFT ||
                        howAttach == PlacesToAttach.TOP){
                    dropAngle = LeftToRight;
                }
                else if(howAttach == PlacesToAttach.LEFT_TOP)
                    dropAngle = LeftToDown;
                else
                    dropAngle = LeftTotop;
            }
            else if(resivePlace == PlacesToAttach.RIGHT_TOP){
                dropPoint.z += MDominoHeight;

                if(howAttach == PlacesToAttach.LEFT_LEFT ||
                        howAttach == PlacesToAttach.RIGHT_LEFT){
                    dropPoint.x -= shift;
                    dropPoint.z -= shift;
                }
                else if(howAttach == PlacesToAttach.LEFT_RIGHT ||
                        howAttach == PlacesToAttach.RIGHT_RIGHT){
                    dropPoint.x += shift;
                    dropPoint.z -= shift;
                }
                else if(howAttach == PlacesToAttach.BOTTOM ||
                        howAttach == PlacesToAttach.TOP){
                    dropPoint.z -= shift;
                }

                if(howAttach == PlacesToAttach.LEFT_LEFT ||
                        howAttach == PlacesToAttach.RIGHT_RIGHT ||
                        howAttach == PlacesToAttach.BOTTOM){
                    dropAngle = LeftToRight;
                }
                else if(howAttach == PlacesToAttach.LEFT_RIGHT ||
                        howAttach == PlacesToAttach.RIGHT_LEFT ||
                        howAttach == PlacesToAttach.TOP){
                    dropAngle = LeftToLeft;
                }
                else if(howAttach == PlacesToAttach.LEFT_TOP)
                    dropAngle = LeftTotop;
                else if(howAttach == PlacesToAttach.RIGHT_TOP)
                    dropAngle = LeftToDown;
            }
            else if(resivePlace == PlacesToAttach.TOP){
                dropPoint.x += MDominoHeight - shift;

                if(howAttach == PlacesToAttach.LEFT_TOP)
                    dropAngle = LeftToLeft;
                else if(howAttach == PlacesToAttach.RIGHT_TOP)
                    dropAngle = LeftToRight;
            }
            else if((resivePlace == PlacesToAttach.BOTTOM)){
                dropPoint.x -= MDominoHeight - shift;

            if(howAttach == PlacesToAttach.LEFT_TOP)
                    dropAngle = LeftToRight;
            else if(howAttach == PlacesToAttach.RIGHT_TOP)
                    dropAngle = LeftToLeft;
            }
            else{
                if(resivePlace == PlacesToAttach.LEFT_RIGHT || resivePlace == PlacesToAttach.RIGHT_LEFT){
                    dropPoint.x += MDominoHeight - shift;

                if(howAttach == PlacesToAttach.LEFT_TOP)
                    dropAngle = LeftToLeft;
                else if(howAttach == PlacesToAttach.RIGHT_TOP)
                    dropAngle = LeftToRight;
                }
            else if(resivePlace == PlacesToAttach.LEFT_LEFT || resivePlace == PlacesToAttach.RIGHT_RIGHT){
                dropPoint.x -= MDominoHeight - shift;

                if(howAttach == PlacesToAttach.LEFT_TOP)
                dropAngle = LeftToRight;
                else if(howAttach == PlacesToAttach.RIGHT_TOP)
                dropAngle = LeftToLeft;
                }
                if(resivePlace == PlacesToAttach.LEFT_RIGHT || resivePlace == PlacesToAttach.LEFT_LEFT)
                    dropPoint.z -= shift;
                else
                    dropPoint.z += shift;
            }
            Transform old = dice.getLocalTransform().clone();
	    float[] tempAngles = new float[3];

            dice.setLocalTranslation(dropPoint);
	    old.getRotation().toAngles(tempAngles);
            dice.setLocalRotation(new Quaternion().fromAngles(tempAngles[0], dropAngle, 0));

            Node tempNode = new Node();

            Vector3f v = resiver.getWorldTranslation().clone();

            tempNode.setLocalTranslation(v);
            mTableNode.attachChild(tempNode);

            Node parent = dice.getParent();

	    if(parent != null)
            parent.detachChild(dice);

            tempNode.attachChild(dice);

	    Quaternion q = resiver.getWorldRotation().clone();
	    q.toAngles(tempAngles);
            tempNode.setLocalRotation(new Quaternion().fromAngles(0, tempAngles[1], 0));
            Transform t = dice.getWorldTransform().clone();
            tempNode.detachAllChildren();

	    if(parent != null)
            parent.attachChild(dice);

            mTableNode.detachChild(tempNode);
            dice.setLocalTransform(old);
            return t;
        }
        Transform getCenterDicePlace(Spatial dice){
            Transform newTrans = dice.getWorldTransform().clone();
            newTrans.setRotation(DiceRotations.LEFT_TO_TOP.getQuaternion());
            newTrans.setTranslation(new Vector3f(0,0,0));

            return newTrans;
        }
    }

}
