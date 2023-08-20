/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.connection.wireless;

import me.knighthat.interactivedeck.InteractiveDeck;
import me.knighthat.interactivedeck.connection.request.Request;
import me.knighthat.interactivedeck.console.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WirelessSender extends Thread {

    private static final @NotNull BlockingQueue<Request> QUEUE = new LinkedBlockingQueue<>( 20 );

    private static @NotNull OutputStream stream;

    WirelessSender() {
        setName( "NET/O" );
    }

    public static void start( @NotNull OutputStream stream ) {
        QUEUE.clear();
        WirelessSender.stream = stream;
        new WirelessSender().start();
    }

    public static void send( @NotNull Request request ) {
        try {
            QUEUE.put( request );
        } catch ( InterruptedException e ) {
            Log.warn( "Thread was interrupted while send request is pending!" );
            Log.warn( "Caused by: " + e.getMessage() );
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Request request;
        while ( !Thread.interrupted() )
            try {
                request = QUEUE.take();
                String serialized = request.serialize();

                Log.deb( "Sending:" );
                Log.deb( serialized );

                stream.write( serialized.getBytes() );
                stream.flush();

            } catch ( InterruptedException e ) {
                //TODO Needs proper error handling
                if ( InteractiveDeck.client == null )
                    return;

                Log.err( "Thread interrupted!" );
                e.printStackTrace();
                break;
            } catch ( IOException e ) {
                //TODO Needs proper error handling
                Log.err( "Error occurs while sending out request" );
                e.printStackTrace();
                break;
            }
    }
}
