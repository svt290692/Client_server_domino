/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui;

/**
 *
 * @author svt
 */
public interface MenuListener {

    void triggerdStartGame();
        void triggerdConnectToGame();
            void triggerdConnectToGame_connect();
        void triggerdCreateGame();
            void triggerdCreateGame_create();

    void triggerdSettings();
    void triggerdAbout();
    void triggerdExit();



}
