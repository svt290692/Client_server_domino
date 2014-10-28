/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * some config for logging my programm
 * @author svt
 */
public class GlobalLogConfig {

    private static boolean wasInit = false;
    private static Handler handler;

    private static final String logDirStdName = "log";
/**
 * this static method need to first initial logging system of the game
 *
 * @param globalLevel the level of log global system that must initial
 * all of log that will be call static method "initLoggerFromGlobal
 * if globalLevel parameter will be null, globalLevel substitute Level.ALL
 */
    public static void initGlobalLogging(Level globalLevel,String nameOfLogFolder){
	if(wasInit == true)
	    return;

        if(nameOfLogFolder == null){
            nameOfLogFolder = logDirStdName;
        }
        initLogDir(nameOfLogFolder);

	Logger gLogger = Logger.getGlobal();
	if(globalLevel == null)
	    gLogger.setLevel(Level.ALL);
	else
	    gLogger.setLevel(globalLevel);

	try {
	    try{
		long curTime = System.currentTimeMillis();
		String time = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss__").format(curTime);

		handler = new FileHandler(nameOfLogFolder + File.separator + time + "GLOBAL_LOG " + ".xml",false);
	    }catch(IOException | SecurityException ex){
		Logger.getGlobal().log(Level.WARNING, "Logger can not Create file Handler From Current time", ex);
		handler = new FileHandler(nameOfLogFolder + File.separator + "GLOBAL_LOG.log");
	    }

	    handler.setFormatter(new XMLFormatter());
	    gLogger.addHandler(handler);

	}catch (IOException ex) {
	    Logger.getGlobal().log(Level.WARNING, "Logger can not Create file Handler ", ex);
	} catch (SecurityException ex) {
	    Logger.getGlobal().log(Level.WARNING, null, ex);
	}
	wasInit = true;
    }
    public static boolean isInit(){
        return wasInit;
    }
/**
 * this method need to initialize logDirectory that will contain all log of my programm
 * if some error will resive it will push in standart log
 * @param logDirName name of logging directory
 */
    public static boolean initLogDir(String logDirName){

        File logDir = new File(logDirName);
        if(logDir.exists()){
            if(!logDir.isDirectory()){
                Logger.getGlobal().log(Level.WARNING,
                        "The name of dir is already in use and"
                        + " this in not directiry. please"
                        + " remove, replace or rename file that has name :"
                        + logDirName);
                System.err.println("Error Log Config dir cant create");
                return false;
            }
        }
        else{
            if(!logDir.mkdir()){
                Logger.getGlobal().log(Level.WARNING,"cant create dir: "
                        + logDirName);
                System.err.println("folder of name" + logDirName
                        + "cant create");
                return false;
            }
        }
        return true;
    }

    public static Handler getGlobalLogHandler(){
	return handler;
    }
/**
 *
 * @param log - the log was entered will be initial all global handlers
 * and Level of this log will be like as Global log
 */
    public static void initLoggerFromGlobal(Logger log){
	initialLoggerHandlers(log);
	setLogLevelFromGlobal(log);
    }

    public static void initialLoggerHandlers(Logger log){

	for(Handler h : Logger.getGlobal().getHandlers()){
	    log.addHandler(h);
	}
    }

    public static void setLogLevelFromGlobal(Logger log){
	log.setLevel(Logger.getGlobal().getLevel());
    }
}
