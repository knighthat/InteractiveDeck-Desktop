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

package me.knighthat.interactivedeck.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Settings {

    public static @NotNull File FILE;

    public static String ADDRESS = "0.0.0.0";

    public static int PORT = 9129;

    public static byte[] BUFFER = new byte[1024];

    // UI
    public static @NotNull Color SELECTED_COLOR = Color.YELLOW;

    public static void init() {
        File settings = new File( WorkingDirectory.file(), "settings.json" );
        try {
            if (settings.exists()) {
                load( settings );
            } else
                settings.createNewFile();
        } catch (IOException e) {
            Log.err( "Couldn't generate settings.yml. Perhaps permission error?" );
            Log.err( "Reason: " + e.getMessage() );
        } finally {
            FILE = settings;
        }
    }

    static void load( @NotNull File file ) {
        try (Reader reader = new FileReader( file )) {
            JsonElement json = JsonParser.parseReader( reader );
            if (!json.isJsonNull())
                load( json.getAsJsonObject() );
        } catch (IOException e) {
            Log.err( "Error occurs while reading settings." );
            Log.err( "New settings file will be save at shutdown" );
            Log.err( "Caused by: " + e.getMessage() );
        }
    }

    static void load( @NotNull JsonObject json ) {
        if (json.has( "address" ))
            ADDRESS = json.get( "address" ).getAsString();
        if (json.has( "port" ))
            PORT = json.get( "port" ).getAsInt();
        if (json.has( "buffer" )) {
            int size = json.get( "buffer" ).getAsInt();
            BUFFER = new byte[size];
        }
        if (json.has( "selected_color" ))
            SELECTED_COLOR = ColorUtils.fromJson( json.getAsJsonArray( "selected_color" ) );
    }

    public static @NotNull String address() {
        return ADDRESS + ":" + PORT;
    }
}
