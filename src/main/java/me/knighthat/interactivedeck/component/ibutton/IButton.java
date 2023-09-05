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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.task.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

import static me.knighthat.interactivedeck.utils.ColorUtils.TRANSPARENT;

public class IButton extends JComponent implements JsonSerializable {

    public static final @NotNull Dimension DIMENSION = new Dimension( 120, 120 );

    public final @NotNull UUID uuid;
    public final @NotNull UUID profile;
    public final int x;
    public final int y;
    private final @NotNull BIcon icon;
    private final @NotNull BLabel label;
    private @Nullable Task task;

    IButton( @NotNull UUID uuid,
             @NotNull UUID profile,
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

    public IButton( @NotNull UUID profile, int x, int y ) {
        this( UUID.randomUUID(), profile, new BIcon(), new BLabel(), x, y, null );
    }

    public void toggleSelect() {
        icon.toggleSelect();
    }

    public @NotNull Color background() {
        return icon.getBackground();
    }

    public void background( @NotNull Color color ) {
        icon.setBackground( color );
    }

    public @NotNull Color border() {return icon.getForeground();}

    public void border( @NotNull Color color ) {
        icon.setForeground( color );
    }

    public @NotNull Color foreground() {
        return label.getForeground();
    }

    public void foreground( @NotNull Color color ) {
        label.setForeground( color );
    }

    public @NotNull String text() {
        return label.text();
    }

    public void text( @NotNull String text ) {
        label.text( text );
    }

    public @NotNull Font font() {
        return label.getFont();
    }

    public void font( @NotNull Font font ) {
        label.setFont( font );
    }

    public void task( @Nullable Task task ) {
        this.task = task;
    }

    public @Nullable Task task() {
        return this.task;
    }

    @Override
    public @NotNull JsonObject serialize() {
        /* Template
         * {
         *      "uuid": $uuid,
         *      "x": $x,
         *      "y": $y,
         *      "task":
         *      {
         *          Task.json()
         *      }
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
        JsonElement task = this.task != null ? this.task.serialize() : JsonNull.INSTANCE;

        JsonObject json = new JsonObject();

        json.addProperty( "uuid", uuid.toString() );
        json.addProperty( "x", x );
        json.addProperty( "y", y );
        json.add( "task", task );
        json.add( "icon", icon.serialize() );
        json.add( "label", label.serialize() );

        return json;
    }
}