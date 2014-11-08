/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui;

import ru.MainGame.Gui.Controllers.AbstractMenuScreenController;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.TextField;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import ru.MainGame.CurrentPlayer;
import ru.MainGame.GameState;
import ru.MainGame.GlobalLogConfig;

/**
 * this Class provide menu for player octroll and listen it
 * @author svt
 */
public class MenuState extends AbstractAppState implements MenuListener{

    private SimpleApplication application;
    private AppStateManager stateManager;
    private Nifty nifty;
    
    private AtomicBoolean timeToGame = new AtomicBoolean(false);
    
    private static final Logger LOG = Logger.getLogger(MenuState.class.getName());

    public MenuState() {
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.application = (SimpleApplication)app;
        this.stateManager = stateManager;

        application.getInputManager().setCursorVisible(true);
        nifty = GuiInterfaceHandler.getInstance().getNifty();
        nifty.fromXml("Interface/menu.xml", "start");
        

            nifty = GuiInterfaceHandler.getInstance().getNifty();
            ((AbstractMenuScreenController)nifty.getScreen("start").
                    getScreenController()).setListener(this);
            ((AbstractMenuScreenController)nifty.getScreen("startGame").
                    getScreenController()).setListener(this);
            ((AbstractMenuScreenController)nifty.getScreen("ConnectToGame").
                    getScreenController()).setListener(this);
            ((AbstractMenuScreenController)nifty.getScreen("settings").
                    getScreenController()).setListener(this);
            
        LOG.fine("initialize MenuGui");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
//        if(nifty.getCurrentScreen().getScreenId().equals("hud")){
//            nifty.gotoScreen("start");
//        }
    }

    @Override
    public void cleanup() {
        LOG.log(Level.INFO,"Cleanup menu");
    }



    @Override
    public void triggerdStartGame() {
        LOG.log(Level.FINE,"triggerdStartGame");
        //might be this
//        nifty.gotoScreen("startGame");
        //or
        nifty.gotoScreen("ConnectToGame");
    }

    @Override
    public void triggerdConnectToGame() {
        LOG.log(Level.FINE,"triggerdConnectToGame");
        nifty.gotoScreen("ConnectToGame");
    }

    @Override
    public void triggerdSettings() {
        nifty.gotoScreen("settings");
        LOG.log(Level.FINE,"triggerdSettings");
    }

    @Override
    public void triggerdAbout() {
        nifty.gotoScreen("about");
        LOG.log(Level.FINE,"triggerdAbout");
    }

    @Override
    public void triggerdExit() {
        LOG.log(Level.FINE,"triggerdExit");
        application.stop();
    }
    
    /**
     * when player push the button connect me programm try to connect with server and
     * if connect established the client save in static field name connectonClient
     * to use it later in game
     */
    @Override
    public void triggerdConnectToGame_connect() {
        LOG.log(Level.FINE,"triggerdConnectToGame_connect");
        String name = nifty.getCurrentScreen().findNiftyControl(
                "TF_name", TextField.class).getDisplayedText();
        String ip = ((TextField)nifty.getScreen("ConnectToGame").findNiftyControl("TF_IPaddress", TextField.class)).getDisplayedText();
        String port = ((TextField)nifty.getScreen("ConnectToGame").findNiftyControl("TF_port", TextField.class)).getDisplayedText();
        if(name.isEmpty() || ip.isEmpty() || port.isEmpty()){
            JOptionPane.showMessageDialog(null, "You must fill all fields");
            LOG.log(Level.FINE, "some field is empty");
            return;
        }
        
        if(checkIP(ip) == false){
            JOptionPane.showMessageDialog(null, "Ip is not correct");
            LOG.log(Level.FINE, "incorrect ip entred ={0}", ip);
            return;
        }
        
        if(Integer.parseInt(port) <= 1024 || Integer.parseInt(port) > 65536){
            JOptionPane.showMessageDialog(null, "Port is not correct");
            LOG.log(Level.FINE, "incorrect port entred ={0}", port);
            return;
        }
        
        CurrentPlayer.getInstance().setName(name);
        
        ImageSelect select = nifty.getCurrentScreen().findNiftyControl("#imageSelect", ImageSelect.class);
        CurrentPlayer.getInstance().setIndexOfAvatar(select.getSelectedImageIndex());
        
        LOG.log(Level.FINE, "ip={0}port= {1}", new Object[]{ip, port});
        try {
            Client client = Network.connectToServer(ip, Integer.parseInt(port));
            LOG.log(Level.INFO, "Connect to server with ip <<{0}>> port <<{1}>>", new Object[]{ip,port});
            CurrentPlayer.getInstance().setClientOfCurSession(client);
            stateManager.detach(this);
            stateManager.attach(new GameState());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Can't Connect to server with ip << " + ip +" >> port << "+port+" >> exception: << "+ex+" >>");
            JOptionPane.showMessageDialog(null, "Can not connect to server, please check all field . message of error:\n" + ex.getMessage());
        }
    }
    
    private boolean checkIP(String ip){
        Pattern p = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
        return p.matcher(ip).matches();
    }


    @Override
    public void triggerApplySettings(AppSettings cfg) {
        application.setSettings(cfg);
        application.restart();
    }
}
