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

import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static me.knighthat.interactivedeck.file.Settings.ADDRESS;

public class WirelessAddress {

    private static final @NotNull String DEF_IP = "0.0.0.0";

    static @Nullable InetAddress get() {
        if (ADDRESS.equals( DEF_IP ))
            try {
                return InetAddress.getByAddress( addressToByteArray() );
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
                    if (address.getHostAddress().equals( ADDRESS ))
                        return address;
                }
            }
            Log.warn( "There is no interface has address " + ADDRESS );
            Log.warn( "Reversing address to " + DEF_IP );
            ADDRESS = DEF_IP;
            return get();
        } catch (SocketException e) {
            Log.err( "No functional interface found!" );
            return null;
        }
    }

    static byte[] addressToByteArray() {
        byte[] address = new byte[4];
        String[] splitAddr = ADDRESS.split( "\\." );
        for (int i = 0 ; i < 4 ; i++)
            address[i] = Byte.parseByte( splitAddr[i] );

        String debStr = "Address in bytes array: [%s,%s,%s,%s]";
        Log.deb( String.format( debStr, address[0], address[1], address[2], address[3] ) );

        return address;
    }
}
