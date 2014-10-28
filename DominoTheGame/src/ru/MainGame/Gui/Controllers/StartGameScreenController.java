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
public class StartGameScreenController extends AbstractMenuScreenController{

    @Override
    public void buttonPushed(String what) {
        switch(ButtonNames.valueOf(what)){
//            case CREATE_GAME: mListener.triggerdCreateGame();break;
            case CONNECT_TO_GAME: mListener.triggerdConnectToGame();break;
        }
    }
    
    public void goToPreviousScreen(){
        getNifty().gotoScreen("start");
    }
}
