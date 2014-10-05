/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

/**
 *
 * @author svt
 */
public enum DiceRotations {
    LEFT_TO_LEFT(90 * FastMath.DEG_TO_RAD),
    LEFT_TO_RIGHT(270 * FastMath.DEG_TO_RAD),
    LEFT_TO_TOP(0),
    LEFT_TO_DOWN(180 * FastMath.DEG_TO_RAD);

    private final float f;

    private DiceRotations(float f) {
        this.f = f;
    }
    public float getAxis(){
    return f;
    }
    public Quaternion getQuaternion(){
        return new Quaternion().fromAngles(-90 * FastMath.DEG_TO_RAD, f, 0);
    }
}
