package ru.MainGame;

import com.jme3.network.serializing.Serializer;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StepToSend;

/**
 * the game starter class start game and set default settings for application
 * @author svt
 */
public class GameStarter{

    static{
        
        Serializer.registerClasses(ExtendedSpecificationMessage.class,
                StartGameMessage.class, NumsOfDice.class, StepToSend.class);
    }

    private static final Logger LOG = Logger.getLogger(GameStarter.class.getName());

    public static void main(String[] args) {
	GlobalLogConfig.initGlobalLogging(Level.ALL,null);
	GlobalLogConfig.initLoggerFromGlobal(LOG);

        DominoApp app = new DominoApp();

        app.setShowSettings(true);

        app.start();

    }



}
