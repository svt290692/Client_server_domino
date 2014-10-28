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
public class ConnectToGameScreenController extends AbstractMenuScreenController{

    @Override
    public void buttonPushed(String what) {
        switch(ButtonNames.valueOf(what)){
            case CONNECT :mListener.triggerdConnectToGame_connect();break;
        }
    }
    
    public void goToPreviousScreen(){
        getNifty().gotoScreen("startGame");
    }
}
