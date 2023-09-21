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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;
import static me.knighthat.interactivedeck.utils.ColorUtils.DEFAULT_DARK;

final class BIcon extends BChild {

    static final @NotNull Dimension borderRadius;

    static {
        borderRadius = new Dimension( 15, 15 );
    }

    boolean isSelected = false;

    BIcon() {
        super();

        setBackground( DEFAULT_DARK );
        setForeground( DEFAULT_DARK );
    }

    void toggleSelect() {
        this.isSelected = !this.isSelected;
        repaint();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        int width = getWidth() - 1, height = getHeight() - 1;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor( getBackground() );
        g2d.fillRoundRect( 0, 0, width, height, borderRadius.width, borderRadius.height );

        g2d.setColor( isSelected ? SETTINGS.selectedColor() : getForeground() );
        g2d.drawRoundRect( 0, 0, width, height, borderRadius.width, borderRadius.height );

        super.paintComponent( g );
    }

    @Override
    public @NotNull JsonObject serialize() {
        /*
         * {
         *      "inner": [r, g, b]
         *      "outer": [r, g, b]
         * }
         */
        JsonArray inner = ColorUtils.toJson( getBackground() );
        JsonArray outer = ColorUtils.toJson( getForeground() );

        JsonObject json = new JsonObject();

        json.add( "background", inner );
        json.add( "border", outer );

        return json;
    }
}
