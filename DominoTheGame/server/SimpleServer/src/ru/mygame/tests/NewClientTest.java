/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mygame.tests;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.MessageSpecification;

/**
 *
 * @author svt
 */
public class NewClientTest implements MessageListener<Client>, ErrorListener<Client>, ClientStateListener{
    static Client client;
    private static final Logger LOG = Logger.getLogger(NewClientTest.class.getName());
    
    static{
        Serializer.registerClasses(ExtendedSpecificationMessage.class);
    }
    
    public static void main(String[] args) {
        
        GlobalLogConfig.initGlobalLogging(Level.ALL, "logTestsServer");
        GlobalLogConfig.initLoggerFromGlobal(LOG);
        NewClientTest test = new NewClientTest();
        
        
        try {
            client = Network.connectToServer("127.0.0.1", 5511);
            
            client.start();
            client.addClientStateListener(test);
            client.addErrorListener(test);
            client.addMessageListener(test);
            LOG.log(Level.FINE, "Client started");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Client Cant connect to server");
            System.out.println("Client Cant connect to server");
        }
    }

    @Override
    public void messageReceived(Client source, Message m) {
        LOG.log(Level.FINE, "new Message:", new Object[]{m});
        System.err.println("new Message : << " + m + ">>" + "type = " + m.getClass().getName());
    }

    @Override
    public void handleError(Client source, Throwable t) {
        LOG.log(Level.SEVERE, "error from client {0} error: {1}", new Object[]{source,t});
        System.err.println("error in client << " + source + ">> error: << " + t.getMessage() + ">>");
    }

    @Override
    public void clientConnected(Client c) {
        LOG.log(Level.FINE, "client{0} was connected", new Object[]{c});
        System.err.println("client " + c + " was connected");
        
        ExtendedSpecificationMessage message = new ExtendedSpecificationMessage();
        message.setWhoSend("test");
        message.setSpecification(MessageSpecification.INITIALIZATION);
        client.send(message);
    }

    @Override
    public void clientDisconnected(Client c, DisconnectInfo info) {
        LOG.log(Level.FINE, "client{0} was DISconnected info = {1}", new Object[]{c,info});
        System.err.println("client" + c + " was DISconnected  info = " + info);
    }
}
