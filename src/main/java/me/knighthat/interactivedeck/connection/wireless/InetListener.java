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

import me.knighthat.interactivedeck.connection.ClientInfo;
import me.knighthat.interactivedeck.ui.ConnectionStatusIndicator;
import me.knighthat.interactivedeck.utils.Status;
import me.knighthat.interactivedeck.vars.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static me.knighthat.interactivedeck.vars.Settings.*;

public class InetListener extends Thread {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger("NWRK");
    private static @Nullable ClientInfo client;
    private final @NotNull ConnectionStatusIndicator indicator;

    public InetListener( @NotNull ConnectionStatusIndicator indicator ) {
        this.indicator = indicator;
    }

    @Override
    public void run() {
        super.run();

        byte[] ip = strAddrToIp();
        String fullAddr = String.format("%s.%s.%s.%s:%s", ip[0], ip[1], ip[2], ip[3], PORT);
        boolean running = true;

        while (running) {
            try {
                InetAddress inet = InetAddress.getByAddress(ip);
                ServerSocket socket = new ServerSocket(PORT, 1, inet);
                LOGGER.info("Binding on: " + fullAddr);
                indicator.setStatus(Status.DISCONNECTED);
                this.setupConnection(socket.accept());
                socket.close();
            } catch (IOException e) {
                String msg = "Failed to open port on: " + fullAddr;
                if (e instanceof UnknownHostException)
                    msg = "Invalid IP address:" + ADDRESS;
                LOGGER.error(msg);
                indicator.setStatus(Status.ERROR);
                running = false;
            }
        }
    }

    private void setupConnection( @NotNull Socket client ) {
        String cIP = client.getInetAddress().getHostAddress();
        int cPort = client.getPort();
        String device = null, android = null;

        String inMsg = String.format("Incoming traffic from %s:%s", cIP, cPort);
        LOGGER.info(inMsg);
        LOGGER.info("Fetching data...");

        try {
            InputStream inStream = client.getInputStream();
            int bytesRead;
            while (( bytesRead = inStream.read(BUFFER) ) != -1) {
                String decoded = new String(BUFFER, 0, bytesRead);
                if (decoded.startsWith("device_model:")) {
                    String content = decoded.replace("device_model:", "");
                    device = content.trim();
                } else if (decoded.startsWith("android_version:")) {
                    String content = decoded.replace("android_version:", "");
                    android = content.trim();
                }

                if (device != null && android != null)
                    break;
            }
            if (device == null || android == null) {
                LOGGER.warn("Connection refused! Couldn't fetch information from device");
                return;
            }
        } catch (IOException e) {
            //TODO: Handle this
            String issuesTracker = "https://github.com/knighthat/interactive-deck-desktop/issues";
            LOGGER.info("Couldn't verify device. If you believe this is a bug, please report at" + issuesTracker);
            e.printStackTrace();
            return;
        }

        LOGGER.info("Device: " + device);
        LOGGER.info("Android: " + android);
        LOGGER.info("Access granted!");

        InetListener.client = new ClientInfo(cIP, cPort, device, android);

        finalizeSetup(client);
    }

    private void finalizeSetup( @NotNull Socket client ) {
        indicator.setStatus(Status.CONNECTED);
        try (InputStream inStream = client.getInputStream()) {
            int bytesRead;
            while (( bytesRead = inStream.read(BUFFER) ) != -1) {
                String data = new String(BUFFER, 0, bytesRead);
                LOGGER.info("Received: " + data);
            }
            LOGGER.info("Client closed connection!");
        } catch (IOException e) {
            //TODO: Handle THIS
            e.printStackTrace();
        }
    }

    private byte[] strAddrToIp() {
        try {
            String[] addrSplit = Settings.ADDRESS.split("\\.");
            byte[] addr = new byte[addrSplit.length];

            for (int i = 0 ; i < addr.length ; i++)
                addr[i] = Byte.parseByte(addrSplit[i]);

            return addr;
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to bind to address " + Settings.ADDRESS);
            return new byte[] {0, 0, 0, 0};
        }
    }
}
