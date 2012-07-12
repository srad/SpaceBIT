package org.ssrad.spacebit.helpers;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.ssrad.spacebit.main.SpaceBit;

public class LogHelper {
	
	/** Log file location */
	private static String LOG_FILE = "game.log";

	private static final Logger logger = Logger.getLogger(LogHelper.class);

	/** Log message format */
	private static PatternLayout layout = new PatternLayout("Time: %d{DATE} - %m%n"); // - Class: %c: %m%n");

	/**
	 * Returns the logger singleton.
	 * @return {@link #logger instance}.
	 */
	public static Logger getLogger() {
		if (!logger.getAllAppenders().hasMoreElements()) {
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);

			FileAppender fileAppender;
			try {
				String logFile = SpaceBit.getInstance().getSettingsHelper().getGameDir() + "/" + LOG_FILE;
				fileAppender = new FileAppender(layout, logFile, true);
				logger.addAppender(fileAppender);
			} catch (IOException e) {
				logger.error(e.getCause());
			}
		}
		return logger;
	}

}
