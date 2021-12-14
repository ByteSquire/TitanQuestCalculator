package de.bytesquire.titanquest.tqcalculator.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.logging.*;

public class Util {

    public static class LogLevel {
        public static final Level ERROR = Level.SEVERE;
        public static final Level WARNING = Level.WARNING;
        public static final Level DEBUG = Level.FINER;
        public static final Level INFO = Level.INFO;
        public static final Level TRACE = Level.FINEST;
    }

    public static final Logger GLOBAL_LOGGER = Logger.getLogger("de.bytesquire.titanquest.tqcalculator");

    public static void init() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s]%n\t%5$s %6$s%n%n");
        Locale.setDefault(Locale.UK);

        final ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(LogLevel.ERROR);
        consoleHandler.setFormatter(new SimpleFormatter());

        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("tqCalculator.log");
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());

            GLOBAL_LOGGER.setLevel(Level.ALL);
            for (Handler handler : GLOBAL_LOGGER.getHandlers())
                GLOBAL_LOGGER.removeHandler(handler);
            GLOBAL_LOGGER.setUseParentHandlers(false);

            GLOBAL_LOGGER.addHandler(consoleHandler);
            GLOBAL_LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLoggerForClass(Class<?> aClass) {
        return Logger.getLogger(aClass.getName());
    }

    private static void log(Logger aLogger, Level aLogLevel, Exception aException) {
        if (aException == null || aLogger == null)
            return;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        aException.printStackTrace(printWriter);
        aLogger.log(LogLevel.TRACE, stringWriter.toString());
        aLogger.log(aLogLevel, aException.getMessage());
    }

    private static void log(Logger aLogger, Level aLogLevel, String aMessage) {
        aLogger.log(aLogLevel, aMessage);
    }

    public static void logError(Logger aLogger, Exception aException) {
        log(aLogger, LogLevel.ERROR, aException);
    }

    public static void logWarning(Logger aLogger, Exception aException) {
        log(aLogger, LogLevel.WARNING, aException);
    }

    public static void logDebug(Logger aLogger, Exception aException) {
        log(aLogger, LogLevel.DEBUG, aException);
    }

    public static void logInfo(Logger aLogger, Exception aException) {
        log(aLogger, LogLevel.INFO, aException);
    }

    public static void logError(Logger aLogger, String aMessage) {
        log(aLogger, LogLevel.ERROR, aMessage);
    }

    public static void logWarning(Logger aLogger, String aMessage) {
        log(aLogger, LogLevel.WARNING, aMessage);
    }

    public static void logDebug(Logger aLogger, String aMessage) {
        log(aLogger, LogLevel.DEBUG, aMessage);
    }

    public static void logInfo(Logger aLogger, String aMessage) {
        log(aLogger, LogLevel.INFO, aMessage);
    }
}
