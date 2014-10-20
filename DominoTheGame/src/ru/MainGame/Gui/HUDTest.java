/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import ru.MainGame.Gui.Controllers.AbstractMenuScreenController;

/**
 *
 * @author svt
 */
public class HUDTest extends SimpleApplication{

    @Override
    public void simpleInitApp() {
        getInputManager().setCursorVisible(true);

        NiftyJmeDisplay display = new NiftyJmeDisplay(getAssetManager(),getInputManager(), getAudioRenderer(), getGuiViewPort());
        
        Nifty nifty = display.getNifty();
//        ((AbstractMenuScreenController)nifty.getScreen("start").
//                getScreenController()).setListener(this);
//        ((AbstractMenuScreenController)nifty.getScreen("startGame").
//                getScreenController()).setListener(this);
//        ((AbstractMenuScreenController)nifty.getScreen("ConnectToGame").
//                getScreenController()).setListener(this);

//        getGuiViewPort().addProcessor(display);

        HUDInterface interf = new HUDInterface(this,display);
        interf.initialize();
        
//        new ControlBuilder("player1", "player1"){{
//            parameter("name", "ppp");
//            parameter("state", "SSS");
//        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("topPanel"));
//        new ButtonBuilder("B1", "PUSH ME!!!"){{
//            alignLeft();
//        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("topPanel"));
//        new ButtonBuilder("B1", "PUSH ME!!!"){{
//            alignRight();
//        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("topPanel"));
        final String name = "name";
        final String status = "status";
        new PanelBuilder("player"+name){{
            childLayout(ElementBuilder.ChildLayoutType.Center);
            width("10.0%");
            height("*");
            control(new LabelBuilder("name"){{
                text(name);
                valignTop();
                valign(ElementBuilder.VAlign.Top);
            }});
            control(new LabelBuilder("state"){{
                text(status);
                valignBottom();
                valign(ElementBuilder.VAlign.Bottom);
            }});
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
        }}.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("topPanel"));
        
    }
    public static void main(String args[]){
        HUDTest test = new HUDTest();
        test.start();
    }
}
