package ru.MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Logger;
import ru.MainGame.Gui.Controllers.AbstractMenuScreenController;
import ru.MainGame.Gui.GuiInterfaceHandler;
import ru.MainGame.Gui.MenuState;

public class DominoApp extends SimpleApplication {

    public static float screenWidth;
    public static float screenHeight;
//    private final GameState runningGame;

    public static final int GAME_VERSION = 1;
    public static final String GAME_NAME = "Domino game version: " + GAME_VERSION + " PRE_ALPHA_VERSION";

    private static final Logger LOG = Logger.getLogger(DominoApp.class.getName());

    DominoApp(){
	super();
//	runningGame = new GameState();
	GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    @Override
    public void simpleInitApp(){

	screenHeight = settings.getHeight();
	screenWidth  = settings.getWidth();
        NiftyJmeDisplay display = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        GuiInterfaceHandler.getInstance().setDisplay(display);
        
        Nifty nifty = display.getNifty();
        nifty.fromXml("Interface/menu.xml", "start");
        GuiInterfaceHandler.getInstance().setNifty(nifty);
        getGuiViewPort().addProcessor(display);
        
        
        stateManager.attach(new MenuState());

	LOG.fine("The game was run with name :" + GAME_NAME);
    }
}
