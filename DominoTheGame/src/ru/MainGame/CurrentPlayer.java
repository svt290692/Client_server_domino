/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.network.Client;
import java.util.Random;

/**
 *this class represents Player of this session
 * @author svt
 */
public class CurrentPlayer {
    private String name;
    private Client ClientOfCurSession;

    public String getName() {
        return name;
    }

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
        ///only for test
        Random r = new Random();
        name = "Player" + r.nextInt(1000);
    }
    
    public static CurrentPlayer getInstance() {
        return CurrentPlayerHolder.INSTANCE;
    }
    
    private static class CurrentPlayerHolder {

        private static final CurrentPlayer INSTANCE = new CurrentPlayer();
    }
}
