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

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class WirelessAddress {

    private static final @NotNull String DEF_IP = "0.0.0.0";

    static @Nullable InetAddress get() {
        if (SETTINGS.address().equals( DEF_IP ))
            try {
                return InetAddress.getByAddress( SETTINGS.addressInBytes() );
            } catch (UnknownHostException e) {
                return null;
            }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nif = interfaces.nextElement();
                Enumeration<InetAddress> iAddresses = nif.getInetAddresses();
                while (iAddresses.hasMoreElements()) {
                    InetAddress address = iAddresses.nextElement();
                    if (address.getHostAddress().equals( SETTINGS.address() ))
                        return address;
                }
            }
            Log.warn( "There is no interface has address " + SETTINGS.address() );
            Log.warn( "Reversing address to " + DEF_IP );
            SETTINGS.address( DEF_IP );
            return get();
        } catch (SocketException e) {
            Log.exc( "", e, false );
            return null;
        }
    }
}
