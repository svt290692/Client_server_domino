/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.MainGame.Gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Gui.Controllers.HUDScreenController;

/**
 *
 * @author svt
 */
public class HUDInterface{
    private final SimpleApplication sApp;

    NiftyJmeDisplay display;
    Nifty nifty;
    private static final Logger LOG = Logger.getLogger(HUDInterface.class.getName());
    private static final String PLAYER = "Player";
    private String textOfCurButton = null;
    static{
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }
    
    public HUDInterface(SimpleApplication sApp,NiftyJmeDisplay diasplay) {
        this.sApp = sApp;
        this.display = diasplay;
    }

    public void initialize(){
        synchronized(GuiInterfaceHandler.getInstance()){
            if(null == display){
                LOG.log(Level.SEVERE, "display of the gui in hud is not ready to start ERROR!!!!");
                sApp.stop();
            }
            
            nifty = display.getNifty();
            nifty.gotoScreen("hud");

            ((HUDScreenController)nifty.getScreen("hud").getScreenController()).setListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        readyPushed();
                    }
                });
        }
    }
    
    public void changeStatus(String player,String status){
        Label l = ((Label)nifty.getScreen("hud").findElementByName("state" + player).getNiftyControl(Label.class));
        if(l != null){
            l.setText(status);
            System.err.println("!!!!!ChangeStatus of "+ player + " to " + l.getText());
        }
    }
    
    

    public void clean(){
//        synchronized(GuiInterfaceHandler.getInstance()){
//            nifty.exit();
//            sApp.getViewPort().removeProcessor(display);
//            sApp.getInputManager().setCursorVisible(false);
//
//            nifty = null;
//    //        display = null;
//        }
    }

    public void makePopupText(final String text){
        new LabelBuilder("popupText" + text){{
            text(text);
            font("/Interface/Fonts/popopFont.fnt");
            color("#660000ff");
//            width("*");
            onStartScreenEffect(new EffectBuilder("fade"){{
                    effectParameter("start", "#0");
                    effectParameter("end", "#f");
//                    startDelay(2000);
                    length(1000);
//                    alternateDisable("exit");
//                    inherit(true);
                }});
                onEndScreenEffect(new EffectBuilder("fade"){{
                    effectParameter("start", "#f");
                    effectParameter("end", "#0");
                    startDelay(6000);
                    length(1000);
//                    alternateDisable("exit");
//                    inherit(false);
//                    post(true);
                    
                }});
            alignCenter();
            valignCenter();
            
        }}.build(nifty, nifty.getScreen("hud"), nifty.getScreen("hud").
                findElementByName("toText")).startEffect(EffectEventId.onStartScreen, new EndNotify() {

            @Override
            public void perform() {
                nifty.getScreen("hud").findElementByName("popupText" + text).markForRemoval();
            }
        });
        
    }
    
    /**
     * add special player element with name and status
     * @param name 
     */
    public void addPlayerToTopPanel(final String name,final String status,final int indexOfAvatar,final boolean isMainPlayer){
        
    new PanelBuilder(PLAYER+name){{
        childLayout(ElementBuilder.ChildLayoutType.Horizontal);
        width("25.0%");
        height("*");
        style("nifty-panel");
//        if(isMainPlayer)
//            backgroundColor("#33ff3323");
        
        image(new ImageBuilder("avatar"+name){{
            filename("/Interface/Images/avatar"+indexOfAvatar+".png");
            width("50px");
            height("50px");
            alignLeft();
            valignCenter();
        }});
 
        panel(new PanelBuilder("temp"+name){{
        width("35.0%");
        height("*");
        childLayout(ChildLayoutType.Vertical);
        
        control(new LabelBuilder("name"+name){{
            text(name);
            width("*");
            valignCenter();
            alignCenter();
//            valign(ElementBuilder.VAlign.Top);
        }});
        
        if(isMainPlayer)
        control(new LabelBuilder("you"+name){{
            text("(you)");
            width("*");
            valignCenter();
            alignCenter();
//            valign(ElementBuilder.VAlign.Top);
        }});
        }});
        
        panel(new PanelBuilder("temp"){{
        width("35.0%");
        height("*");
        childLayout(ChildLayoutType.Vertical);
        
        control(new LabelBuilder("state"+name){{
            text(status);
            width("*");
            height("30px");
            valignCenter();
            alignCenter();
//            valign(ElementBuilder.VAlign.Bottom);
        }});
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
        }}.build(nifty, nifty.getScreen("hud"), nifty.getScreen("hud").findElementByName("topPanel"));
    
    }
    
    public boolean isTopPanelEmpty(){
        return (nifty.getScreen("hud").findElementByName("topPanel").getElements().isEmpty());
    }
    
    public void cleanTopPanel(){
        for(Element e : nifty.getScreen("hud").findElementByName("topPanel").getElements()){
            e.markForRemoval();
        }
    }
    
    /**
     * just remove player from top panel
     * @param name 
     */
    public void removePlayer(String name){
        Element el = nifty.getScreen("hud").findElementByName(PLAYER + name);
        if(el != null)
        el.markForRemoval();
    }

    public void makeButtonInButtonLayer(String text){
        textOfCurButton = text;
        new ButtonBuilder("ready", text){{
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
        }}.build(nifty, nifty.getScreen("hud"),
        nifty.getScreen("hud").findElementByName("toMenu"));
    }
    
    public String getCurrentReadyButtonText(){
        return textOfCurButton;
    }
    
    public void readyPushed(){
//        nifty.getScreen("hud").findElementByName("ready").markForRemoval();
    }
    
    public void removeCurButtonInMenu(EndNotify end){
        Element el =  nifty.getScreen("hud").findElementByName("ready");
            if(end != null)
                el.markForRemoval(end);
            else
                el.markForRemoval();
    }
}