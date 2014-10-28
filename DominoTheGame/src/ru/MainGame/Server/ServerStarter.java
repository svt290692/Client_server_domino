package ru.MainGame.Server;

import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.MainGame.GlobalLogConfig;
import ru.MainGame.Network.FromBothSides.ExtendedSpecificationMessage;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.StepToSend;

/**
 * test
 * @author normenhansen
 */
public class ServerStarter{

    public final static String logDirName = "ServerLog";
    public final static String serverName = "Server";

    private static final Logger LOG = Logger.getLogger(ServerStarter.class.getName());

    static{
        Serializer.registerClasses(ExtendedSpecificationMessage.class,
                StartGameMessage.class,NumsOfDice.class, StepToSend.class);
    }

    public ServerStarter() {
        GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

    private enum Args{
	LOG_LEVEL("loglevel"),
	PORT("port");

	private Args(String val) {
	    this.val = val;
	}

	String val;
    }

    public static void main(String[] args) {

	initParam(args);
	initServersLog();
	Server server = null;

	int port = 5511;

	try {
	    server = Network.createServer(port ,-1);
	} catch (IOException ex) {
	    LOG.log(Level.SEVERE, "Server can't start", ex);
	    return;
	}
	ServerHandler handler = new ServerHandler(server);

	server.addConnectionListener(handler);
	server.addMessageListener(handler);

	LOG.fine("server get listeners");

	server.start();

	try {
	    Thread.sleep(100);
	} catch (InterruptedException ex) {
	    System.err.println("Main thread was interrupt exception = "  + ex);
	}

	if(server.isRunning())
	LOG.log(Level.INFO,"Server start at port:{0}" , new Object[]{port});

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void initParam(String[] args){
	if(args.length > 1 ){
	    String[] map;
	    /**
	     * init args
	     */
	    for(int i = 1; i < args.length;i++){
		map = args[i].split("=");

		if(map[0].equals(Args.LOG_LEVEL.val)){
		    Level level = Level.parse(map[1]);
		    if(level != null){
			GlobalLogConfig.initGlobalLogging(level,logDirName);
		    }
		}
	    }
	}
    }
    private static void initServersLog(){

	if(!GlobalLogConfig.isInit()){
	    GlobalLogConfig.initGlobalLogging(Level.ALL,logDirName);
	}
	GlobalLogConfig.initLoggerFromGlobal(LOG);
    }

}
