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
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.menus.MenuProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class Profile implements JsonSerializable {

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

    public void displayName( @NotNull String displayName ) {
        this.displayName = displayName;
    }

    public @NotNull String displayName() {
        return this.displayName;
    }

    public void columns( int columns ) {
        // If new columns is equal to current rows, then do nothing
        if (columns == this.columns)
            return;

        // If new rows is greater than current rows, then add more buttons
        if (columns > this.columns)
            addButtons( this.columns, columns, 0, this.rows );

        // If new columns is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (columns < this.columns)
            removeButtons( button -> button.x >= columns );

        this.columns = columns;
    }

    public int columns() {
        return this.columns;
    }

    public void rows( int rows ) {
        // If new rows is equal to current rows, then do nothing
        if (rows == this.rows)
            return;

        // If new rows is greater than current rows, then add more buttons
        if (rows > this.rows) {
            JsonArray array = new JsonArray();

            addButtons( 0, this.columns, this.rows, rows )
                    .forEach( button -> array.add( button.serialize() ) );
        }

        // If new rows is less than current row,
        // then remove excess buttons within profile and public list of buttons
        if (rows < this.rows)
            removeButtons( btn -> btn.y >= rows );

        this.rows = rows;
    }

    public int rows() {
        return this.rows;
    }

    public void gap( int gap ) {
        this.gap = gap;
    }

    public int gap() {
        return this.gap;
    }

    /*
     * Buttons
     */

    public @NotNull @Unmodifiable List<IButton> buttons() {
        return List.copyOf( buttons );
    }

    private @NotNull Collection<IButton> addButtons( int fromX, int toX, int fromY, int toY ) {
        Collection<IButton> newButtons = new HashSet<>();

        for (int y = fromY ; y < toY ; y++)
            for (int x = fromX ; x < toX ; x++) {
                IButton button = new IButton( uuid, x, y );
                this.buttons.add( button );
                MenuProperty.add( button );
                newButtons.add( button );
            }

        sortButtons();

        return newButtons;
    }

    private @NotNull Collection<IButton> removeButtons( @NotNull Predicate<IButton> condition ) {
        Collection<IButton> deletedButtons = new HashSet<>();

        Iterator<IButton> buttons = this.buttons.iterator();
        while (buttons.hasNext()) {
            IButton button = buttons.next();
            if (!condition.test( button ))
                continue;

            buttons.remove();
            MenuProperty.remove( button );
            deletedButtons.add( button );
        }

        sortButtons();

        return deletedButtons;
    }

    private void sortButtons() {
        buttons.sort( Comparator.comparingInt( button -> button.x * button.y ) );
    }

    public void remove() {
        removeButtons( button -> true );
        MenuProperty.remove( this );

        String fileName = uuid + ".profile";
        File file = new File( WorkingDirectory.path(), fileName );

        if (file.exists())
            file.delete();
    }

    @Override
    public @NotNull JsonElement serialize() {
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
