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

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FontUtils {

    @Contract( pure = true )
    public static @NotNull Font fromJson( @NotNull JsonObject json ) {
        String name = "Arial";
        int size = 14;
        int weight = Font.PLAIN;

        if (json.has( "name" ))
            name = json.get( "name" ).getAsString();
        if (json.has( "size" ))
            size = json.get( "size" ).getAsInt();
        if (json.has( "weight" ))
            weight = switch (json.get( "weight" ).getAsString()) {
                case "bold" -> Font.BOLD;
                case "italic" -> Font.ITALIC;
                default -> Font.PLAIN;
            };

        return new Font( name, weight, size );
    }

    @Contract( pure = true )
    public static @NotNull JsonObject toJson( @NotNull Font font ) {
        String weight = switch (font.getStyle()) {
            case Font.BOLD -> "bold";
            case Font.ITALIC -> "italic";
            default -> "plain";
        };

        JsonObject json = new JsonObject();
        json.addProperty( "name", font.getName() );
        json.addProperty( "weight", weight );
        json.addProperty( "size", font.getSize() );

        return json;
    }
}
