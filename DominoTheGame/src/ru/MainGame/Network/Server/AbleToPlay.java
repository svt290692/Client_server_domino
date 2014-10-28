/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.Server;

import ru.MainGame.Network.StatusPlayer;

/**
 * just info about abstract player that can play domino
 * @author svt
 */
public class AbleToPlay {
    private String myName;
    private int indexOfAvatar;
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void incScore(int inc){
        score += inc;
    }
    
    protected StatusPlayer myStatus;

    public int getIndexOfAvatar() {
        return indexOfAvatar;
    }

    public void setIndexOfAvatar(int indexOfAvatar) {
        this.indexOfAvatar = indexOfAvatar;
    }
    
    public AbleToPlay() {
	this.myName = null;
    }

    public String getName() {
	return myName;
    }

    public void setName(String myName) {
        this.myName = myName;
    }
    
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + (this.myName != null ? this.myName.hashCode() : 0);
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
	final AbleToPlay other = (AbleToPlay) obj;
	if ((this.myName == null) ? (other.myName != null) : !this.myName.equals(other.myName)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "SomePlayer,{" + "MyName=" + myName + '}';
    }

    public StatusPlayer getStatus() {
        return myStatus;
    }

    public void setStatus(StatusPlayer myStatus) {
        this.myStatus = myStatus;
    }


}
