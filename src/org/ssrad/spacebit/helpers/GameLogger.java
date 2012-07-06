package org.ssrad.spacebit.helpers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger {
	
	private static final String LOG_FILE_NAME = "game.log";
	private static GameLogger logger = null;
	
	public static void Log(Level level, String msg) {
//		if (logger == null) {
//			try {
//			    FileHandler handler = new FileHandler(LOG_FILE_NAME);
//			    handler.setFormatter(new SimpleFormatter());
//
//			    Logger logger = Logger.getLogger("org.ssrad");
//			    logger.addHandler(handler);
//			} catch (IOException e) {
//			}
//		} else {
//			GameLogger.Log(level, msg);
//		}
	}

}
