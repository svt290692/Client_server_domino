/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mygame.tests;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.MessageSpecification;

/**
 *
 * @author svt
 */
public class ServerTestListener implements ConnectionListener, MessageListener<HostedConnection>{

    Server server;

    public ServerTestListener(Server server) {
        this.server = server;
    }
    
    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
        System.out.println("Connection added");
        ExtendedSpecificationMessage m = new ExtendedSpecificationMessage();
        m.setSpecification(MessageSpecification.INITIALIZATION);
        conn.send(m);
        
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        System.out.println("Connection removed");
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        System.out.println("message Received << " + m.getClass().getName() + ">>");
        server.broadcast(m);
    }
}
