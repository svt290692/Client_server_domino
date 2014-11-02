/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

/**
 * statuses of players
 * @author svt
 */
public enum StatusPlayer {
    NOT_READY,
    READY_TO_PLAY,
    WATCHER,
    IN_GAME;

    @Override
    public String toString() {
        switch(this){
            case READY_TO_PLAY: return "Ready";
            case NOT_READY: return "Not ready";
            case IN_GAME: return "";
                default: return "";
        }
    }
    
    
}
