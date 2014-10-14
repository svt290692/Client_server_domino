 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

import com.jme3.network.serializing.Serializable;
import java.util.Objects;
import ru.MainGame.DiceNumbers;

/**
 *
 * @author svt
 */
@Serializable
public class StepToSend{
    private NumsOfDice inHand;
    private NumsOfDice inTable;
    
    private  DiceNumbers inTableNum;
    private  DiceNumbers inHandNum;

    public StepToSend(NumsOfDice inHand, NumsOfDice inTable, DiceNumbers inTableNum, DiceNumbers inHandNum) {
        this.inHand = inHand;
        this.inTable = inTable;
        this.inTableNum = inTableNum;
        this.inHandNum = inHandNum;
    }
    
    public StepToSend() {
    }

    public NumsOfDice getInHand() {
        return inHand;
    }

    public void setInHand(NumsOfDice inHand) {
        this.inHand = inHand;
    }

    public NumsOfDice getInTable() {
        return inTable;
    }

    public void setInTable(NumsOfDice inTable) {
        this.inTable = inTable;
    }

    public DiceNumbers getInTableNum() {
        return inTableNum;
    }

    public void setInTableNum(DiceNumbers inTableNum) {
        this.inTableNum = inTableNum;
    }

    public DiceNumbers getInHandNum() {
        return inHandNum;
    }

    public void setInHandNum(DiceNumbers inHandNum) {
        this.inHandNum = inHandNum;
    }

    @Override
    public String toString() {
        return "StepToSend{" + "inHand=" + inHand + ", inTable=" + inTable + ", inTableNum=" + inTableNum + ", inHandNum=" + inHandNum + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.inHand);
        hash = 53 * hash + Objects.hashCode(this.inTable);
        hash = 53 * hash + Objects.hashCode(this.inTableNum);
        hash = 53 * hash + Objects.hashCode(this.inHandNum);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StepToSend other = (StepToSend) obj;
        if (!Objects.equals(this.inHand, other.inHand)) {
            return false;
        }
        if (!Objects.equals(this.inTable, other.inTable)) {
            return false;
        }
        if (this.inTableNum != other.inTableNum) {
            return false;
        }
        if (this.inHandNum != other.inHandNum) {
            return false;
        }
        return true;
    }

}
