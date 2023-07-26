package me.knighthat.interactivedeck.console;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger("MAIN");
    }

    public static void info( @NotNull String s ) {
        LOGGER.info(s);
    }

    public static void warn( @NotNull String s ) {
        LOGGER.warn(s);
    }

    public static void err( @NotNull String s ) {
        LOGGER.error(s);
    }
}
