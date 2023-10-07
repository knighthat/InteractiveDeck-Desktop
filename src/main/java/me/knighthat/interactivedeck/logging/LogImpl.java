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

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static me.knighthat.interactivedeck.logging.Log.exc;

final class LogImpl extends Thread {

    private static final @NotNull LinkedBlockingQueue<LogRecord> LOGS;

    private static final @NotNull LogRecord TERMINATOR;

    static {
        LOGS = new LinkedBlockingQueue<>( 5 );
        TERMINATOR = new LogRecord( () -> {} );
    }

    private Logger logger;

    LogImpl() {}

    private boolean skipQueue() {return System.getProperties().containsKey( "log.skipQueue" ) && Boolean.getBoolean( "log.skipQueue" );}

    private void sysout( @NotNull Log.LogLevel level, @NotNull String s ) {
        String format = "[%s] %s".formatted( level.name(), s );
        if (level.equals( Log.LogLevel.ERROR ))
            System.out.println( format );
        else
            System.out.println( format );
    }

    public synchronized void log( @NotNull Log.LogLevel level, @NotNull String s, boolean skipQueue ) {
        if (System.getProperties().containsKey( "log.enabled" ) && !Boolean.getBoolean( "log.enabled" )) {
            sysout( level, s );
            return;
        }

        Runnable task = () -> {
            switch (level) {
                case DEBUG -> logger.debug( s );
                case INFO -> logger.info( s );
                case WARNING -> logger.warn( s );
                case ERROR -> logger.error( s );
            }
        };

        if (skipQueue || skipQueue())
            task.run();
        else
            try {
                long seconds = 5L;
                TimeUnit timeUnit = TimeUnit.SECONDS;
                LogRecord record = new LogRecord( task );
                LOGS.offer( record, seconds, timeUnit );
            } catch (InterruptedException e) {
                Log.exc( "Timed out while wait to log", e, true );
                Log.reportBug();
            }
    }

    public void stopRunning() {LOGS.add( TERMINATOR );}

    @Override
    public void run() {
        this.logger = LoggerFactory.getLogger( "LOGGER" );

        while (true) {
            try {
                LogRecord log = LOGS.take();
                if (log == TERMINATOR)
                    break;
                Thread.currentThread().setName( log.threadName );
                log.task.run();
            } catch (InterruptedException e) {
                exc( "Logs queue crashed!", e, true );
            }
        }
    }

    private static class LogRecord {
        private final @NotNull String threadName;
        private final @NotNull Runnable task;

        private LogRecord( @NotNull Runnable task ) {
            this.threadName = Thread.currentThread().getName();
            this.task = task;
        }
    }
}