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

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import me.knighthat.interactivedeck.connection.request.RequestSerializable;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.task.GotoPage;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

import static me.knighthat.interactivedeck.file.Settings.SELECTED_COLOR;
import static me.knighthat.interactivedeck.utils.ColorUtils.TRANSPARENT;

public class IButton extends JComponent implements JsonSerializable, RequestSerializable {

    private final @NotNull UUID uuid;
    private final @NotNull Profile profile;
    private final @NotNull BIcon icon;
    private final @NotNull BLabel label;
    private final int x;
    private final int y;
    private @Nullable Task task;

    IButton( @NotNull UUID uuid,
             @NotNull Profile profile,
             @NotNull BIcon icon,
             @NotNull BLabel label,
             int x, int y,
             @Nullable Task task ) {
        this.uuid = uuid;
        this.profile = profile;
        this.icon = icon;
        this.label = label;
        this.x = x;
        this.y = y;
        this.task = task;

        setOpaque( false );
        setForeground( TRANSPARENT );

        setLayout( new OverlayLayout( this ) );
        add( label, 0 );
        add( icon, 1 );
    }

    public IButton( @NotNull Profile profile, int x, int y ) {
        this( UUID.randomUUID(), profile, new BIcon(), new BLabel(), x, y, null );
    }


    public static @NotNull IButton fromJson( @NotNull Profile profile, @NotNull JsonObject json ) throws JsonSyntaxException {
        if (!json.has( "uuid" ) ||
                !json.has( "icon" ) ||
                !json.has( "label" ))
            throw new JsonSyntaxException( "Not enough argument" );

        String idStr = json.get( "uuid" ).getAsString();
        UUID uuid = UUID.fromString( idStr );

        JsonObject iconJson = json.getAsJsonObject( "icon" );
        BIcon icon = BIcon.fromJson( iconJson );

        JsonObject labelJson = json.getAsJsonObject( "label" );
        BLabel label = BLabel.fromJson( labelJson );

        JsonPrimitive xPrim = json.getAsJsonPrimitive( "x" );
        int x = xPrim.getAsInt();

        JsonPrimitive yPrim = json.getAsJsonPrimitive( "y" );
        int y = yPrim.getAsInt();

        Task task = json.has( "task" ) ? Task.fromJson( json.getAsJsonObject( "task" ) ) : null;

        return new IButton( uuid, profile, icon, label, x, y, task );
    }

    public @NotNull UUID uuid() {
        return this.uuid;
    }

    public @NotNull Profile profile() {
        return this.profile;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public void select() {
        this.icon.repaint( SELECTED_COLOR, this.icon.inner() );
    }

    public void unselect() {
        this.icon.repaint( this.icon.inner(), this.icon.inner() );
    }

    public void background( @NotNull Color bg ) {
        this.icon.repaint( null, bg );
    }

    public @NotNull Color background() {
        return this.icon.inner();
    }

    public void foreground( @NotNull Color fg ) {
        this.label.setForeground( fg );
    }

    public @NotNull Color foreground() {
        return this.label.getForeground();
    }

    public void text( @NotNull String text ) {
        this.label.text( text );
    }

    public @NotNull String text() {
        return this.label.text();
    }

    public void font( @NotNull Font font ) {
        this.label.font( font );
    }

    public @NotNull Font font() {
        return this.label.font;
    }

    public void task( @Nullable Task task ) {
        this.task = task;
    }

    public @Nullable Task task() {
        return this.task;
    }

    private @NotNull JsonObject jsonObject() {
        /* Template
         * {
         *      "uuid": "UUID",
         *      "x": x,
         *      "y": y,
         *      "script": script.path
         *      "icon": {}
         *      "label": {}
         * }
         */
        JsonObject json = new JsonObject();

        json.addProperty( "uuid", uuid().toString() );
        json.addProperty( "x", x() );
        json.addProperty( "y", y() );

        return json;
    }

    @Override
    public @NotNull JsonObject serialize() {
        /* Template
         * {
         *      "uuid": "UUID",
         *      "x": x,
         *      "y": y,
         *      "script": script.path
         *      "icon":
         *      {
         *          BIcon.json()
         *      }
         *      "label":
         *      {
         *          BLabel.json()
         *      }
         * }
         */
        JsonObject json = jsonObject();
        if (this.task != null)
            json.add( "task", task.serialize() );
        json.add( "icon", icon.serialize() );
        json.add( "label", label.serialize() );

        return json;
    }

    @Override
    public @NotNull JsonObject toRequestFormat() {
        /* Template
         * {
         *      "uuid": $uuid,
         *      "x": $x,
         *      "y": $y,
         *      "background": $color,
         *      "foreground": $color,
         *      "text": $text
         * }
         */
        JsonObject json = jsonObject();

        json.add( "background", ColorUtils.toJson( icon.inner() ) );
        json.add( "foreground", ColorUtils.toJson( label.getForeground() ) );
        json.addProperty( "text", label.text() );
        if (task instanceof GotoPage t)
            json.addProperty( "goto", t.target().toString() );

        return json;
    }
}