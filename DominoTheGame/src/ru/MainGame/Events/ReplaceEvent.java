/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Events;

import com.jme3.scene.Spatial;
import ru.MainGame.PlacesToAttach;

/**
 * this class astract event physics replace of dices
 * @author svt
 */
public class ReplaceEvent {
    private final Spatial diceInTable;
    private final Spatial diceInHand;
    private final PlacesToAttach resiverPlace;
    private final PlacesToAttach comePlace;

    public ReplaceEvent(Spatial diceInTable, Spatial diceInHand,
	    PlacesToAttach inTable, PlacesToAttach inHand)throws NullPointerException{

	if(diceInHand == null || diceInTable == null ||
	       inHand == null || inTable == null)
	    throw new NullPointerException("Can not resive null pointer in ReplaceEvent");
	this.diceInTable = diceInTable;
	this.diceInHand = diceInHand;
	this.resiverPlace = inTable;
	this.comePlace = inHand;
    }

    public Spatial getDiceInTable() {
	return diceInTable;
    }

    public Spatial getDiceInHand() {
	return diceInHand;
    }

    public PlacesToAttach getResiverPlace() {
	return resiverPlace;
    }

    public PlacesToAttach getComePlace() {
	return comePlace;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 97 * hash + (this.diceInTable != null ? this.diceInTable.hashCode() : 0);
	hash = 97 * hash + (this.diceInHand != null ? this.diceInHand.hashCode() : 0);
	hash = 97 * hash + (this.resiverPlace != null ? this.resiverPlace.hashCode() : 0);
	hash = 97 * hash + (this.comePlace != null ? this.comePlace.hashCode() : 0);
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
	final ReplaceEvent other = (ReplaceEvent) obj;
	if (this.diceInTable != other.diceInTable && (this.diceInTable == null || !this.diceInTable.equals(other.diceInTable))) {
	    return false;
	}
	if (this.diceInHand != other.diceInHand && (this.diceInHand == null || !this.diceInHand.equals(other.diceInHand))) {
	    return false;
	}
	if (this.resiverPlace != other.resiverPlace) {
	    return false;
	}
	if (this.comePlace != other.comePlace) {
	    return false;
	}
	return true;
    }
}
