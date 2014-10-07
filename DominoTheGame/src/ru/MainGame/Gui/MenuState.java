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
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static NiftyJmeDisplay display = null;
    private Nifty nifty;


    private static final Logger LOG = Logger.getLogger(MenuState.class.getName());

    public MenuState() {
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    public static NiftyJmeDisplay getDisplay() {
        return display;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.application = (SimpleApplication)app;
        this.stateManager = stateManager;

        application.getInputManager().setCursorVisible(true);

        display = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());

        nifty = display.getNifty();
        nifty.fromXml("Interface/menu.xml", "start");
        ((AbstractMenuScreenController)nifty.getScreen("start").
                getScreenController()).setListener(this);
        ((AbstractMenuScreenController)nifty.getScreen("startGame").
                getScreenController()).setListener(this);
        ((AbstractMenuScreenController)nifty.getScreen("ConnectToGame").
                getScreenController()).setListener(this);

        application.getGuiViewPort().addProcessor(display);

//        new ControlDefinitionBuilder("button") {{
//            align(Align.Center);
//            valign(VAlign.Center);
//            controller("de.lessvoid.nifty.controls.button.ButtonControl");
//            inputMapping("de.lessvoid.nifty.input.mapping.MenuInputMapping");
//            style("nifty-button");
//            panel(new PanelBuilder() {{
//            style("#panel");
//            focusable(true);
//            text(new TextBuilder("#text") {{
//            style("#text");
//            text(controlParameter("label"));
//            }});
//            }});
//        }}.registerControlDefintion(nifty);
//
//        nifty.getCurrentScreen().findElementByName("mainPanel");
//
//        new ControlBuilder("theButton", "button") {{
//            parameter("label", "OK");
//        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("mainPanel"));
        LOG.fine("initialize MenuGui");

    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        nifty.exit();
        application.getViewPort().removeProcessor(display);
        application.getInputManager().setCursorVisible(false);

        nifty = null;

        LOG.fine("cleanup MenuGui");
    }



    @Override
    public void triggerdStartGame() {
        System.out.println("triggerdStartGame");
        nifty.gotoScreen("startGame");
    }

    @Override
    public void triggerdConnectToGame() {
        System.out.println("triggerdConnectToGame");
        nifty.gotoScreen("ConnectToGame");

        CurrentPlayer.getInstance().setName(
                nifty.getCurrentScreen().findNiftyControl(
                "TF_name", TextField.class).getDisplayedText());
    }

    @Override
    public void triggerdCreateGame() {
        System.out.println("triggerdCreateGame");

        CurrentPlayer.getInstance().setName(
                nifty.getCurrentScreen().findNiftyControl(
                "TF_name", TextField.class).getDisplayedText());
    }

    @Override
    public void triggerdSettings() {
        System.out.println("triggerdSettings");
    }

    @Override
    public void triggerdAbout() {
        System.out.println("triggerdAbout");
    }

    @Override
    public void triggerdExit() {
        System.out.println("triggerdExit");
//        nifty.getCurrentScreen().findElementByName("exit").removeFromFocusHandler();
//        nifty.getCurrentScreen().findElementByName("exit").disable();
//        nifty.getCurrentScreen().findElementByName("exit").disableFocus();
//        final Element e = nifty.getCurrentScreen().findElementByName("exit");
//        e.markForRemoval(new EndNotify() {
//
//            @Override
//            public void perform() {
//                nifty.getCurrentScreen().findElementByName("mainPanel").add(e);
//                e.startEffect(EffectEventId.onStartScreen);
//            }
//        });
    }
    /**
     * when player push the button connect me programm try to connect with server and
     * if connect established the client save in static field name connectonClient
     * to use it later in game
     */
    @Override
    public void triggerdConnectToGame_connect() {
        System.out.println("triggerdConnectToGame_connect");
        String ip = ((TextField)nifty.getScreen("ConnectToGame").findNiftyControl("TF_IPaddress", TextField.class)).getDisplayedText();
        String port = ((TextField)nifty.getScreen("ConnectToGame").findNiftyControl("TF_port", TextField.class)).getDisplayedText();

        System.out.println("ip=" + ip + "port= "+port);
        try {
            Client client = Network.connectToServer(ip, Integer.parseInt(port));
            LOG.log(Level.INFO, "Connect to server with ip <<{0}>> port <<{1}>>", new Object[]{ip,port});
            CurrentPlayer.getInstance().setClientOfCurSession(client);
            stateManager.detach(this);
            stateManager.attach(new GameState());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Can't Connect to server with ip << " + ip +" >> port << "+port+" >> exception: << "+ex+" >>");
            System.err.println("error connect");
        }
    }

    @Override
    public void triggerdCreateGame_create() {
        System.out.println("triggerdCreateGame_create");
    }
}
