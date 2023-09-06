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
package me.knighthat.interactivedeck.connection.wireless;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.connection.Client;
import me.knighthat.interactivedeck.connection.Connection;
import me.knighthat.interactivedeck.connection.Status;
import me.knighthat.interactivedeck.connection.request.Request;
import me.knighthat.interactivedeck.connection.request.RequestHandler;
import me.knighthat.interactivedeck.console.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static me.knighthat.interactivedeck.file.Settings.*;

public class WirelessController extends Thread {

    public WirelessController() {
        setName( "NET" );
    }

    @Override
    public void run() {
        InetAddress IP = WirelessAddress.get();
        if (IP == null) {
            Connection.status( Status.ERROR );
            interrupt();
        }

        while (!Thread.interrupted())
            try (ServerSocket socket = new ServerSocket( PORT, 1, IP )) {
                resetConnection();

                this.handleConnection( socket.accept() );

                Log.info( "Client disconnected!" );
            } catch (IOException e) {
                setName( "NET" );
                //TODO Implement proper handler

                Log.err( "Could not start listening on " + address() );
                Log.err( e.getMessage() );

                Connection.status( Status.ERROR );
                interrupt();
            }
    }

    void resetConnection() {
        setName( "NET" );

        Connection.status( Status.DISCONNECTED );

        Client.INSTANCE = null;

        Log.info( "Listening on: " + address() );
    }

    void handleConnection( @NotNull Socket client ) throws IOException {
        String cInfo = client.getInetAddress().getHostAddress() + ":" + client.getPort();
        Log.info( "Connection from " + cInfo );

        WirelessSender.start( client.getOutputStream() );
        setupReceiver( client.getInputStream() );
    }

    void setupReceiver( @NotNull InputStream inStream ) throws IOException {
        setName( "NET/I" );

        int bytesRead;
        String finalStr = "";
        while (( bytesRead = inStream.read( BUFFER ) ) != 1) {
            String decoded;
            try {
                decoded = new String( BUFFER, 0, bytesRead );
            } catch (StringIndexOutOfBoundsException e) {
                break;
            }
            Log.deb( "Received: " );
            Log.deb( decoded );

            finalStr = finalStr.concat( decoded );

            try {
                JsonObject json = JsonParser.parseString( finalStr ).getAsJsonObject();
                Request request = Request.parse( json );
                RequestHandler.process( request );
                finalStr = "";

            } catch (JsonParseException ignored) {
            }
        }
    }
}
