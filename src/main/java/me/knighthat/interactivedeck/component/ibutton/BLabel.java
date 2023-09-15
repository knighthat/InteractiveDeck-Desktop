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

package me.knighthat.interactivedeck.component.ibutton;

import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

final class BLabel extends BChild {

    @NotNull
    String text = "";

    BLabel() {
        super();
        setForeground( Color.WHITE );

        setFont( new Font( "Stardos Stencil", Font.PLAIN, 14 ) );
    }

    public void text( @NotNull String text ) {
        this.text = text;
        repaint();
    }

    public @NotNull String text() {
        return this.text;
    }

    @Override
    protected void paintComponent( Graphics g ) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor( getForeground() );
        g2d.setFont( getFont() );

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int width = fontMetrics.stringWidth( text );
        int height = fontMetrics.getHeight();
        int x = ( getWidth() - width ) / 2;
        int y = ( getHeight() - height ) / 2 + fontMetrics.getAscent();

        g2d.drawString( text, x, y );

        super.paintComponent( g );
    }

    @Override
    public @NotNull JsonObject serialize() {
        /* Template
         * {
         *      "text": $text,
         *      "color": [r, g, b],
         *      "font":
         *      {
         *          "name": $family,
         *          "weight": $style,
         *          "size": $size,
         *      }
         * }
         */
        JsonObject json = new JsonObject();
        json.addProperty( "text", text );
        json.add( "foreground", ColorUtils.toJson( getForeground() ) );
        json.add( "font", FontUtils.toJson( getFont() ) );

        return json;
    }
}
