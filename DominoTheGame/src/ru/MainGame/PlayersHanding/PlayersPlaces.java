/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.math.Vector3f;

/**
 * places in scene that need put players dices
 * @author svt
 */
public enum PlayersPlaces {
    MAIN_PLAYER(0f,1f),
    FIRST_PLAEYR(-1.5f,-1f),
    SECOND_PLAEYR(0f,-1f),
    THIRD_PLAEYR(1.5f,-1f);

    private float x,z;

    private PlayersPlaces(float x, float z) {
	this.x = x;
	this.z = z;
    }
    public Vector3f getVector(){
	return new Vector3f(x,0,z);
    }
}
