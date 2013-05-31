package org.ssrad.spacebit.helpers;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;

public class LogHelper {

    /**
     * Log file location
     */
    private static String LOG_FILE_LOCATION = "game.log";

    private static final Logger logger = Logger.getLogger(LogHelper.class);

    /**
     * Log message format
     */
    private static PatternLayout layout = new PatternLayout("Time: %d{DATE} - %m%n"); // - Class: %c: %m%n");

    /**
     * Returns the logger singleton.
     *
     * @return {@link #logger instance}.
     */
    public static Logger getLogger() {
        if (!logger.getAllAppenders().hasMoreElements()) {
            ConsoleAppender consoleAppender = new ConsoleAppender(layout);
            logger.addAppender(consoleAppender);

            FileAppender fileAppender;
            try {
                fileAppender = new FileAppender(layout, LOG_FILE_LOCATION, true);
                logger.addAppender(fileAppender);
            } catch (IOException e) {
                logger.error(e.getCause());
            }
        }
        return logger;
    }

}
