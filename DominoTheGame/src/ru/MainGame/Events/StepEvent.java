/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Events;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;
import java.io.IOException;
import ru.MainGame.DiceNumbers;

/**
 *
 * @author svt
 */
public class StepEvent implements Savable{
    private final Spatial diceInTable;
    private final Spatial diceInHand;
    private final DiceNumbers inTableNum;
    private final DiceNumbers inHandNum;

    public StepEvent(Spatial diceInTable, Spatial diceInHand,
	    DiceNumbers inTable, DiceNumbers inHand) {
	this.diceInTable = diceInTable;
	this.diceInHand = diceInHand;
	this.inTableNum = inTable;
	this.inHandNum = inHand;
    }

    public Spatial getDiceInTable() {
	return diceInTable;
    }

    public Spatial getDiceInHand() {
	return diceInHand;
    }

    public DiceNumbers getInTableNum() {
	return inTableNum;
    }

    public DiceNumbers getInHandNum() {
	return inHandNum;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 23 * hash + (this.diceInTable != null ? this.diceInTable.hashCode() : 0);
	hash = 23 * hash + (this.diceInHand != null ? this.diceInHand.hashCode() : 0);
	hash = 23 * hash + (this.inTableNum != null ? this.inTableNum.hashCode() : 0);
	hash = 23 * hash + (this.inHandNum != null ? this.inHandNum.hashCode() : 0);
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
	final StepEvent other = (StepEvent) obj;
	if (this.diceInTable != other.diceInTable && (this.diceInTable == null || !this.diceInTable.equals(other.diceInTable))) {
	    return false;
	}
	if (this.diceInHand != other.diceInHand && (this.diceInHand == null || !this.diceInHand.equals(other.diceInHand))) {
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

    public void write(JmeExporter ex) throws IOException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void read(JmeImporter im) throws IOException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
