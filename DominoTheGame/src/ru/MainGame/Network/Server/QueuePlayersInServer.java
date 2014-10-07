/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.Server;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author svt
 */
public class QueuePlayersInServer implements QueuePlayers{

    public final List<AbleToPlay> mPlayers;


    public QueuePlayersInServer() {
	this.mPlayers = new LinkedList<>();
    }

    @Override
    public AbleToPlay nextPlayter() {
	return mPlayers.get(0);
    }

    @Override
    public void wasStep() {
	Collections.swap(mPlayers, 0, mPlayers.size()-1);
    }

    @Override
    public void removePlayer(AbleToPlay player) {
	mPlayers.remove(player);
    }

    @Override
    public void addPlayer(AbleToPlay player) {
	mPlayers.add(player);
    }

    @Override
    public List<AbleToPlay> asList() {
	return new LinkedList<>(mPlayers);
    }

    @Override
    public void addNewElements(Collection from) {
        mPlayers.addAll(from);
    }

}
