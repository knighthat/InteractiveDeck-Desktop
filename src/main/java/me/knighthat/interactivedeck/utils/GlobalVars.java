package me.knighthat.interactivedeck.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GlobalVars {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try (InputStream is = GlobalVars.class.getClassLoader().getResourceAsStream("global.properties")) {
            PROPERTIES.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static @NotNull String get( @NotNull String var ) {
        return PROPERTIES.getProperty(var);
    }

    public static @NotNull String name() {
        return get("APPLICATION_NAME");
    }

    public static @NotNull String version() {
        return get("APPLICATION_VERSION");
    }
}
