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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static me.knighthat.interactivedeck.settings.Settings.SETTINGS;

public class FontUtils {

    /**
     * Converts {@link JsonElement} into {@link  JsonObject} then
     * extracts <b>name</b>, <b>size</b>, <b>style</b> to convert
     * them to new {@link Font}.<br>
     *
     * Invalid value will be replaced by default UI Font's value.<br>
     *
     * @param json an instance of {@link JsonObject} that holds {@link Font} values
     */
    @Contract( pure = true )
    public static @NotNull Font fromJson( @NotNull JsonElement json ) {
        if (!( json instanceof JsonObject jObj ))
            return SETTINGS.getUiFont();

        // Font family
        String family;
        try {
            family = jObj.get( "name" ).getAsString();
        } catch (IllegalStateException | NullPointerException e) {
            family = SETTINGS.getUiFont().getFamily();
        }

        // Font size
        int size;
        try {
            size = jObj.get( "size" ).getAsInt();
        } catch (IllegalStateException | NullPointerException e) {
            size = SETTINGS.getUiFont().getSize();
        }

        // Font style
        int style;
        try {
            style = switch (jObj.get( "weight" ).getAsString()) {
                case "bold" -> Font.BOLD;
                case "italic" -> Font.ITALIC;
                case "bold|italic" -> Font.BOLD | Font.ITALIC;
                default -> Font.PLAIN;
            };
        } catch (IllegalStateException | NullPointerException e) {
            style = SETTINGS.getUiFont().getStyle();
        }

        return new Font( family, style, size );
    }

    @Contract( pure = true )
    public static @NotNull JsonObject toJson( @NotNull Font font ) {
        String weight = switch (font.getStyle()) {
            case Font.BOLD -> "bold";
            case Font.ITALIC -> "italic";
            case Font.BOLD | Font.ITALIC -> "bold|italic";
            default -> "plain";
        };

        JsonObject json = new JsonObject();
        json.addProperty( "name", font.getFamily() );
        json.addProperty( "weight", weight );
        json.addProperty( "size", font.getSize() );

        return json;
    }
}
