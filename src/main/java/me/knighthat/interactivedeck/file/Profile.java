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
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.lib.connection.request.AddRequest;
import me.knighthat.lib.connection.request.RemoveRequest;
import me.knighthat.lib.connection.request.TargetedRequest;
import me.knighthat.lib.connection.request.UpdateRequest;
import me.knighthat.lib.json.SaveAsJson;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Profile implements SaveAsJson {

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

    Profile( @NotNull String displayName, boolean isDefault ) {
        this( UUID.randomUUID(), displayName, isDefault, 4, 2, 3, new ArrayList<>( 8 ) );
        addButtons( 0, columns, 0, rows );
    }

    private @NotNull JsonArray addButtons( int fromX, int toX, int fromY, int toY ) {
        JsonArray added = new JsonArray();

        for (int y = fromY ; y < toY ; y++)
            for (int x = fromX ; x < toX ; x++) {
                IButton button = new IButton( uuid, x, y );
                this.buttons.add( button );
                MenuProperty.add( button );
                added.add( button.serialize() );
            }

        sortButtons();

        String info = "Added %s button(s) to profile %s (%s)";
        Log.info( info.formatted( added.size(), displayName, uuid.toString() ) );

        return added;
    }

    private @NotNull JsonArray removeButtons( @NotNull Predicate<IButton> condition ) {
        JsonArray deleted = new JsonArray();

        Iterator<IButton> buttons = this.buttons.iterator();
        while (buttons.hasNext()) {
            IButton button = buttons.next();
            if (!condition.test( button ))
                continue;

            buttons.remove();
            MenuProperty.remove( button );
            deleted.add( button.getUuid().toString() );
        }

        sortButtons();

        String info = "Deleted %s button(s) from profile %s (%s)";
        Log.info( info.formatted( deleted.size(), displayName, uuid.toString() ) );

        return deleted;
    }

    private void sortButtons() {
        buttons.sort( Comparator.comparingInt( button -> button.getPosX() * button.getPosY() ) );
    }

    private void sendUpdate( @NotNull Consumer<JsonObject> consumer ) {
        JsonObject json = new JsonObject();
        consumer.accept( json );

        new UpdateRequest( json, uuid, TargetedRequest.Target.PROFILE ).send();
    }

    public void displayName( @NotNull String displayName ) {
        if (displayName.equals( displayName() ) || displayName.isBlank())
            return;

        Log.profileUpdate( displayName, "name", this.displayName, displayName );

        this.displayName = displayName;
        sendUpdate( json -> json.addProperty( "displayName", displayName ) );
    }

    public void columns( int columns ) {
        // If new columns is equal to current rows, then do nothing
        if (columns == columns())
            return;

        // If new rows is greater than current rows, then add more buttons
        if (columns > columns()) {
            JsonArray added = addButtons( this.columns, columns, 0, this.rows );
            new AddRequest( uuid, added ).send();
        }

        // If new columns is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (columns < columns()) {
            JsonArray deleted = removeButtons( button -> button.getPosX() >= columns );
            new RemoveRequest( uuid, deleted ).send();
        }

        Log.profileUpdate( displayName, "columns", this.columns, columns );

        this.columns = columns;
        sendUpdate( json -> json.addProperty( "columns", columns ) );
    }

    public int columns() {
        return this.columns;
    }

    /*
     * Buttons
     */

    public void rows( int rows ) {
        // If new rows is equal to current rows, then do nothing
        if (rows == rows())
            return;

        // If new rows is greater than current rows, then add more buttons
        if (rows > rows()) {
            JsonArray added = addButtons( 0, this.columns, this.rows, rows );
            new AddRequest( uuid, added ).send();
        }

        // If new rows is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (rows < rows()) {
            JsonArray deleted = removeButtons( btn -> btn.getPosY() >= rows );
            new RemoveRequest( uuid, deleted ).send();
        }

        Log.profileUpdate( displayName, "rows", this.rows, rows );

        this.rows = rows;
        sendUpdate( json -> json.addProperty( "rows", rows ) );
    }

    public int rows() {
        return this.rows;
    }

    public void gap( int gap ) {
        if (gap == gap())
            return;

        Log.profileUpdate( displayName, "gap between buttons", this.gap, gap );

        this.gap = gap;
        sendUpdate( json -> json.addProperty( "gap", gap ) );
    }

    public int gap() {
        return this.gap;
    }

    public @NotNull @Unmodifiable List<IButton> buttons() {
        return List.copyOf( buttons );
    }

    public void remove() {
        JsonArray deleted = removeButtons( button -> true );
        MenuProperty.remove( this );

        String fileName = uuid + ".profile";
        File file = new File( WorkingDirectory.path(), fileName );

        if (file.exists() && !file.delete())
            Log.err( "Could not delete " + uuid + ".profile" );

        new RemoveRequest( array -> array.add( uuid.toString() ) ).send();

        String deleteMsg = "Profile %s (%s) with %s button(s) is deleted!";
        Log.info( deleteMsg.formatted( displayName, uuid, deleted.size() ) );
    }

    public @NotNull String displayName() {return this.displayName;}

    @NotNull
    @Override
    public String getDisplayName() {return this.displayName;}

    @NotNull
    @Override
    public String getFileName() {return uuid.toString();}

    @NotNull
    @Override
    public String getFileExtension() {return "profile";}

    @Override
    public @NotNull JsonObject serialize() {
        /* Template
         * {
         *      "uuid": $uuid,
         *      "displayName": $displayName,
         *      "default": $isDefault,
         *      "rows": $rows,
         *      "columns": $columns,
         *      "gap": $gap,
         *      "buttons":
         *      {
         *          IButton.json()
         *      }
         * }
         */
        JsonArray buttons = new JsonArray();
        buttons().forEach( btn -> buttons.add( btn.serialize() ) );

        JsonObject json = new JsonObject();

        json.addProperty( "uuid", uuid.toString() );
        json.addProperty( "displayName", displayName );
        json.addProperty( "default", isDefault );
        json.addProperty( "rows", rows );
        json.addProperty( "columns", columns );
        json.addProperty( "gap", gap );
        json.add( "buttons", buttons );

        return json;
    }
}
