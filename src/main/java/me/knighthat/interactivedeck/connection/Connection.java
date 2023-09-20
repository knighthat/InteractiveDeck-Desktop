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
package me.knighthat.interactivedeck.connection;

import me.knighthat.interactivedeck.component.netstatus.NetStatus;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Connection {

    private static @NotNull Status status = Status.UNKNOWN;

    public static void status( @NotNull Status status ) {
        Connection.status = status;
        NetStatus.updateStatus( status );
    }

    public static boolean isConnected() {
        return status.equals( Status.CONNECTED );
    }

    public enum Status {
        DISCONNECTED( "#6c757d", "Disconnected" ),
        CONNECTED( "#70e000", "Connected" ),
        ERROR( "#ff0000", "ERROR" ),
        UNKNOWN( "#fb8500", "Unknown" );

        private final String hexColor;
        private final String label;

        Status( String hexColor, String label ) {
            this.hexColor = hexColor;
            this.label = label;
        }

        public final Color color() {
            return ColorUtils.fromHex( this.hexColor );
        }

        public final String label() {
            return this.label;
        }
    }
}
