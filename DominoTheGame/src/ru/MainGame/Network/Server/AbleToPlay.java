/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.Server;

/**
 *
 * @author svt
 */
public class AbleToPlay {
    private String myName;
    private int indexOfAvatar;

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


}
