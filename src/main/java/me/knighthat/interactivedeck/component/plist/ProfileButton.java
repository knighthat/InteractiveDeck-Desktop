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

package me.knighthat.interactivedeck.component.plist;

import me.knighthat.interactivedeck.component.icon.Icons;
import org.apache.batik.swing.JSVGCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.svg.SVGDocument;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static me.knighthat.interactivedeck.utils.ColorUtils.TRANSPARENT;

public class ProfileButton extends JSVGCanvas {

    private final @Nullable SVGDocument ICON;
    private final @Nullable SVGDocument ICON_HOVER;

    public ProfileButton( @NotNull ButtonType type ) {
        ICON = switch (type) {
            case ADD -> Icons.INTERNAL.PROFILE_ADD;
            case REMOVE -> Icons.INTERNAL.PROFILE_REMOVE;
            case CONFIGURE -> Icons.INTERNAL.PROFILE_CONFIGURE;
        };
        ICON_HOVER = switch (type) {
            case ADD -> Icons.INTERNAL.PROFILE_ADD_HOVER;
            case REMOVE -> Icons.INTERNAL.PROFILE_REMOVE_HOVER;
            case CONFIGURE -> Icons.INTERNAL.PROFILE_CONFIGURE_HOVER;
        };

        setBackground( TRANSPARENT );
        setDocument( ICON );
        setRecenterOnResize( false );

        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered( MouseEvent e ) {
                setDocument( ICON_HOVER );
            }

            @Override
            public void mouseExited( MouseEvent e ) {
                setDocument( ICON );
            }
        } );
    }

    public enum ButtonType {
        ADD, REMOVE, CONFIGURE
    }
}
