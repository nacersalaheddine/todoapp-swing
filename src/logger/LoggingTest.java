package logger;

import consts.Resources;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingTest {

    static Logger logger = Logger.getLogger(LoggingTest.class.getName());
    
    public static void main(String[] args) {
        /*
        try {
            URL url = LoggingExample.class.getResource(Resources.LOGGER_PROPERIES_FILE);
            LogManager.getLogManager().readConfiguration(url.openStream());
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
        logger.setLevel(Level.FINE);
        logger.addHandler(new ConsoleHandler());
        */
        //adding custom handler
        logger.addHandler(new LoggerHandler());
        try {
            
            //create a folder called logs

            
            //FileHandler file name with max size and number of log files limit
            Handler logsFileHandler = new FileHandler(Resources.LOGS_FILES_PATH, 2000, 5);
            logsFileHandler.setFormatter(new LoggerFormatter());
            //setting custom filter for FileHandler
            logsFileHandler.setFilter(new LoggerFilter());
            logger.addHandler(logsFileHandler);
            
            for(int i=0; i<1000; i++){
                //logging messages
                logger.log(Level.INFO, "Msg"+i);
            }
            logger.log(Level.CONFIG, "Config data");
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

    }

}