/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck.console;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger( "MAIN" );
    }

    public static void log( @NotNull LogLevel level, @NotNull String s ) {
        switch ( level ) {
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

    public enum LogLevel {
        DEBUG, INFO, WARNING, ERROR
    }
}
