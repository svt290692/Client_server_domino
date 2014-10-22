/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import ru.MainGame.TableHanding.DiceAnimator;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.EndNotify;

/**
 *
 * @author svt
 */
public class DiceSimpleAnimator implements DiceAnimator{
    private final Spatial mDice;
    private final Transform firstPlace;
    private final Transform endPlace;
    private EndNotify endAction = new EndNotify(){@Override public void perform() {}};
    
    public DiceSimpleAnimator(Spatial mDice, Transform firstPlace, Transform endPlace) {
        this.mDice = mDice;
        this.firstPlace = firstPlace;
        this.endPlace = endPlace;
    }
 
    @Override
    public void doAnimation(final boolean finalReplace) {
        final AnimationFactory factory = new AnimationFactory(8, "anim");
        final Transform takeOff = new Transform(
                new Vector3f((endPlace.getTranslation().x - firstPlace.getTranslation().x) / 2 ,
                firstPlace.getTranslation().y + 0.15f,
                endPlace.getTranslation().z - ((endPlace.getTranslation().z - firstPlace.getTranslation().z) / 2)),
                endPlace.getRotation().clone(), mDice.getLocalScale());
        
        factory.addTimeTransform(0f, firstPlace);
        factory.addTimeTransform(0.5f, takeOff);
//        factory.addTimeTranslation(2, endPlace.getTranslation().clone().setY(endPlace.getTranslation().y + 0.5f));
        factory.addTimeTransform(1f, endPlace);

        final AnimControl control = new AnimControl();
        final Animation anim = factory.buildAnimation();
        control.addAnim(anim);
        mDice.addControl(control);

        control.addListener(new MyAnimPostListener(anim, control,finalReplace));
        AnimationEventCounter.getInstance().captureAnimationSlot();
        control.createChannel().setAnim("anim");

    }

    public void setEndAction(EndNotify endAction) {
        this.endAction = endAction;
    }
    
    class MyAnimPostListener implements AnimEventListener{
        
        final Animation anim;
        final AnimControl control;
        final boolean finalReplace;

        public MyAnimPostListener(Animation anim, AnimControl control,boolean finalReplace){
            this.anim = anim;
            this.control = control;
            this.finalReplace = finalReplace;
        }

        @Override
        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
            if(finalReplace)
            control.getSpatial().setLocalTransform(endPlace);
            
            AnimationEventCounter.getInstance().releaseAnimationSlot();
                
            this.control.removeAnim(anim);
            mDice.removeControl(AnimControl.class);
            endAction.perform();
        }

        @Override
        public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        }
    }
    
    @Override
    public String toString() {
        return "DiceSimpleAnimator{" + "mDice=" + mDice + ", firstPlace=" + firstPlace + ", endPlace=" + endPlace + '}';
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


    public Spatial getModel() {
        return mDice;
    }

    public Transform getFirstPlace() {
        return firstPlace;
    }

    public Transform getEndPlace() {
        return endPlace;
    }
    
}
