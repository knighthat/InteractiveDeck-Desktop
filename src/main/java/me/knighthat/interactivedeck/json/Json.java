/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.json;

import com.google.gson.*;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Json {

    private static final @NotNull Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public static @NotNull JsonElement parse( @Nullable Object obj ) {
        JsonElement element;
        if ( obj == null ) {
            element = JsonNull.INSTANCE;
        } else if ( obj instanceof Number numb ) {
            element = new JsonPrimitive( numb );
        } else if ( obj instanceof Boolean bool ) {
            element = new JsonPrimitive( bool );
        } else if ( obj instanceof Color color ) {
            element = ColorUtils.toJson( color );
        } else if ( obj instanceof Font font ) {
            element = FontUtils.toJson( font );
        } else if ( obj instanceof Iterable<?> list ) {
            element = new JsonArray();
            list.forEach( e -> {
                JsonElement value = parse( e );
                ( (JsonArray) element ).add( value );
            } );
        } else {
            element = new JsonPrimitive( String.valueOf( obj ) );
        }
        return element;
    }

    public static void save( @NotNull JsonObject json, @NotNull File file ) throws IOException {
        FileWriter writer = new FileWriter( file );
        GSON.toJson( json, writer );
        writer.close();
    }
}