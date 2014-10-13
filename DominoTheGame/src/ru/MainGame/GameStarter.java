package ru.MainGame;

import com.jme3.network.serializing.Serializer;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StatusPlayer;
import ru.MainGame.Network.StepToSend;

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

        AppSettings cfg = new AppSettings(true);
        cfg.setFrameRate(60); // set to less than or equal screen refresh rate
        cfg.setVSync(true);   // prevents page tearing
        cfg.setFrequency(60); // set to screen refresh rate
        cfg.setResolution(1024, 768);
        cfg.setFullscreen(false);
        cfg.setSamples(2);    // anti-aliasing
        cfg.setTitle("GOAT"); // branding: window name
//        try {
//          // Branding: window icon
//          cfg.setIcons(new BufferedImage[]{ImageIO.read(new File("assets/Interface/icon.gif"))});
//        } catch (IOException ex) {
//            Logger.getLogger(GameStarter.class.getName()).log(Level.SEVERE, "Icon missing.", ex);
//        }
        // branding: load splashscreen from assets
//        cfg.setSettingsDialogImage("Interface/MySplashscreen.png");
        app.setShowSettings(false); // or don't display splashscreen
        app.setSettings(cfg);
	LOG.log(Level.CONFIG, "The game try start with configs : \n{0}", cfg);
        app.start();

    }



}
