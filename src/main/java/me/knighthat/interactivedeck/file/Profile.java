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

import com.google.gson.*;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.connection.request.RequestSerializable;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.exception.ProfileFormatException;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.menus.MenuProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Profile implements JsonSerializable, RequestSerializable {

    public final @NotNull UUID uuid;
    public final boolean isDefault;
    private final @NotNull List<IButton> buttons;
    private @NotNull String displayName;
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

    public Profile( @NotNull String displayName, boolean isDefault ) {
        // Init an array list of 8 because it contains 4 columns and 2 rows by default
        this( UUID.randomUUID(), displayName, isDefault, 4, 2, 3, new ArrayList<>( 8 ) );

        for (int y = 0 ; y < rows ; y++)
            for (int x = 0 ; x < columns ; x++)
                this.buttons.add( new IButton( this, x, y ) );
    }

    public static @NotNull Optional<Profile> fromFile( @NotNull File file ) {
        Profile profile = null;

        try (FileReader reader = new FileReader( file )) {

            JsonElement json = JsonParser.parseReader( reader );
            profile = Profile.fromJson( json.getAsJsonObject() );

        } catch (ProfileFormatException e) {

            Log.warn( file.getName() + " does not meet requirements. Skipping..." );

        } catch (JsonParseException e) {

            Log.warn( file.getName() + " is not a valid JSON file. Skipping..." );
            Log.warn( "Cause: " + e.getMessage() );

        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {

            Log.err( "Could not read " + file.getName() + ". Perhaps permission error?" );
            Log.err( "Error message: " + e.getMessage() );

        }
        return Optional.ofNullable( profile );
    }

    private static @NotNull Profile fromJson( @NotNull JsonObject json ) {
        if (!json.has( "uuid" ) ||
                !json.has( "displayName" ) ||
                !json.has( "default" ) ||
                !json.has( "rows" ) ||
                !json.has( "columns" ) ||
                !json.has( "gap" ) ||
                !json.has( "buttons" ))
            throw new ProfileFormatException( "Missing information" );

        String idStr = json.get( "uuid" ).getAsString();
        UUID uuid = UUID.fromString( idStr );
        String displayName = json.get( "displayName" ).getAsString();
        boolean isDefault = json.get( "default" ).getAsBoolean();
        int rows = json.get( "rows" ).getAsInt();
        int columns = json.get( "columns" ).getAsInt();
        int gap = json.get( "gap" ).getAsInt();
        List<IButton> buttons = new ArrayList<>();

        Profile profile = new Profile( uuid, displayName, isDefault, columns, rows, gap, buttons );

        JsonArray btnJson = json.getAsJsonArray( "buttons" );
        btnJson.forEach( button -> {
            IButton btn = IButton.fromJson( profile, button.getAsJsonObject() );
            buttons.add( btn );
        } );

        return profile;
    }

    public void displayName( @NotNull String displayName ) {
        this.displayName = displayName;
    }

    public @NotNull String displayName() {
        return this.displayName;
    }

    public void column( int columns ) {
        // If new columns is equal to current rows, then do nothing
        if (columns == this.columns)
            return;

        // If new rows is greater than current rows, then add more buttons
        if (columns > this.columns)
            for (int y = 0 ; y < this.rows ; y++)
                for (int x = this.columns ; x < columns ; x++) {
                    IButton button = new IButton( this, x, y );
                    this.buttons.add( button );
                    MenuProperty.add( button );
                }

        // If new columns is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (columns < this.columns) {
            List<IButton> toBeDeleted = new ArrayList<>();

            this.buttons
                    .stream()
                    .filter( btn -> btn.x() >= columns )
                    .forEach( toBeDeleted::add );

            toBeDeleted.forEach( btn -> {
                this.buttons.remove( btn );
                MenuProperty.remove( btn );
            } );
        }
        this.columns = columns;
    }

    public int column() {
        return this.columns;
    }

    public void row( int rows ) {
        // If new rows is equal to current rows, then do nothing
        if (rows == this.rows)
            return;

        // If new rows is greater than current rows, then add more buttons
        if (rows > this.rows)
            for (int y = this.rows ; y < rows ; y++)
                for (int x = 0 ; x < this.columns ; x++) {
                    IButton button = new IButton( this, x, y );
                    this.buttons.add( button );
                    MenuProperty.add( button );
                }

        // If new rows is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (rows < this.rows) {
            List<IButton> toBeDeleted = new ArrayList<>();

            this.buttons
                    .stream()
                    .filter( btn -> btn.y() >= rows )
                    .forEach( toBeDeleted::add );

            toBeDeleted.forEach( btn -> {
                this.buttons.remove( btn );
                MenuProperty.remove( btn );
            } );
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
        File file = new File( WorkingDirectory.file(), fileName );

        try {
            file.createNewFile();
            Json.save( this.serialize(), file );
        } catch (IOException e) {
            Log.err( "Failed to save profile " + displayName() );
            Log.err( "Caused by: " + e.getMessage() );
        }
    }

    public void remove() {
        MenuProperty.remove( this );
        String fileName = uuid + ".profile";
        File file = new File( WorkingDirectory.path(), fileName );

        if (file.exists())
            file.delete();
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

        json.addProperty( "uuid", uuid.toString() );
        json.addProperty( "displayName", displayName );
        json.addProperty( "default", isDefault );
        json.addProperty( "rows", row() );
        json.addProperty( "columns", column() );
        json.addProperty( "gap", gap() );

        return json;
    }

    @Override
    public @NotNull JsonObject serialize() {
        /*
         * "buttons":
         * [
         *      buttons
         * ]
         */
        JsonObject json = jsonTemplate();

        JsonArray buttons = new JsonArray();
        this.buttons.forEach( btn -> buttons.add( btn.serialize() ) );
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
