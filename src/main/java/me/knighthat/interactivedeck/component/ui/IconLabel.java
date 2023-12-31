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

package me.knighthat.interactivedeck.component.ui;

import me.knighthat.interactivedeck.svg.SVGParser;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class IconLabel extends UILabel {

    public IconLabel( @NotNull BufferedImage bufferedImage,
                      @MagicConstant( valuesFromClass = SwingConstants.class ) int horizontalAlignment,
                      @MagicConstant( valuesFromClass = SwingConstants.class ) int verticalAlignment ) {
        super( new ImageIcon( bufferedImage ) );
        setForeground( ColorUtils.TRANSPARENT );
        setHorizontalAlignment( horizontalAlignment );
        setVerticalAlignment( verticalAlignment );
    }

    public IconLabel( @NotNull SVGDocument document,
                      int width, int height,
                      @MagicConstant( valuesFromClass = SwingConstants.class ) int horizontalAlignment,
                      @MagicConstant( valuesFromClass = SwingConstants.class ) int verticalAlignment ) {
        SVGSVGElement root = document.getRootElement();
        root.setAttribute( "width", String.valueOf( width ) );
        root.setAttribute( "height", String.valueOf( height ) );
        BufferedImage image = SVGParser.toBufferedImage( document );
        setIcon( new ImageIcon( image ) );
        setHorizontalAlignment( horizontalAlignment );
        setVerticalAlignment( verticalAlignment );
    }
}
