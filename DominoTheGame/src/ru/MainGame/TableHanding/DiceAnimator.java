/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.math.Transform;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.EndNotify;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author svt
 */
public abstract class DiceAnimator {
    protected EndNotify endAction = new EndNotify() {
        @Override
        public void perform() {
        }
    };
    protected final Transform endPlace;
    protected final Transform firstPlace;
    protected final Spatial mDice;
    Map<Float, Transform> mPositions = new TreeMap<>();

    public DiceAnimator(Transform endPlace, Transform firstPlace, Spatial mDice) {
        this.endPlace = endPlace;
        this.firstPlace = firstPlace;
        this.mDice = mDice;
    }
    
    
    public Map<Float,Transform> getBehindPositions(){
        return mPositions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.mDice != null ? this.mDice.hashCode() : 0);
        hash = 97 * hash + (this.firstPlace != null ? this.firstPlace.hashCode() : 0);
        hash = 97 * hash + (this.endPlace != null ? this.endPlace.hashCode() : 0);
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
        final DiceAnimator other = (DiceAnimator) obj;
        if (!Objects.equals(this.endAction, other.endAction)) {
            return false;
        }
        if (!Objects.equals(this.endPlace, other.endPlace)) {
            return false;
        }
        if (!Objects.equals(this.firstPlace, other.firstPlace)) {
            return false;
        }
        if (!Objects.equals(this.mDice, other.mDice)) {
            return false;
        }
        return true;
    }

    public void setEndAction(EndNotify endAction) {
        this.endAction = endAction;
    }

    @Override
    public String toString() {
        return "DiceSimpleAnimator{" + "mDice=" + mDice + ", firstPlace=" + firstPlace + ", endPlace=" + endPlace + '}';
    }
    
    protected abstract float animWholeTime();

    public void doAnimation(final boolean finalReplace) {
        final AnimationFactory factory = new AnimationFactory(animWholeTime(), "anim");
        
        factory.addTimeTransform(0.0F, firstPlace);
        
//        factory.addTimeTransform(0.25F, takeOff);
        
        for(Float f : getBehindPositions().keySet()){
            factory.addTimeTransform(f, getBehindPositions().get(f));
        }
        
        factory.addTimeTransform(0.5F, endPlace);
        
        final AnimControl control = new AnimControl();
        final Animation anim = factory.buildAnimation();
        
        control.addAnim(anim);
        mDice.addControl(control);
        
        control.addListener(new MyAnimPostListener(anim, control, finalReplace));
        AnimationEventCounter.getInstance().captureAnimationSlot();
        
        control.createChannel().setAnim("anim");
    }

    public final void addBehindPlaceAndTime(Float time, Transform place) {
        mPositions.put(time, place);
    }

    public Transform getEndPlace() {
        return endPlace;
    }

    public Transform getFirstPlace() {
        return firstPlace;
    }

    public Spatial getModel() {
        return mDice;
    }

    class MyAnimPostListener implements AnimEventListener {

        final Animation anim;
        final AnimControl control;
        final boolean finalReplace;

        public MyAnimPostListener(Animation anim, AnimControl control, boolean finalReplace) {
            super();
            this.anim = anim;
            this.control = control;
            this.finalReplace = finalReplace;
        }

        @Override
        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
            if (finalReplace) {
                control.getSpatial().setLocalTransform(endPlace);
            }
            AnimationEventCounter.getInstance().releaseAnimationSlot();
            this.control.removeAnim(anim);
            mDice.removeControl(AnimControl.class);
            endAction.perform();
        }

        @Override
        public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        }
    }
//    abstract void doAnimation(boolean finalReplace);
}
