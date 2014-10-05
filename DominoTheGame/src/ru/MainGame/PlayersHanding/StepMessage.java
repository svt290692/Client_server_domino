/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.network.AbstractMessage;
import ru.MainGame.Events.StepEvent;

/**
 *
 * @author svt
 */
public class StepMessage extends AbstractMessage{
    StepEvent stepEvent;

    public StepMessage(StepEvent stepEvent) {
	this.stepEvent = stepEvent;
    }

    public StepEvent getStepEvent() {
	return stepEvent;
    }

    public void setStepEvent(StepEvent stepEvent) {
	this.stepEvent = stepEvent;
    }

    @Override
    public String toString() {
	return "StepMessage{" + "stepEvent=" + stepEvent + '}';
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 89 * hash + (this.stepEvent != null ? this.stepEvent.hashCode() : 0);
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
	final StepMessage other = (StepMessage) obj;
	if (this.stepEvent != other.stepEvent && (this.stepEvent == null || !this.stepEvent.equals(other.stepEvent))) {
	    return false;
	}
	return true;
    }

}
