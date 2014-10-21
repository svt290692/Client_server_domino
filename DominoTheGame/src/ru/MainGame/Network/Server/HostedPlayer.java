/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.Server;

import com.jme3.network.HostedConnection;
import ru.MainGame.Network.StatusPlayer;
import ru.MainGame.Network.StatusPlayer;


/**
 *
 * @author svt
 */
public class HostedPlayer extends AbleToPlay{

    private HostedConnection mConnection;
    private StatusPlayer myStatus;
    
    public HostedPlayer(HostedConnection connection) {
	super();
	myStatus = StatusPlayer.NOT_READY;
        mConnection = connection;
    }

    public StatusPlayer getStatus() {
        return myStatus;
    }

    public void setStatus(StatusPlayer myStatus) {
        this.myStatus = myStatus;
    }

    public void setmConnection(HostedConnection mConnection) {
	this.mConnection = mConnection;
    }

    public HostedConnection getmConnection() {
	return mConnection;
    }



}
