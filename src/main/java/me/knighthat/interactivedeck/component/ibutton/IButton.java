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
import me.knighthat.interactivedeck.connection.request.TargetedRequest;
import me.knighthat.interactivedeck.connection.request.UpdateRequest;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.task.GotoPage;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

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
        if (color.equals( background() ))
            return;

        Log.buttonUpdate( uuid, "background", ColorUtils.toHex( background() ), ColorUtils.toHex( color ) );

        icon.setBackground( color );
        sendUpdate( json -> json.add( "background", ColorUtils.toJson( color ) ) );
    }

    public @NotNull Color border() {return icon.getForeground();}

    public void border( @NotNull Color color ) {
        if (color.equals( border() ))
            return;

        Log.buttonUpdate( uuid, "border", ColorUtils.toHex( border() ), ColorUtils.toHex( color ) );

        icon.setForeground( color );
        sendUpdate( json -> json.add( "border", ColorUtils.toJson( color ) ) );
    }

    public @NotNull Color foreground() {
        return label.getForeground();
    }

    public void foreground( @NotNull Color color ) {
        if (color.equals( foreground() ))
            return;

        Log.buttonUpdate( uuid, "foreground", ColorUtils.toHex( foreground() ), ColorUtils.toHex( color ) );

        label.setForeground( color );
        sendUpdate( json -> json.add( "foreground", ColorUtils.toJson( color ) ) );
    }

    public @NotNull String text() {
        return label.text();
    }

    public void text( @NotNull String text ) {
        if (text.equals( text() ))
            return;

        Log.buttonUpdate( uuid, "text", text(), text );

        label.text( text );
        sendUpdate( json -> json.addProperty( "text", text ) );
    }

    public @NotNull Font font() {
        return label.getFont();
    }

    public void font( @NotNull Font font ) {
        if (font.getFamily().equals( font.getFamily() ) &&
                font.getStyle() == font().getStyle() &&
                font.getSize() == font().getSize())
            return;

        String fontFormat = "[f=%s,s=%s,w=%s]";
        String currentFont = fontFormat.formatted( font().getFamily(), font().getSize(), font().getStyle() );
        String newFont = fontFormat.formatted( font.getFamily(), font.getSize(), font.getStyle() );
        Log.buttonUpdate( uuid, "font", currentFont, newFont );

        label.setFont( font );
        sendUpdate( json -> json.add( "font", FontUtils.toJson( font() ) ) );
    }

    public void task( @Nullable Task task ) {
        String currentTask = this.task != null ? this.task.getClass().getName() : "null";
        String newTask = task != null ? task.getClass().getName() : "null";
        Log.buttonUpdate( uuid, "task", currentTask, newTask );

        this.task = task;

        if (!( task instanceof GotoPage gotoPage ))
            return;
        String pUuid = gotoPage.target().toString();
        sendUpdate( json -> json.addProperty( "goto", pUuid ) );
    }

    public @Nullable Task task() {
        return this.task;
    }

    private void sendUpdate( @NotNull Consumer<JsonObject> consumer ) {
        JsonObject json = new JsonObject();
        consumer.accept( json );

        new UpdateRequest( TargetedRequest.Target.BUTTON, uuid, json ).send();
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