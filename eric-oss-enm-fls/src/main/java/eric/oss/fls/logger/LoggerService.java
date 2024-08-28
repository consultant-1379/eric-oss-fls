package eric.oss.fls.logger;

import java.io.File;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.stereotype.Service;

@Service
public class LoggerService {

	 /** The logger. */
    private static final Logger LOGGER = Logger.getLogger("com.eric.oss.fls");
    
    /**
     * Intialize logger.
     *
     * @param appPath
     *            the app path
     */
    public static void intializeLogger(String appPath){
        try{
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.FINE);
            // Creating fileHandler
            final FileHandler fileHandler = new FileHandler(appPath + File.separator + "logs/eric-oss-enm-fls.log");
            fileHandler.setLevel(Level.FINE);
            // Creating and assigning SimpleFormatter
            Formatter simpleFormatter = new SimpleFormatter() {
                private static final String format = "[%1$tb %1$td,%1$tY %1$tT] %2$-7s: %3$s %n";
                @Override
                public synchronized String format(LogRecord lr){
                    return String.format(format,new Date(lr.getMillis()),lr.getLevel().getLocalizedName(),lr.getMessage());
                }
            };
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);

        } catch(Exception e){
        	e.printStackTrace();
            System.out.println("Exception occurred while initializing logger. " + e.getMessage());
        }
    }

    /**
     * Gets the logger.
     *
     * @return the logger
     */
    public static Logger getLOGGER(){
        return LOGGER;
    }
}
