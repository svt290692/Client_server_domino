 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

import com.jme3.network.serializing.Serializable;
import java.util.Objects;

/**
 *
 * @author svt
 */
@Serializable
public class StepToSend{
    private NumsOfDice inHand;
    private NumsOfDice inTable;

    public StepToSend(NumsOfDice inHand, NumsOfDice inTable) {
        this.inHand = inHand;
        this.inTable = inTable;
    }

    public StepToSend() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.inHand);
        hash = 89 * hash + Objects.hashCode(this.inTable);
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
        return true;
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

    @Override
    public String toString() {
        return "StepToSend{" + "inHand=" + inHand + ", inTable=" + inTable + '}';
    }
    
}
