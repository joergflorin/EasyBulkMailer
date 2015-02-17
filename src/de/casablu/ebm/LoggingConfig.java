/*
 * Created on 17.02.2015 by joerg
 */
package de.casablu.ebm;

import java.io.IOException;
import java.util.logging.LogManager;

public class LoggingConfig {
    public LoggingConfig() throws SecurityException, IOException {
        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration(LoggingConfig.class
                .getResourceAsStream("logging.properties"));
    }
}
