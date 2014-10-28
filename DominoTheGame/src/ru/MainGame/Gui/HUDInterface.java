/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.MainGame.Gui;

import com.jme3.app.SimpleApplication;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Gui.Controllers.HUDScreenController;

/**
 * it interface of game that contain methods to top panel add player and popup text and so on...
 * @author svt
 */
public class HUDInterface{
    private final SimpleApplication sApp;

    private Nifty nifty;
    private Screen hudScreen;
    
    private ActionListener exitListener;
    
    private static final Logger LOG = Logger.getLogger(HUDInterface.class.getName());
    private static final String PLAYER = "Player";
    private String textOfCurButton = null;
    private HUDScreenController mController;
    static{
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }
    
    public HUDInterface(SimpleApplication sApp) {
        this.sApp = sApp;
    }

    public void initialize(){
            nifty = GuiInterfaceHandler.getInstance().getNifty();
            nifty.fromXml("Interface/hud.xml", "hud");
            
            sApp.getInputManager().setCursorVisible(true);
            hudScreen = nifty.getScreen("hud");
            mController = ((HUDScreenController)nifty.getScreen("hud").getScreenController());
            mController.setListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        readyPushed();
                    }});
            mController.setDontWantExitListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    closePopupMenu();
                }
            });
            mController.setWantExitListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    exitListener.actionPerformed(null);
                }
            });
    }

    public void setExitListener(ActionListener ExitListener) {
        this.exitListener = ExitListener;
    }
    
    public void makePopupMenu(){
        Element el = hudScreen.findElementByName("popupExit");
        
        if(el == null){
            GuiInterfaceHandler.getInstance().getNifty().createPopupWithId("popupExit","popupExit");
            GuiInterfaceHandler.getInstance().getNifty().showPopup(hudScreen,"popupExit",null);
        }
        else{
            closePopupMenu();
        }
    }
    
    private void closePopupMenu(){
        Element el = hudScreen.findElementByName("popupExit");
        if(el != null){
            el.markForRemoval();
        }
    }
    
    public void changeStatus(String player,String status){
        Element el = hudScreen.findElementByName("state" + player);
        if(el == null)
            return;
        try{
            Label l = ((Label)el.getNiftyControl(Label.class));
            if(l != null){
                l.setText(status);
                LOG.log(Level.FINER, "ChangeStatus of {0} to {1}", new Object[]{player, l.getText()});
            }
        }catch(NullPointerException ex){
            LOG.log(Level.WARNING, "error in gui change status player ex = {0}", ex);
        }
    }
    
    public void clean(){
//        sApp.getInputManager().setCursorVisible(false);
    }

    public void makePopupText(final String text){
        
        Element element = hudScreen.
                findElementByName("popupText" + text);
        if(element != null){
            element.markForRemoval();
            return;
        }
        
        new LabelBuilder("popupText" + text){{
            text(text);
            font("/Interface/Fonts/popopFont.fnt");
            color("#000000ff");
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
            
        }}.build(nifty, hudScreen, hudScreen.
                findElementByName("toText")).startEffect(EffectEventId.onStartScreen, new EndNotify() {

            @Override
            public void perform() {
                hudScreen.findElementByName("popupText" + text).markForRemoval();
            }
        });
    }
    
    public void removeGuiObject(String name,EndNotify end){
        Element element = hudScreen.
                findElementByName(name);
        if(element != null){
            element.markForRemoval(end);
        }
    }
    
    public void makeScoreDeck(final Collection<String> stringsToPut,final boolean fish,final EndNotify end){
        final Element el = (new PanelBuilder("Score"){{
        childLayout(ElementBuilder.ChildLayoutType.Vertical);
        
        backgroundImage("/Interface/Images/deck.jpg");
        width("50.0%");
        height("50%");
        font("/Interface/Fonts/myDomFont.fnt");
        
        if(fish == true){
            image(new ImageBuilder("fish"){{
                
            onStartScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "in");
            effectParameter("direction", "bottom");
            }});
            
            onEndScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "out");
            effectParameter("direction", "bottom");
            }});
                
            filename("/Interface/Images/fish.png");
            }});
        }
        
        for(String s : stringsToPut){
            control(new LabelBuilder("scoreName" + s, s){{
                font("/Interface/Fonts/myDomFont.fnt");
            onStartScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "in");
            effectParameter("direction", "bottom");
            }});
            
            onEndScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "out");
            effectParameter("direction", "bottom");
            }});
            }});
        }
        
        control(new ButtonBuilder("closeScore", "Ok"){{
        interactOnClick("onEndLookToScore()");
            alignLeft();
            valignCenter();
            onStartScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "in");
            effectParameter("direction", "bottom");
            }});
            
            onEndScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "out");
            effectParameter("direction", "bottom");
            }});
        
        }});
        
        onStartScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "in");
            effectParameter("direction", "bottom");
        }});
        onEndScreenEffect(new EffectBuilder("move"){{
            length(200);
            effectParameter("mode", "out");
            effectParameter("direction", "bottom");
        }});
        alignCenter();
        valignCenter();
        }}.build(nifty, hudScreen, hudScreen.findElementByName("toMenu")));
        mController.setEndLookToScoreNotify(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                el.markForRemoval();
                end.perform();
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
 
        panel(new PanelBuilder("temp1"+name){{
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
        
        panel(new PanelBuilder("temp2"+name){{
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
        }}.build(nifty, hudScreen, hudScreen.findElementByName("topPanel"));
    
    }
    
    public boolean isTopPanelEmpty(){
        return (hudScreen.findElementByName("topPanel").getElements().isEmpty());
    }
    
    public void cleanTopPanel(){
        for(Element e : hudScreen.findElementByName("topPanel").getElements()){
            e.markForRemoval();
        }
    }
    
    /**
     * just remove player from top panel
     * @param name 
     */
    public void removePlayer(String name){
        Element el = hudScreen.findElementByName(PLAYER + name);
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
        }}.build(nifty, hudScreen,
        hudScreen.findElementByName("toMenu"));
    }
    
    public String getCurrentReadyButtonText(){
        return textOfCurButton;
    }
    
    public void readyPushed(){
    }
    
    public void removeCurButtonInMenu(EndNotify end){
        Element el =  hudScreen.findElementByName("ready");
            if(end != null)
                el.markForRemoval(end);
            else
                el.markForRemoval();
    }
}