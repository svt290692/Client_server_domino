/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui.Controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ru.MainGame.Gui.MenuListener;

/**
 *
 * @author svt
 */
public abstract class AbstractMenuScreenController implements ScreenController{

    protected Nifty nifty;
    protected Screen screen;
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public Nifty getNifty() {
        return nifty;
    }

    MenuListener mListener;
    public void setListener(MenuListener listener){
        mListener = listener;
    }
    abstract void buttonPushed(String what);

}
