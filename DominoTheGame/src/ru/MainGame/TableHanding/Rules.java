/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import ru.MainGame.Dice;
import ru.MainGame.DiceNumbers;
import ru.MainGame.Events.StepEvent;

/**
 *
 * @author svt
 */
public abstract class  Rules {
     public abstract void doStep(StepEvent event);
     public abstract void startGame(Spatial firstDice);
     public abstract boolean TryMakeTips(Spatial dice);
     public abstract boolean isGameStarted();
     public abstract StepEvent takeStepFromTip(Spatial tip);
     public abstract void removeTips();
     protected Spatial getTipFrom(AssetManager assetManager,Spatial dice){
	 Spatial clone = dice.clone();
	 Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
	 mat.getAdditionalRenderState().setWireframe(true);
	 clone.setMaterial(mat);
	 return clone;
     }
}
