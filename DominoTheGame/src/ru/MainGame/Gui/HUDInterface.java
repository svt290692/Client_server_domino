/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.MainGame.Gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ru.MainGame.Gui.Controllers.AbstractMenuScreenController;
import ru.MainGame.Gui.Controllers.HUDScreenController;

/**
 *
 * @author svt
 */
public class HUDInterface{
    private final SimpleApplication sApp;

    NiftyJmeDisplay display;
    Nifty nifty;

    public HUDInterface(SimpleApplication sApp) {
        this.sApp = sApp;
    }

    public void initialize(){

        display = new NiftyJmeDisplay(sApp.getAssetManager(), sApp.getInputManager(), sApp.getAudioRenderer(), sApp.getGuiViewPort());

        nifty = display.getNifty();
        nifty.fromXml("Interface/hud.xml", "start");

        ((HUDScreenController)nifty.getCurrentScreen().getScreenController()).setListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    readyPushed();
                }
            });

        new ControlDefinitionBuilder("player") {{
            align(Align.Center);
            valign(VAlign.Center);
            controller("de.lessvoid.nifty.controls.button.ButtonControl");
            inputMapping("de.lessvoid.nifty.input.mapping.MenuInputMapping");
            style("nifty-button");
            panel(new PanelBuilder() {{
            onStartScreenEffect(new EffectBuilder("move"){{
                length(150);
                effectParameter("mode", "in");
                effectParameter("direction", "right");
            }});
            onEndScreenEffect(new EffectBuilder("move"){{
                length(150);
                effectParameter("mode", "out");
                effectParameter("direction", "left");
            }});
            style("#panel");
            width("50px");
            height("20px");
            focusable(false);
            text(new TextBuilder("#text") {{
            style("#text");
            text(controlParameter("label"));
            }});
            }});
        }}.registerControlDefintion(nifty);

        sApp.getGuiViewPort().addProcessor(display);
    }

    public void clean(){

        nifty.exit();
        sApp.getViewPort().removeProcessor(display);
        sApp.getInputManager().setCursorVisible(false);

        nifty = null;
        display = null;
    }

    public void addlabel(String label){
    new ControlBuilder("player_" + label, "player") {{
            parameter("label", "label");
        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("topPanel"));
    }

    public void removeLabel(String label){
        try{
        nifty.getCurrentScreen().findElementByName("player_" + label).markForRemoval();
        }catch(NullPointerException ex){/*Just nothing*/}
    }

    public void makeReadyButton(){

        new ButtonBuilder("ready", "Ready!"){{
          interactOnClick("action()");
            onStartScreenEffect(new EffectBuilder("move"){{
                length(150);
                effectParameter("mode", "in");
                effectParameter("direction", "left");
            }});
            onEndScreenEffect(new EffectBuilder("move"){{
                length(150);
                effectParameter("mode", "out");
                effectParameter("direction", "left");
                alignLeft();
                valignCenter();
            }});
        }}.build(nifty, nifty.getCurrentScreen(),
        nifty.getCurrentScreen().findElementByName("toMenu"));
    }

    public void readyPushed(){
        nifty.getCurrentScreen().findElementByName("ready").markForRemoval();
    }
}
