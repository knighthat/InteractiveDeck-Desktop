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
import me.knighthat.interactivedeck.connection.request.Request;
import me.knighthat.interactivedeck.connection.request.RequestHandler;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.menus.NotificationCenter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class WirelessController extends Thread {

    public WirelessController() {
        setName( "NET" );
    }

    @Override
    public void run() {
        InetAddress IP = WirelessAddress.get();
        if (IP == null) {
            Connection.status( Connection.Status.ERROR );
            interrupt();
            return;
        } else
            Connection.status( Connection.Status.DISCONNECTED );

        while (!Thread.interrupted())
            try (ServerSocket socket = new ServerSocket( SETTINGS.port(), 1, IP )) {
                String message = "Listening on: " + SETTINGS.fullAddress();
                Log.info( message );
                NotificationCenter.setConstantMessage( message );

                handleConnection( socket.accept() );

                Log.info( "Client disconnected!" );
            } catch (IOException e) {
                setName( "NET" );
                //TODO Implement proper handler
                Log.exc( "Could not start listening on " + SETTINGS.fullAddress(), e, true );

                Connection.status( Connection.Status.ERROR );
                interrupt();
            }
    }

    void handleConnection( @NotNull Socket client ) throws IOException {
        String cInfo = client.getInetAddress().getHostAddress() + ":" + client.getPort();
        Log.info( "Connection from " + cInfo );

        // Init Sender & Receiver
        WirelessSender sender = new WirelessSender( client.getOutputStream() );
        sender.start();
        setupReceiver( client.getInputStream() );

        handleDisconnection( sender, client );
    }

    void handleDisconnection( @NotNull WirelessSender sender, @NotNull Socket client ) throws IOException {
        Client.INSTANCE = null;
        Connection.status( Connection.Status.DISCONNECTED );

        sender.interrupt();
        client.close();
        setName( "NET" );
    }

    void setupReceiver( @NotNull InputStream inStream ) throws IOException {
        setName( "NET/I" );

        byte[] buffer = SETTINGS.buffer();
        int bytesRead;
        String finalStr = "";
        while (( bytesRead = inStream.read( buffer ) ) != -1) {
            String decoded = new String( buffer, 0, bytesRead );

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
