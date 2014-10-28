/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui;

import com.jme3.system.AppSettings;

/**
 * listener of all menu buttons
 * @author svt
 */
public interface MenuListener {

    void triggerdStartGame();
        void triggerdConnectToGame();
            void triggerdConnectToGame_connect();
//        void triggerdCreateGame();

    void triggerdSettings();
        void triggerApplySettings(AppSettings cfg);
    void triggerdAbout();
    void triggerdExit();
    
}
