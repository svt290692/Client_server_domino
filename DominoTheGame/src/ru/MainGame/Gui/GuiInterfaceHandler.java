/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;

/**
 * singleton, it keep gui settings
 * @author svt
 */
public class GuiInterfaceHandler {
    private NiftyJmeDisplay display = null;
    
    private Nifty nifty;

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
    public NiftyJmeDisplay getDisplay() {
        return display;
    }

    public void setDisplay(NiftyJmeDisplay display) {
        this.display = display;
    }
    
    private GuiInterfaceHandler() {
        
    }
    
    public static GuiInterfaceHandler getInstance() {
        return GuiInterfaceHandlerHolder.INSTANCE;
    }
    
    private static class GuiInterfaceHandlerHolder {

        private static final GuiInterfaceHandler INSTANCE = new GuiInterfaceHandler();
    }
}
