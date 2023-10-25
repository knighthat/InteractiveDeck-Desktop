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

import me.knighthat.interactivedeck.connection.Client;
import me.knighthat.interactivedeck.menus.NotificationCenter;
import me.knighthat.lib.connection.Connection;
import me.knighthat.lib.connection.wireless.WirelessReceiver;
import me.knighthat.lib.connection.wireless.WirelessSender;
import me.knighthat.lib.exception.RequestException;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static me.knighthat.interactivedeck.settings.Settings.SETTINGS;
import static me.knighthat.lib.connection.Connection.Status;

public class WirelessController extends Thread {

    public WirelessController() {
        setName( "NET" );
    }

    void handleConnection( @NotNull Socket client ) throws IOException {
        String cInfo = client.getInetAddress().getHostAddress() + ":" + client.getPort();
        Log.info( "Connection from " + cInfo );

        // Init Sender & Receiver
        WirelessSender sender = new WirelessSender( client.getOutputStream() );
        sender.start();

        initReceiver( client.getInputStream() );

        handleDisconnection( sender, client );
    }

    void handleDisconnection( @NotNull WirelessSender sender, @NotNull Socket client ) throws IOException {
        Client.INSTANCE = null;
        Connection.setStatus( Status.DISCONNECTED );

        sender.interrupt();
        client.close();
        setName( "NET" );

        Log.info( "Client disconnected!" );
    }

    private void initReceiver( @NotNull InputStream inStream ) {
        try {
            new WirelessReceiver(
                    inStream,
                    SETTINGS.buffer(),
                    new RequestHandler()
            ).run();
        } catch (RequestException e) {
            Log.exc( "Error occurs while processing request!", e, false );
            Log.reportBug();
        }
    }

    @Override
    public void run() {
        InetAddress IP = WirelessAddress.get();
        if (IP == null) {
            Connection.setStatus( Status.ERROR );
            interrupt();
            return;
        } else
            Connection.setStatus( Status.DISCONNECTED );

        while (!Thread.interrupted())
            try (ServerSocket socket = new ServerSocket( SETTINGS.getPort(), 1, IP )) {
                String message = "Listening on: " + SETTINGS.addressWithPort();
                Log.info( message );
                NotificationCenter.setConstantMessage( message );

                handleConnection( socket.accept() );

            } catch (IOException e) {
                setName( "NET" );
                //TODO Implement proper handler
                Log.exc( "Could not start listening on " + SETTINGS.addressWithPort(), e, true );

                Connection.setStatus( Status.ERROR );
                interrupt();
            }
    }
}
