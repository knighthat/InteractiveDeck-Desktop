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

package me.knighthat.interactivedeck.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.awt.*;

/**
 * @author knighthat
 */
public class ColorUtils {

    public static @NotNull Color TRANSPARENT = new Color( 0, 0, 0, 0 );
    public static @NotNull Color DEFAULT_DARK = new Color( 36, 36, 36 );

    private static @Range( from = 0x0, to = 0xff ) int assertColor( int value ) {return value < 0 ? 0 : Math.min( value, 255 );}

    public static @NotNull String toHex( @NotNull Color color ) {return toHex( color.getRed(), color.getGreen(), color.getBlue() );}

    public static @NotNull String toHex( @Range( from = 0x0, to = 0xff ) int r,
                                         @Range( from = 0x0, to = 0xff ) int g,
                                         @Range( from = 0x0, to = 0xff ) int b ) {
        return String.format( "#%02x%02x%02x", assertColor( r ), assertColor( g ), assertColor( b ) );
    }

    public static @NotNull Color fromHex( @NotNull String hex ) {return Color.decode( hex );}

    /**
     * Return color stored inside a JsonArray.<br>
     * Returns {@link #TRANSPARENT} color if:<br>
     * - Provided {@link JsonElement} is not {@link JsonArray}.<br>
     * - {@link JsonArray} has less than 3 color codes (R, G, B).<br>
     * Additionally, color code exceeds default value (0-255)
     * will be reset to the closest end.
     *
     * @param json is JsonArray that has at least 3 integer values.
     */
    public static @NotNull Color fromJson( @NotNull JsonElement json ) {
        Color result = TRANSPARENT;
        try {
            JsonArray array = json.getAsJsonArray();
            int r = assertColor( array.get( 0 ).getAsInt() );
            int g = assertColor( array.get( 1 ).getAsInt() );
            int b = assertColor( array.get( 2 ).getAsInt() );

            result = new Color( r, g, b );
        } catch (IllegalStateException | IndexOutOfBoundsException ignored) {
        }
        return result;
    }

    public static @NotNull JsonArray toJson( @NotNull Color color ) {
        JsonArray array = new JsonArray( 3 );
        array.add( color.getRed() );
        array.add( color.getGreen() );
        array.add( color.getBlue() );

        return array;
    }

    /*
     * RGB -> YIQ: https://en.wikipedia.org/wiki/YIQ
     */
    public static Color getContrast( @NotNull Color color ) {
        float r = .299F * color.getRed();
        float g = .587F * color.getGreen();
        float b = .114F * color.getBlue();
        float luma = r + g + b;

        return luma >= 128 ? Color.BLACK : Color.WHITE;
    }
}
