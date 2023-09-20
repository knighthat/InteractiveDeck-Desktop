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

package me.knighthat.interactivedeck.component.netstatus;

import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.icon.Icons;
import me.knighthat.interactivedeck.connection.Connection;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.apache.batik.swing.JSVGCanvas;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

final class NetIcon extends JSVGCanvas implements Flexible {

    public NetIcon() {
        setBackground( ColorUtils.TRANSPARENT );
        setDimension( this, 20, 20 );
        setRecenterOnResize( false );
    }

    public void setDocument( @NotNull Connection.Status status ) {
        SVGDocument iconDoc = switch (status) {
            case DISCONNECTED -> Icons.INTERNAL.CONNECTION_DISCONNECTED;
            case CONNECTED -> Icons.INTERNAL.CONNECTION_CONNECTED;
            case ERROR -> Icons.INTERNAL.CONNECTION_ERROR;
            case UNKNOWN -> Icons.INTERNAL.CONNECTION_UNKNOWN;
        };

        setDocument( iconDoc );
        revalidate();
        repaint();
    }
}
