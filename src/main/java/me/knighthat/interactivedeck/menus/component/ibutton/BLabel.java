/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.menus.component.ibutton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

final class BLabel extends BChild {

    @NotNull
    String text = "";

    @NotNull Font font = new Font( "Stardos Stencil", Font.PLAIN, 14 );

    BLabel() {
        super();
        setForeground( Color.WHITE );
    }

    static @NotNull BLabel fromJson( @NotNull JsonObject json ) {
        BLabel label = new BLabel();

        String text = json.get( "text" ).getAsString();
        label.text( text );

        JsonArray colorJson = json.getAsJsonArray( "color" );
        Color color = ColorUtils.fromJson( colorJson );
        label.setForeground( color );

        JsonObject fontJson = json.getAsJsonObject( "font" );
        Font font = FontUtils.fromJson( fontJson );
        label.font( font );

        return label;
    }

    public void text( @NotNull String text ) {
        this.text = text;
        repaint();
    }

    public @NotNull String text() {
        return this.text;
    }

    public @NotNull Font font() {
        return this.font;
    }

    public void font( @NotNull Font font ) {
        this.font = font;
        repaint();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor( getForeground() );
        g2d.setFont( this.font );
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int width = fontMetrics.stringWidth( text );
        int height = fontMetrics.getHeight();
        int x = ( getWidth() - width ) / 2;
        int y = ( getHeight() - height ) / 2 + fontMetrics.getAscent();
        g2d.drawString( text, x, y );

        super.paintComponent( g );
    }

    @Override
    public @NotNull JsonObject json() {
        /* Template
         * {
         *      "text":"placeholder",
         *      "color":[r, r, r],
         *      "font":
         *      {
         *          "name": "font name",
         *          "weight":"plain",
         *          "size":"14",
         *      }
         * }
         */
        JsonObject json = new JsonObject();
        json.add( "text", Json.parse( this.text() ) );
        json.add( "color", Json.parse( super.getForeground() ) );
        json.add( "font", Json.parse( this.font() ) );

        return json;
    }
}
