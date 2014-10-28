/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.Server;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author svt
 */
public interface QueuePlayers {
    /**
     * this method give you next player in queue but it not pop him and not push him bach
     * @return next player that will need give timestep in game
     */
    public AbleToPlay nextPlayter();
    /**
     * if you call this method next player in queue will pop and insert to end of queue
     */
    public void wasStep();
    /**
     * Delete player from it queue
     * @param player player that need to delete from queue
     */
    public void removePlayer(AbleToPlay player);
    /**
     * push New Player back to queue
     * @param player player that need to add to queue
     */
    public void addPlayer(AbleToPlay player);
    /**
     * present queue as list
     * @return list of players in this queue
     */
    public List<AbleToPlay> asList();
    
    public void addNewElements(Collection from);
}
