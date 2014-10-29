/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import ru.MainGame.TableHanding.DiceAnimator;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author svt
 */
public class DiceSimpleAnimator extends DiceAnimator{
    Map<Float, Transform> mPositions = new TreeMap<>();
    public DiceSimpleAnimator(Spatial mDice, Transform firstPlace, Transform endPlace) {
        super(endPlace, firstPlace, mDice);
    }
    
    public void addBehindPlaceAndTime(Float time,Transform place){
        mPositions.put(time, place);
    }

    @Override
    public void doAnimation(boolean finalReplace) {
        final Transform takeOff = new Transform(new Vector3f((
                endPlace.getTranslation().x + firstPlace.getTranslation().x) / 2,
                firstPlace.getTranslation().y + 0.15F,
               (endPlace.getTranslation().z + firstPlace.getTranslation().z) / 2),
                endPlace.getRotation().clone(), mDice.getLocalScale());
        addBehindPlaceAndTime(0.25f, takeOff);
        super.doAnimation(finalReplace); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Float, Transform> getBehindPositions() {
        return mPositions;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiceSimpleAnimator other = (DiceSimpleAnimator) obj;
        if (this.mDice != other.mDice && (this.mDice == null || !this.mDice.equals(other.mDice))) {
            return false;
        }
        if (this.firstPlace != other.firstPlace && (this.firstPlace == null || !this.firstPlace.equals(other.firstPlace))) {
            return false;
        }
        if (this.endPlace != other.endPlace && (this.endPlace == null || !this.endPlace.equals(other.endPlace))) {
            return false;
        }
        return true;
    }


    @Override
    public Spatial getModel() {
        return mDice;
    }

    @Override
    public Transform getFirstPlace() {
        return firstPlace;
    }

    @Override
    public Transform getEndPlace() {
        return endPlace;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    
    
}
