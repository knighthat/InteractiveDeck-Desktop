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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.json.SaveAsJson;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Settings implements SaveAsJson {

    public static final @NotNull Settings SETTINGS = new Settings();

    // NET
    private @NotNull String address = "0.0.0.0";
    private @Range( from = 0x400, to = 0xFFFF ) int port = 9129;
    private int bufferSize = 1024;
    // UI
    private @NotNull Color selectedColor = Color.YELLOW;
    private @NotNull Font UIFont = new Font( "Comfortaa", Font.PLAIN, 14 );
    private @NotNull Font defaultButtonFont = new Font( "StardosStencil", Font.PLAIN, 14 );

    public static void init() {
        File settingsFile = new File( WorkingDirectory.FILE, SETTINGS.fullName() );
        try (FileReader reader = new FileReader( settingsFile )) {
            JsonElement json = JsonParser.parseReader( reader );
            if (!json.isJsonNull() && json.isJsonObject())
                SETTINGS.load( json.getAsJsonObject() );
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            Log.exc( "Could not read " + SETTINGS.fullName(), e, false );
        }
    }

    public void load( @NotNull JsonObject json ) {
        if (json.has( "address" ))
            address = json.get( "address" ).getAsString();
        if (json.has( "port" ))
            port = json.get( "port" ).getAsInt();
        if (json.has( "buffer" ))
            bufferSize = json.get( "buffer" ).getAsInt();
        if (json.has( "selected_color" )) {
            JsonArray color = json.getAsJsonArray( "selected_color" );
            selectedColor = ColorUtils.fromJson( color );
        }
        if (json.has( "ui_font" )) {
            JsonObject font = json.getAsJsonObject( "ui_font" );
            UIFont = FontUtils.fromJson( font );
        }
        if (json.has( "default_button_font" )) {
            JsonObject font = json.getAsJsonObject( "default_button_font" );
            defaultButtonFont = FontUtils.fromJson( font );
        }
    }

    public @NotNull String address() {
        return this.address;
    }

    public void address( @NotNull String address ) {
        this.address = address;
    }

    public byte[] addressInBytes() {
        byte[] bytes = new byte[4];
        String[] splitAddr = address.split( "\\." );
        for (int i = 0 ; i < bytes.length ; i++)
            bytes[i] = Byte.parseByte( splitAddr[i] );

        return bytes;
    }

    public @Range( from = 0x400, to = 0xFFFF ) int port() {
        return this.port;
    }

    public void port( @Range( from = 0x400, to = 0xFFFF ) int port ) {
        this.port = port;
    }

    public @NotNull String fullAddress() {
        return address + ":" + port;
    }

    public int bufferSize() {
        return this.bufferSize;
    }

    public void bufferSize( int size ) {
        this.bufferSize = size;
    }

    public byte[] buffer() {
        return new byte[bufferSize];
    }

    public @NotNull Color selectedColor() {
        return this.selectedColor;
    }

    public void selectedColor( @NotNull Color color ) {
        this.selectedColor = color;
    }

    public @NotNull Font UIFont() {
        return this.UIFont;
    }

    public void UIFont( @NotNull Font font ) {
        this.UIFont = font;
    }

    public @NotNull Font defaultButtonFont() {
        return this.defaultButtonFont;
    }

    public void defaultButtonFont( @NotNull Font font ) {
        this.defaultButtonFont = font;
    }

    @Override
    public @NotNull String displayName() {
        return "settings file";
    }

    @Override
    public @NotNull String fileName() {
        return "settings";
    }

    @Override
    public @NotNull JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty( "address", address );
        json.addProperty( "port", port );
        json.addProperty( "buffer", bufferSize );
        json.add( "selected_color", ColorUtils.toJson( selectedColor ) );
        json.add( "ui_font", FontUtils.toJson( UIFont ) );
        json.add( "default_button_font", FontUtils.toJson( defaultButtonFont ) );

        return json;
    }
}
