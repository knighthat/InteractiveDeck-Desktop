/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.connection.request.RequestSerializable;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.exception.ProfileFormatException;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.menus.component.ibutton.IButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile implements JsonSerializable, RequestSerializable {

    private final @NotNull UUID uuid;
    private final @NotNull List<IButton> buttons;
    public @NotNull String displayName;
    public boolean isDefault;
    private int columns;
    private int rows;

    private int gap;

    Profile( @NotNull UUID uuid, @NotNull String displayName, boolean isDefault, int columns, int rows, int gap, @NotNull List<IButton> buttons ) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.isDefault = isDefault;
        this.columns = columns;
        this.rows = rows;
        this.gap = gap;
        this.buttons = buttons;
    }

    public Profile() {
        this( UUID.randomUUID(), "", false, 4, 2, 3, new ArrayList<>() );
        for ( int y = 0 ; y < row() ; y++ )
            for ( int x = 0 ; x < column() ; x++ )
                this.buttons.add( new IButton( this, x, y ) );
    }

    public static @NotNull Profile fromJson( @NotNull JsonObject json ) {
        if ( !json.has( "uuid" ) ||
                !json.has( "displayName" ) ||
                !json.has( "default" ) ||
                !json.has( "rows" ) ||
                !json.has( "columns" ) ||
                !json.has( "gap" ) ||
                !json.has( "buttons" ) )
            throw new ProfileFormatException( "Missing information" );

        String idStr = json.get( "uuid" ).getAsString();
        UUID uuid = UUID.fromString( idStr );
        String displayName = json.get( "displayName" ).getAsString();
        boolean isDefault = json.get( "default" ).getAsBoolean();
        int rows = json.get( "rows" ).getAsInt();
        int columns = json.get( "columns" ).getAsInt();
        int gap = json.get( "gap" ).getAsInt();
        List<IButton> buttons = new ArrayList<>();

        Profile profile = new Profile( uuid, displayName, isDefault, rows, columns, gap, buttons );

        JsonArray btnJson = json.getAsJsonArray( "buttons" );
        btnJson.forEach( button -> {
            IButton btn = IButton.fromJson( profile, button.getAsJsonObject() );
            buttons.add( btn );
        } );

        return profile;
    }

    public @NotNull UUID uuid() {
        return this.uuid;
    }

    public void displayName( @NotNull String displayName ) {
        this.displayName = displayName;
    }

    public @NotNull String displayName() {
        return this.displayName;
    }

    public void column( int columns ) {
        for ( int y = 0 ; y < row() ; y++ )
            for ( int x = this.columns ; x < columns ; x++ ) {
                int i = y * x;
                this.buttons.add( i, new IButton( this, x, y ) );
            }
        this.columns = columns;
    }

    public int column() {
        return this.columns;
    }

    public void row( int rows ) {
        for ( int y = this.rows ; y < rows ; y++ )
            for ( int x = 0 ; x < column() ; x++ ) {
                int i = y * x;
                this.buttons.add( i, new IButton( this, x, y ) );
            }
        this.rows = rows;
    }

    public int row() {
        return this.rows;
    }

    public void gap( int gap ) {
        this.gap = gap;
    }

    public int gap() {
        return this.gap;
    }

    public @NotNull @Unmodifiable List<IButton> buttons() {
        return List.copyOf( this.buttons );
    }

    public void dump() {
        Log.info( "Saving " + displayName() );

        String fileName = this.uuid.toString().concat( ".profile" );
        File file = new File( WorkingDirectory.get(), fileName );

        try {
            file.createNewFile();

            Json.save( this.json(), file );
        } catch ( IOException e ) {
            Log.err( "Failed to save profile " + displayName() );
            Log.err( "Caused by: " + e.getMessage() );
        }
    }

    private @NotNull JsonObject jsonTemplate() {
        /* Template
         * {
         *      "uuid": uuid,
         *      "displayName": displayName,
         *      "default": isDefault,
         *      "rows": rows,
         *      "columns": columns,
         *      "gap": gap
         * }
         */
        JsonObject json = new JsonObject();

        json.add( "uuid", Json.parse( this.uuid ) );
        json.add( "displayName", Json.parse( this.displayName ) );
        json.add( "default", Json.parse( this.isDefault ) );
        json.add( "rows", Json.parse( this.row() ) );
        json.add( "columns", Json.parse( this.column() ) );
        json.add( "gap", Json.parse( this.gap() ) );

        return json;
    }

    @Override
    public @NotNull JsonObject json() {
        /*
         * "buttons":
         * [
         *      buttons
         * ]
         */
        JsonObject json = jsonTemplate();

        JsonArray buttons = new JsonArray();
        this.buttons.forEach( btn -> buttons.add( btn.json() ) );
        json.add( "buttons", buttons );

        return json;
    }

    @Override
    public @NotNull JsonObject toRequestFormat() {
        JsonObject json = jsonTemplate();

        JsonArray buttons = new JsonArray();
        this.buttons.forEach( btn -> buttons.add( btn.toRequestFormat() ) );
        json.add( "buttons", buttons );

        return json;
    }
}
