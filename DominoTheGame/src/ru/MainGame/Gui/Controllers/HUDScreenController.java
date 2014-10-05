/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.MainGame.Gui.Controllers;

import com.jme3.input.controls.ActionListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author svt
 */
public class HUDScreenController implements ScreenController{
    java.awt.event.ActionListener listener;

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
    public void setListener(java.awt.event.ActionListener listeer){
        this.listener = listeer;
    }
    public void action(){
        listener.actionPerformed(null);
    }
}
