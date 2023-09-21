/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck.logging;

import me.knighthat.interactivedeck.utils.UuidUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Log {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger( "MAIN" );
    }

    public static void log( @NotNull LogLevel level, @NotNull String s ) {
        switch (level) {
            case DEBUG -> LOGGER.debug( s );
            case INFO -> LOGGER.info( s );
            case WARNING -> LOGGER.warn( s );
            case ERROR -> LOGGER.error( s );
        }
    }

    public static void deb( @NotNull String s ) {
        log( LogLevel.DEBUG, s );
    }

    public static void info( @NotNull String s ) {
        log( LogLevel.INFO, s );
    }

    public static void warn( @NotNull String s ) {
        log( LogLevel.WARNING, s );
    }

    public static void err( @NotNull String s ) {
        log( LogLevel.ERROR, s );
    }

    /**
     * A shorthand to print error to console or file.<br>
     *
     * @param s Custom/Additional message
     * @param e Exception
     * @param b Print stack trace
     */
    public static void exc( @NotNull String s, @NotNull Exception e, boolean b ) {
        if (!s.isBlank())
            err( s );
        err( "Reason: " + e.getMessage() );
        if (b)
            e.printStackTrace();
    }

    /**
     * Prints out exception but at warning levell
     *
     * @param s Custom/Additional message
     * @param e Exception
     * @param b Print stack trace
     */
    public static void wexc( @NotNull String s, @NotNull Exception e, boolean b ) {
        if (!s.isBlank())
            warn( s );
        err( "Reason: " + e.getMessage() );
        if (b)
            e.printStackTrace();
    }

    private static void update( @NotNull String object, @NotNull String id, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        String info = "%s %s changed %s from \"%s\" to \"%s\"";
        info = info.formatted( object, id, property, from, to );
        info( info );
    }

    public static void buttonUpdate( @NotNull UUID uuid, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        update( "Button", UuidUtils.lastFiveChars( uuid ), property, from, to );
    }

    public static void profileUpdate( @NotNull String displayName, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        update( "Profile", displayName, property, from, to );
    }

    public enum LogLevel {
        DEBUG, INFO, WARNING, ERROR
    }
}
