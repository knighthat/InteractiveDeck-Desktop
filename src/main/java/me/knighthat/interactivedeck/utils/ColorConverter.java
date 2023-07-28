/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author knighthat
 */
public class ColorConverter {

    public static @NotNull Color TRANSPARENT = new Color(0, 0, 0, 0);
    public static @NotNull Color DEFAULT_DARK = new Color(36, 36, 36);

    public static String toHex( Color color ) {
        return toHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String toHex( int r, int g, int b ) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public static Color parse( String hex ) {
        return Color.decode(hex);
    }

    /*
     * Thanks brimborium for providing this piece of code
     * For details, visit https://stackoverflow.com/questions/4672271/reverse-opposing-colors
     */
    public static Color getContrast( @NotNull Color color ) {
        double y = ( 299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue() ) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}
