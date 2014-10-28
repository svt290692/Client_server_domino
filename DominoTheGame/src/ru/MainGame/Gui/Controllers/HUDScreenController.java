/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.MainGame.Gui.Controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author svt
 */
public class HUDScreenController implements ScreenController{
    java.awt.event.ActionListener listener;
    java.awt.event.ActionListener endLookToScoreNotify;
    
    java.awt.event.ActionListener wantExitListener;
    java.awt.event.ActionListener dontWantExitListener;
    

    public HUDScreenController() {

    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void setWantExitListener(java.awt.event.ActionListener wantExitListener) {
        this.wantExitListener = wantExitListener;
    }

    public void setDontWantExitListener(java.awt.event.ActionListener dontWantExitListener) {
        this.dontWantExitListener = dontWantExitListener;
    }

    
    
    public void popupExit(String exit){
        if(exit.equals("no")){
            dontWantExitListener.actionPerformed(null);
        }
        else{
            wantExitListener.actionPerformed(null);
        }
    }
    
    public void setEndLookToScoreNotify(java.awt.event.ActionListener endLookToScoreNotify) {
        this.endLookToScoreNotify = endLookToScoreNotify;
    }
    
    public void setListener(java.awt.event.ActionListener listeer){
        this.listener = listeer;
    }
    
    public void action(){
        listener.actionPerformed(null);
    }
    
    public void onEndLookToScore(){
        endLookToScoreNotify.actionPerformed(null);
    }
}
