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

import java.util.UUID;

public interface Log {

    @NotNull LogImpl LOG = new LogImpl();

    static void start() {LOG.start();}

    static void stop() {LOG.stopRunning();}

    static void log( @NotNull LogLevel level, @NotNull String s ) {LOG.log( level, s, false );}

    static void deb( @NotNull String s ) {log( LogLevel.DEBUG, s );}

    static void info( @NotNull String s ) {log( LogLevel.INFO, s );}

    static void warn( @NotNull String s ) {warn( s, false );}

    static void warn( @NotNull String s, boolean skipQueue ) {LOG.log( LogLevel.WARNING, s, skipQueue );}

    static void err( @NotNull String s ) {err( s, false );}

    static void err( @NotNull String s, boolean skipQueue ) {LOG.log( LogLevel.ERROR, s, skipQueue );}

    /**
     * A shorthand to print error to console or file.<br>
     *
     * @param s               Custom/Additional message
     * @param t               Throwable error
     * @param printStackTrace Print stack trace
     */
    static void exc( @NotNull String s, @NotNull Throwable t, boolean printStackTrace ) {
        if (!s.isBlank())
            err( s );
        if (t.getMessage() != null)
            err( "Reason: " + t.getMessage(), true );
        if (t.getCause() != null)
            err( "Cause: " + t.getCause().getMessage(), true );
        if (printStackTrace)
            t.printStackTrace();
    }

    /**
     * Prints out exception but at warning level
     *
     * @param s               Custom/Additional message
     * @param t               Throwable error
     * @param printStackTrace Print stack trace
     */
    static void wexc( @NotNull String s, @NotNull Throwable t, boolean printStackTrace ) {
        if (!s.isBlank())
            warn( s );
        if (t.getMessage() != null)
            warn( "Reason: " + t.getMessage(), true );
        if (t.getCause() != null)
            warn( "Cause: " + t.getCause().getMessage(), true );
        if (printStackTrace)
            t.printStackTrace();
    }

    static void reportBug() {
        err( "Unexpected error occurs, please report this to:", true );
        err( "https://github.com/knighthat/InteractiveDeck-Desktop/issues", true );
    }

    private static void update( @NotNull String object, @NotNull String id, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        String info = "%s %s changed %s from \"%s\" to \"%s\"";
        info = info.formatted( object, id, property, from, to );
        info( info );
    }

    static void buttonUpdate( @NotNull UUID uuid, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        update( "Button", UuidUtils.lastFiveChars( uuid ), property, from, to );
    }

    static void profileUpdate( @NotNull String displayName, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        update( "Profile", displayName, property, from, to );
    }

    enum LogLevel {
        DEBUG, INFO, WARNING, ERROR
    }
}
