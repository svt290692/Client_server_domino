package ru.MainGame;

import com.jme3.app.SimpleApplication;
import java.util.logging.Logger;
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

        stateManager.attach(new MenuState());

	LOG.fine("The game was run with name :" + GAME_NAME);
    }
}
