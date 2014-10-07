/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.scene.Spatial;
import java.util.Random;
import ru.MainGame.Dice;
import ru.MainGame.DiceNumbers;
import ru.MainGame.DiceRotations;
import ru.MainGame.Events.ReplaceEvent;
import ru.MainGame.Events.StepEvent;
import ru.MainGame.PlayersHanding.PlacesToAttach;

/**
 *
 * @author svt
 */
public class ClassicRules extends Rules{

    private final TableState mTable;

    private final byte maxBoneToSide = 10;

    private byte moreDicesLeft = maxBoneToSide / 2;
    private byte moreDicesRight = moreDicesLeft;

    private boolean GameStarted = false;
    private boolean curDirLeftToLeft = true;
    private boolean curDirRightToLeft = false;

    private DiceRotations leftDiceRotation;
    private DiceRotations rightDiceRotation;

    private DiceNumbers leftFreeDice;
    private DiceNumbers rightFreeDice;

    private Spatial rightDice = null;
    private Spatial leftDice = null;

    private Spatial leftTip;
    private Spatial rightTip;

    private final AssetManager assetManager;

    private final String MAPPING_PREF_TO_LEFT = "pref to left";
    public ClassicRules(TableState mTable, AssetManager assetManager) {
	this.mTable = mTable;
	this.assetManager = assetManager;
    }

    public boolean isGameStarted(){
	return GameStarted;
    }

    public void startGame(Spatial firstDice){
	GameStarted = true;
	leftDice = rightDice = firstDice;
	mTable.attachFirstDice(firstDice, false);
    }

    public void doStep(StepEvent event)throws IllegalStateException,IllegalArgumentException{
	if(!isStepCorrect(event))
	    throw new IllegalStateException(" Numbers of dices is not identical");

	ReplaceEvent replaceEvent = convertAndCommitEvent(event,true);
        mTable.attachDice(replaceEvent, false,true);
    }

    private boolean isStepCorrect(StepEvent event)throws IllegalArgumentException{
	Dice dice = event.getDiceInHand().getControl(Dice.class);
	Dice resiver = event.getDiceInTable().getControl(Dice.class);
	Spatial inTable = event.getDiceInTable();

	if(inTable.equals(mTable.getFirstDice())){
	    if(resiver.getBothNum() != -1){
		if(resiver.getBothNum() != dice.getLeftNum()&&
		   resiver.getBothNum() != dice.getRightNum())
		    return false;
                else{
                    if(dice.getNum(event.getInHandNum()) != resiver.getBothNum()){
                        return false;
                    }
                }
	    }
	    else{
		//TODO if First Dice has not BOTH NUM
	    }
	}
	else{
	    if(inTable.equals(leftDice)){
		if(resiver.getNum(leftFreeDice) != dice.getNum(event.getInHandNum()))
		    return false;
	    }
	    else if(inTable.equals(rightDice)){
		if(resiver.getNum(rightFreeDice) != dice.getNum(event.getInHandNum()))
		    return false;
	    }
	    else if(!inTable.equals(leftDice) && !inTable.equals(rightDice))
		return false;
	}
	return true;
    }

    private ReplaceEvent convertAndCommitEvent(StepEvent event,boolean isRealStep)throws IllegalArgumentException{

	DiceNumbers inHand = event.getInHandNum();
	Spatial diceInHand = event.getDiceInHand();
	Spatial diceInTable = event.getDiceInTable();
        PlacesToAttach tablePlace = null;
        PlacesToAttach dicePlace = null;

        if(inHand == DiceNumbers.LEFT_NUM){
	    dicePlace = PlacesToAttach.LEFT_TOP;
	    if(isRealStep){
		if(diceInTable.equals(leftDice))leftFreeDice = DiceNumbers.RIGHT_NUM;
		else if(diceInTable.equals(rightDice))rightFreeDice = DiceNumbers.RIGHT_NUM;
		else throw new IllegalArgumentException("dice in table is not left dice and not right dice");
	    }
	}
        else if(inHand == DiceNumbers.RIGHT_NUM){
	    dicePlace = PlacesToAttach.RIGHT_TOP;
	    if(isRealStep){
		if(diceInTable.equals(leftDice))leftFreeDice = DiceNumbers.LEFT_NUM;
		else if(diceInTable.equals(rightDice))rightFreeDice = DiceNumbers.LEFT_NUM;
		else throw new IllegalArgumentException("dice in table is not left dice and not right dice");
	    }
	}
	else{
	    dicePlace = PlacesToAttach.BOTTOM;
	    if(isRealStep){
		if(diceInTable.equals(leftDice))leftFreeDice = DiceNumbers.BOTH_NUM;
		else if(diceInTable.equals(rightDice))rightFreeDice = DiceNumbers.BOTH_NUM;
		else throw new IllegalArgumentException("dice in table is not leftdice and not right dice");
	    }
	}

	if(leftDice.equals(rightDice)){
	    boolean isToLeft = false;
	    Random rand = new Random();

	    Boolean pref = diceInHand.getUserData(MAPPING_PREF_TO_LEFT);
	    if(pref == null){
		if(rand.nextInt(2) == 0 )isToLeft = true;
		else isToLeft = false;
	    }
	    else{
		isToLeft = pref.booleanValue();
	    }

	    if(isToLeft == true){
		tablePlace = PlacesToAttach.BOTTOM;
		if(isRealStep){
		    leftDice = diceInHand;
		    moreDicesLeft--;
		    if(dicePlace == PlacesToAttach.LEFT_TOP)leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
		    else leftDiceRotation = DiceRotations.LEFT_TO_LEFT;

		    if(inHand == DiceNumbers.LEFT_NUM) leftFreeDice = DiceNumbers.RIGHT_NUM;
		    else leftFreeDice = DiceNumbers.RIGHT_NUM;
		}
	    }
	    else{
		tablePlace = PlacesToAttach.TOP;
		if(isRealStep){
		    rightDice = diceInHand;
		    moreDicesRight--;
		    if(dicePlace == PlacesToAttach.LEFT_TOP)rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
		    else rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;

		    if(inHand == DiceNumbers.LEFT_NUM) rightFreeDice = DiceNumbers.RIGHT_NUM;
		    else rightFreeDice = DiceNumbers.LEFT_NUM;
		}
	    }
	}
	else if(diceInTable == mTable.getFirstDice()){
	    if(leftDice == mTable.getFirstDice()){
		tablePlace = PlacesToAttach.BOTTOM;
		if(isRealStep){
		    leftDice = diceInHand;
		    moreDicesLeft--;
		    if(dicePlace == PlacesToAttach.LEFT_TOP)leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
		    else leftDiceRotation = DiceRotations.LEFT_TO_LEFT;

		    if(inHand == DiceNumbers.LEFT_NUM) leftFreeDice = DiceNumbers.RIGHT_NUM;
		    else leftFreeDice = DiceNumbers.LEFT_NUM;
		}
	    }
	    else{
		tablePlace = PlacesToAttach.TOP;
		if(isRealStep){
		    rightDice = diceInHand;
		    moreDicesRight--;
		    if(dicePlace == PlacesToAttach.LEFT_TOP)rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
		    else rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;

		    if(inHand == DiceNumbers.LEFT_NUM) rightFreeDice = DiceNumbers.RIGHT_NUM;
		    else rightFreeDice = DiceNumbers.LEFT_NUM;
		}
	    }
	}
	else{

	    if(diceInTable.equals(leftDice)){
		if(curDirLeftToLeft == true){
		    if(moreDicesLeft > 1){
			if(leftDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    tablePlace = PlacesToAttach.LEFT_TOP;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    tablePlace = PlacesToAttach.RIGHT_TOP;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
			    tablePlace = PlacesToAttach.BOTTOM;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    tablePlace = PlacesToAttach.TOP;
			}

			if(isRealStep){
			    if(dicePlace == PlacesToAttach.LEFT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    moreDicesLeft--;
			}
		    }
		    else if(moreDicesLeft == 1){
			if(leftDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else{
				tablePlace = PlacesToAttach.LEFT_RIGHT;
				if(isRealStep){
				    moreDicesLeft--;
				}
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else{
				tablePlace = PlacesToAttach.RIGHT_RIGHT;
				if(isRealStep){
				    moreDicesLeft--;
				}
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
				tablePlace = PlacesToAttach.LEFT_TOP;
				if(isRealStep){
	    			    moreDicesLeft--;
				}
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
				tablePlace = PlacesToAttach.RIGHT_TOP;
				if(isRealStep){
				    moreDicesLeft--;
				}
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				leftDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_TOP;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_DOWN;
			}
		    }
		    else if(moreDicesLeft == 0){

			if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else
				tablePlace = PlacesToAttach.LEFT_RIGHT;
			    if(isRealStep){
				moreDicesLeft = maxBoneToSide;
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else
				tablePlace = PlacesToAttach.RIGHT_RIGHT;
			    if(isRealStep){
				moreDicesLeft = maxBoneToSide;
			    }
			}
			if(isRealStep){
			    curDirLeftToLeft = !curDirLeftToLeft;
			    if(dicePlace == PlacesToAttach.BOTTOM)
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			}
		    }
		}
		else{
		    if(moreDicesLeft > 1){
			if(leftDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    tablePlace = PlacesToAttach.RIGHT_TOP;

			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    tablePlace = PlacesToAttach.LEFT_TOP;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
			    tablePlace = PlacesToAttach.TOP;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    tablePlace = PlacesToAttach.BOTTOM;
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.LEFT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_TOP;
			    moreDicesLeft--;
			}
		    }
		    else if(moreDicesLeft == 1){
			if(leftDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else{
				tablePlace = PlacesToAttach.RIGHT_LEFT;
				if(isRealStep){
				    moreDicesLeft--;
				}
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else{
				tablePlace = PlacesToAttach.LEFT_LEFT;
				if(isRealStep){
				    moreDicesLeft--;
				}
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
				tablePlace = PlacesToAttach.LEFT_TOP;
			    moreDicesLeft--;
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
				tablePlace = PlacesToAttach.RIGHT_TOP;
				if(isRealStep){
				    moreDicesLeft--;
				}
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				leftDiceRotation = DiceRotations.LEFT_TO_TOP;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_TOP;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_DOWN;
			}
		    }
		    else if(moreDicesLeft == 0){

			if(leftDiceRotation == DiceRotations.LEFT_TO_TOP){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else
				tablePlace = PlacesToAttach.LEFT_LEFT;
			    if(isRealStep){
				moreDicesLeft = maxBoneToSide;
			    }
			}
			else if(leftDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else
				tablePlace = PlacesToAttach.RIGHT_LEFT;
			    if(isRealStep){
				moreDicesLeft = maxBoneToSide;
			    }
			}
			if(isRealStep){
			    curDirLeftToLeft = !curDirLeftToLeft;
			    if(dicePlace == PlacesToAttach.BOTTOM)
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				leftDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else
				leftDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			}
		    }
		}
		if(isRealStep){
		    leftDice = diceInHand;
		}
	    }
	    else if(diceInTable.equals(rightDice)){
		if(curDirRightToLeft == true){
		    if(moreDicesRight > 1){
			if(rightDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    tablePlace = PlacesToAttach.LEFT_TOP;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    tablePlace = PlacesToAttach.RIGHT_TOP;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
			    tablePlace = PlacesToAttach.BOTTOM;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    tablePlace = PlacesToAttach.TOP;
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.LEFT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    moreDicesRight--;
			}
		    }
		    else if(moreDicesRight == 1){
			if(rightDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else{
				tablePlace = PlacesToAttach.LEFT_LEFT;
				if(isRealStep){
				    moreDicesRight--;
				}
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else{
				tablePlace = PlacesToAttach.RIGHT_LEFT;
				if(isRealStep){
				    moreDicesRight--;
				}
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
				tablePlace = PlacesToAttach.RIGHT_TOP;
				if(isRealStep){
				    moreDicesRight--;
				}
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
				tablePlace = PlacesToAttach.LEFT_TOP;
				if(isRealStep){
				    moreDicesRight--;
				}
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				rightDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_TOP;
			}
		    }
		    else if(moreDicesRight == 0){

			if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else
				tablePlace = PlacesToAttach.RIGHT_LEFT;
			    if(isRealStep){
				moreDicesRight = maxBoneToSide;
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else
				tablePlace = PlacesToAttach.LEFT_LEFT;
			    if(isRealStep){
				moreDicesRight = maxBoneToSide;
			    }
			}
			if(isRealStep){
			    curDirRightToLeft = !curDirRightToLeft;
			    if(dicePlace == PlacesToAttach.BOTTOM)
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
			}
		    }
		}
		else{///
		    if(moreDicesRight > 1){
			if(rightDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    tablePlace = PlacesToAttach.RIGHT_TOP;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    tablePlace = PlacesToAttach.LEFT_TOP;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
			    tablePlace = PlacesToAttach.TOP;
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    tablePlace = PlacesToAttach.BOTTOM;
			}

			if(isRealStep){
			    if(dicePlace == PlacesToAttach.LEFT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_TOP;
			    moreDicesRight--;
			}
		    }
		    else if(moreDicesRight == 1){
			if(rightDiceRotation == DiceRotations.LEFT_TO_LEFT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else{
				tablePlace = PlacesToAttach.RIGHT_RIGHT;
				if(isRealStep){
				    moreDicesRight--;
				}
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_RIGHT){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else{
				tablePlace = PlacesToAttach.LEFT_RIGHT;
				if(isRealStep){
				    moreDicesRight--;
				}
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
				tablePlace = PlacesToAttach.RIGHT_TOP;
				if(isRealStep){
				    moreDicesRight--;
				}
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
				tablePlace = PlacesToAttach.LEFT_TOP;
				if(isRealStep){
				    moreDicesRight--;
				}
			}
			if(isRealStep){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				rightDiceRotation = DiceRotations.LEFT_TO_TOP;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_DOWN;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_TOP;
			}
		    }
		    else if(moreDicesRight == 0){

			if(rightDiceRotation == DiceRotations.LEFT_TO_TOP){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.RIGHT_TOP;
			    else
				tablePlace = PlacesToAttach.RIGHT_RIGHT;
			    if(isRealStep){
				moreDicesRight = maxBoneToSide;
			    }
			}
			else if(rightDiceRotation == DiceRotations.LEFT_TO_DOWN){
			    if(dicePlace == PlacesToAttach.BOTTOM)
				tablePlace = PlacesToAttach.LEFT_TOP;
			    else
				tablePlace = PlacesToAttach.LEFT_RIGHT;
			    if(isRealStep){
				moreDicesRight = maxBoneToSide;
			    }
			}
			if(isRealStep){
			    curDirRightToLeft = !curDirRightToLeft;
			    if(dicePlace == PlacesToAttach.BOTTOM)
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			    else if(dicePlace == PlacesToAttach.RIGHT_TOP)
				rightDiceRotation = DiceRotations.LEFT_TO_LEFT;
			    else
				rightDiceRotation = DiceRotations.LEFT_TO_RIGHT;
			}
		    }
		}
		if(isRealStep){
		    rightDice = diceInHand;
		}
	    }
	    else{
		throw new IllegalArgumentException("Incorrect Dice Place In StepEvent");
	    }

	}
	return new ReplaceEvent(diceInTable, diceInHand, tablePlace, dicePlace);
    }

    public void makeTips(Spatial dice) {
	removeTips();

	StepEvent leftTip = null;
	StepEvent rightTip = null;
	Dice controll = dice.getControl(Dice.class);
	if(controll.getBothNum() != -1){
	    StepEvent leftCheckToBoth = new StepEvent(leftDice, dice, leftFreeDice,DiceNumbers.BOTH_NUM);
	    StepEvent rightCheckToBoth = new StepEvent(rightDice, dice, rightFreeDice, DiceNumbers.BOTH_NUM);
	    if(isStepCorrect(leftCheckToBoth)) leftTip = leftCheckToBoth;
	    else if(isStepCorrect(rightCheckToBoth)) rightTip = rightCheckToBoth;
	}
	else{
	    StepEvent leftCheckToLeft = new StepEvent(leftDice, dice,leftFreeDice, DiceNumbers.LEFT_NUM);
	    StepEvent leftCheckToRight = new StepEvent(leftDice, dice,leftFreeDice, DiceNumbers.RIGHT_NUM);
	    StepEvent rightCheckToLeft = new StepEvent(rightDice, dice,rightFreeDice,DiceNumbers.LEFT_NUM);
	    StepEvent rightCheckToRight = new StepEvent(rightDice, dice,rightFreeDice, DiceNumbers.RIGHT_NUM);
	    if(isStepCorrect(leftCheckToLeft)) leftTip = leftCheckToLeft;
	    else if(isStepCorrect(leftCheckToRight)) leftTip = leftCheckToRight;

	    if(isStepCorrect(rightCheckToLeft)) rightTip = rightCheckToLeft;
	    else if(isStepCorrect(rightCheckToRight)) rightTip = rightCheckToRight;
	}
	if(leftTip != null){
	    Spatial tip = getTipFrom(assetManager, dice);
	    tip.setUserData(MAPPING_PREF_TO_LEFT, true);
	    StepEvent tipEvent = new
		    StepEvent(leftTip.getDiceInTable(), tip,
		    leftTip.getInTableNum(), leftTip.getInHandNum());
	    mTable.attachDice(convertAndCommitEvent(tipEvent, false),false,false);

	    tip.setUserData("tip",leftTip);
	    this.leftTip = tip;
	}
	if(rightTip != null){
	    Spatial tip = getTipFrom(assetManager, dice);
	    tip.setUserData(MAPPING_PREF_TO_LEFT, false);
	    StepEvent tipEvent = new
		    StepEvent(rightTip.getDiceInTable(), tip,
		    rightTip.getInTableNum(), rightTip.getInHandNum());
	    mTable.attachDice(convertAndCommitEvent(tipEvent, false),false,false);

	    tip.setUserData("tip",rightTip);
	    this.rightTip = tip;
	}
    }
    /**
     *
     * @param tip Spatial that player click thought that a tip
     * @return return true if tip is correct else return false
     */
    @Override
    public StepEvent takeTip(Spatial tip){
	StepEvent tipEvent = tip.getUserData("tip");
	if(tipEvent == null) return null;

	if(rightDice.equals(leftDice)){
	    if(tip.getLocalTranslation().x < 0)
		tipEvent.getDiceInHand().setUserData(MAPPING_PREF_TO_LEFT, true);
	    else
		tipEvent.getDiceInHand().setUserData(MAPPING_PREF_TO_LEFT, false);
	}
	doStep(tipEvent);
	return tipEvent;
    }
    public void removeTips(){
	if(leftTip != null){
	    mTable.getMyNode().detachChild(leftTip);
	    leftTip = null;
	}
	if(rightTip != null){
	    mTable.getMyNode().detachChild(rightTip);
	    rightTip = null;
	}
    }
}
