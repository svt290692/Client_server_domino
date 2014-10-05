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

/**
 *
 * @author svt
 */
public class DiceSimpleAnimator implements DiceAnimator{
    private final Spatial mDice;
    private final Transform firstPlace;
    private final Transform endPlace;
    
    public DiceSimpleAnimator(Spatial mDice, Transform firstPlace, Transform endPlace) {
        this.mDice = mDice;
        this.firstPlace = firstPlace;
        this.endPlace = endPlace;
    }
 
    @Override
    public void doAnimation(final boolean finalReplace) {
        final AnimationFactory factory = new AnimationFactory(8, "anim");
        final Transform takeOff = new Transform(
                new Vector3f(firstPlace.getTranslation().x,
                firstPlace.getTranslation().y + 0.4f,
                firstPlace.getTranslation().z),
                endPlace.getRotation().clone(), mDice.getLocalScale());
        
        factory.addTimeTransform(0, firstPlace);
        factory.addTimeTransform(1, takeOff);
        factory.addTimeTranslation(3, endPlace.getTranslation().clone().setY(endPlace.getTranslation().y + 0.5f));
        factory.addTimeTransform(4, endPlace);

        final AnimControl control = new AnimControl();
        final Animation anim = factory.buildAnimation();
        control.addAnim(anim);
        mDice.addControl(control);
//        System.out.println(control.getAnimationNames());
        
//        final AnimationEvent newEvent = new AnimationEvent(mDice, "anim", LoopMode.DontLoop);
//        newEvent.addListener(new MyAnimPostListener(anim, control,finalReplace));
        control.addListener(new MyAnimPostListener(anim, control,finalReplace));
        control.createChannel().setAnim("anim");
        
//        newEvent.play();
//        mCinematic.addCinematicEvent(0, newEvent);
//
//        
//        mCinematic.addListener(new CinematicEventListener() {
//        public void onPlay(CinematicEvent cinematic) {
//        }
//
//        public void onPause(CinematicEvent cinematic) {
//        }
//
//        public void onStop(CinematicEvent cinematic) {
//            if(finalReplace)
//            model.setLocalTransform(endPlace);
//            
//            control.clearChannels();
//            model.removeControl(control);
////            mCinematic.removeCinematicEvent(newEvent);
//        }});
//        mCinematic.play();
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
//        public void onPlay(CinematicEvent cinematic) {
//        }
//
//        public void onPause(CinematicEvent cinematic) {
//        }
//
//        public void onStop(CinematicEvent cinematic) {
//            
//        }

        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
            if(finalReplace)
            control.getSpatial().setLocalTransform(endPlace);
                
            this.control.removeAnim(anim);
            mDice.removeControl(AnimControl.class);
        }

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
