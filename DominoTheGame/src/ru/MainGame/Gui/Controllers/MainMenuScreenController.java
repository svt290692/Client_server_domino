/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui.Controllers;

import ru.MainGame.Gui.ButtonNames;

/**
 *
 * @author svt
 */
public class MainMenuScreenController extends AbstractMenuScreenController{

    @Override
    public void buttonPushed(String what){
        switch(ButtonNames.valueOf(what)){
            case START_GAME: mListener.triggerdStartGame(); break;
            case ABOUT: mListener.triggerdAbout();break;
            case EXIT: mListener.triggerdExit();break;
        }
    }
}
