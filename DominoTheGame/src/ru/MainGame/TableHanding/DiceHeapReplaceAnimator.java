/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Map;

/**
 *
 * @author svt
 */
public class DiceHeapReplaceAnimator extends DiceAnimator{

    public DiceHeapReplaceAnimator(Spatial mDice,Transform firstPlace, Transform endPlace) {
        super(endPlace, firstPlace, mDice);
        final Transform takeOff = new Transform(new Vector3f((
                endPlace.getTranslation().x + firstPlace.getTranslation().x) / 2,
                firstPlace.getTranslation().y + 0.45F,
               (endPlace.getTranslation().z + firstPlace.getTranslation().z) / 2),
                endPlace.getRotation().clone(), mDice.getLocalScale());
        addBehindPlaceAndTime(0.25f, takeOff);
    }
    
    @Override
    public Map<Float, Transform> getBehindPositions() {
        return mPositions;
    }

    @Override
    protected float animWholeTime() {
        return 1.0f;
    }

}
