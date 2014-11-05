/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Gui.Controllers;

import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.MainGame.Gui.ButtonNames;
import static ru.MainGame.Gui.ButtonNames.*;

/**
 *
 * @author svt
 */
public class SettingsScreenController extends AbstractMenuScreenController{
    private DropDown<String> resoluton;
    private CheckBox fullScreen;
    private CheckBox Vsynch;
    private DropDown<String> antiAliasing;
    
    List<String> fullscreenList;
    List<String> windowList;
    
    private String DISABLED = "Disabled";

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        resoluton = (DropDown<String>)screen.findNiftyControl("screenResolutionLabel_DD",DropDown.class);
        antiAliasing = (DropDown<String>)screen.findNiftyControl("AntiAliasing_DD",DropDown.class);
        fullScreen = (CheckBox)screen.findNiftyControl("Fullscreen_CB",CheckBox.class);
        Vsynch = (CheckBox)screen.findNiftyControl("Vsync_CB",CheckBox.class);
        
        windowList = new ArrayList<>(Arrays.asList(new String[]{"640x480","800x480","800x600","1024x600",
                    "1024x768","1280x720","1280x768","1280x995","1360x768"}));
        fullscreenList = new ArrayList<>(Arrays.asList(new String[]{"640x480","800x600",
                    "1024x768","1280x720","1280x768","1366x768"}));
        
        
        resoluton.addAllItems(windowList);
        
        resoluton.selectItemByIndex(0);
        antiAliasing.addAllItems(Arrays.asList(new String[]{DISABLED,"2x","4x","6x","8x","16x"}));
        antiAliasing.selectItemByIndex(0);
        fullScreen.setChecked(false);
        Vsynch.setChecked(false);
    }
    
    @Override
    public void buttonPushed(String what) {
        switch(ButtonNames.valueOf(what)){
            case APPLY_SETTINGS: 
                sendSettingsToListener();
                break;
        }
    }
    
    public void fullscreenClick(){
        resoluton.clear();
        
        if(fullScreen.isChecked()){
            resoluton.addAllItems(windowList);
            fullScreen.setChecked(false);
        }
        else{
            resoluton.addAllItems(fullscreenList);
            fullScreen.setChecked(true);
        }
    }
    
    public void sendSettingsToListener(){
        AppSettings cfg = new AppSettings(true);
        
        int leftResoluton;
        int rightResolution;
        int samples = 0;
        boolean isFullscreen;
        boolean isVSynch;
        
        leftResoluton = Integer.parseInt(resoluton.getSelection().split("x")[0]);
        rightResolution = Integer.parseInt(resoluton.getSelection().split("x")[1]);
        
        String samplesCurrent = antiAliasing.getSelection();
        
        if(!samplesCurrent.equals(DISABLED)){
            samples = Integer.parseInt(samplesCurrent.split("x")[0]);
        }
        
        isFullscreen = fullScreen.isChecked();
        isVSynch = Vsynch.isChecked();
        
        cfg.setResolution(leftResoluton, rightResolution);
        cfg.setSamples(samples);
        cfg.setFullscreen(isFullscreen);
        cfg.setVSync(isVSynch);
        cfg.setFrameRate(60);
        cfg.setFrequency(60); 
        mListener.triggerApplySettings(cfg);
    }
    
    public void goToPreviousScreen(){
        nifty.gotoScreen("start");
    }
}
