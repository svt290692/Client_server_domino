/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.network.Client;

/**
 *this class represents Player of this session
 * @author svt
 */
public class CurrentPlayer {
    private String name;
    int indexOfAvatar;
    private Client ClientOfCurSession;

    /**
     * 
     * @return index of avatar that player choice 
     */
    public int getIndexOfAvatar() {
        return indexOfAvatar;
    }
    /**
     * 
     * @param indexOfAvatar index of image in project asset
     */
    public void setIndexOfAvatar(int indexOfAvatar) {
        this.indexOfAvatar = indexOfAvatar;
    }
    
    /**
     * 
     * @return name that player was input
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return online client that contain current instance
     */
    public Client getClientOfCurSession() {
        return ClientOfCurSession;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }

    public void setClientOfCurSession(Client ClientOfCurSession) {
        this.ClientOfCurSession = ClientOfCurSession;
    }
    
    private CurrentPlayer() {
    }
    
    public static CurrentPlayer getInstance() {
        return CurrentPlayerHolder.INSTANCE;
    }
    
    private static class CurrentPlayerHolder {

        private static final CurrentPlayer INSTANCE = new CurrentPlayer();
    }
}
