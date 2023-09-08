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
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.UUID;

public class IButtons {

    public static @NotNull IButton fromJson( @NotNull UUID profile, @NotNull JsonObject json ) throws JsonSyntaxException {
        if (!json.has( "uuid" ) ||
                !json.has( "icon" ) ||
                !json.has( "label" ))
            throw new JsonSyntaxException( "Not enough argument" );

        String idStr = json.get( "uuid" ).getAsString();
        JsonObject iconJson = json.getAsJsonObject( "icon" );
        JsonObject labelJson = json.getAsJsonObject( "label" );
        Task task = null;

        if (json.has( "task" ) && json.get( "task" ) != JsonNull.INSTANCE)
            task = Task.fromJson( json.getAsJsonObject( "task" ) );

        return new IButton(
                UUID.fromString( idStr ),
                profile,
                icon( iconJson ),
                label( labelJson ),
                json.get( "x" ).getAsInt(),
                json.get( "y" ).getAsInt(),
                task
        );
    }

    static @NotNull BLabel label( @NotNull JsonObject json ) {
        BLabel label = new BLabel();

        String text = json.get( "text" ).getAsString();
        label.text( text );

        String fgProp = json.has( "color" ) ? "color" : "foreground";
        label.setForeground( fromJson( json, fgProp ) );

        JsonObject fontJson = json.getAsJsonObject( "font" );
        Font font = FontUtils.fromJson( fontJson );
        label.setFont( font );

        return label;
    }

    static @NotNull BIcon icon( @NotNull JsonObject json ) {
        BIcon icon = new BIcon();

        String outerProp = json.has( "outer" ) ? "outer" : "border";
        icon.setForeground( fromJson( json, outerProp ) );

        String innerProp = json.has( "inner" ) ? "inner" : "background";
        icon.setBackground( fromJson( json, innerProp ) );

        return icon;
    }

    static @NotNull Color fromJson( @NotNull JsonObject json, @NotNull String property ) {
        JsonArray array = json.getAsJsonArray( property );
        return ColorUtils.fromJson( array );
    }
}
