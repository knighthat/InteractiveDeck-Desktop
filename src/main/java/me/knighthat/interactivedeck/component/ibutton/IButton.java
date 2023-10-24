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
import lombok.Getter;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.lib.component.LiveComponent;
import me.knighthat.lib.component.ibutton.InteractiveButton;
import me.knighthat.lib.connection.request.RequestJson;
import me.knighthat.lib.connection.request.TargetedRequest;
import me.knighthat.lib.json.JsonSerializable;
import me.knighthat.lib.logging.EventLogging;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static me.knighthat.interactivedeck.utils.ColorUtils.TRANSPARENT;

@Getter
public class IButton extends JComponent implements InteractiveButton, JsonSerializable, RequestJson {

    /**
     * Turns {@link JsonObject} to {@link IButton} instance.<br>
     *
     * @param profile this button belongs to.
     * @param json    the json text of this button.
     *
     * @return instance of {@link IButton}.
     *
     * @throws IOException if coordinate <b>x</b> or <b>y</b> is missing.
     */
    public static @NotNull IButton fromJson( @NotNull UUID profile, @NotNull JsonObject json ) throws IOException {
        if (!json.has( "x" ))
            throw new IOException( "Missing coordinate \"x\"" );
        if (!json.has( "y" ))
            throw new IOException( "Missing coordinate \"y\"" );

        int x = json.get( "x" ).getAsInt();
        int y = json.get( "y" ).getAsInt();
        UUID uuid;
        if (json.has( "uuid" )) {
            String idString = json.get( "uuid" ).getAsString();
            uuid = UUID.fromString( idString );
        } else {
            Log.warn( "Button from profile " + profile + " does not have an UUID, assigning new one." );
            uuid = UUID.randomUUID();
        }

        IButton button = new IButton( uuid, profile, x, y, null );
        button.update( json );

        return button;
    }

    private final @NotNull UUID uuid;
    private final @NotNull UUID profile;
    private final int posX;
    private final int posY;
    private final @NotNull IButtonBackground back;
    private final @NotNull IButtonForeground front;
    private @Nullable Task task;

    IButton( @NotNull UUID uuid,
             @NotNull UUID profile,
             int posX, int posY,
             @Nullable Task task ) {
        this.uuid = uuid;
        this.profile = profile;
        this.back = new IButtonBackground( this );
        this.front = new IButtonForeground( this );
        this.posX = posX;
        this.posY = posY;
        this.task = task;

        setForeground( TRANSPARENT );

        setLayout( new OverlayLayout( this ) );
        add( front, 0 );
        add( back, 1 );
    }

    public IButton( @NotNull UUID profile, int posX, int posY ) {
        this( UUID.randomUUID(), profile, posX, posY, null );
    }

    public void setTask( @Nullable Task task ) {
        if (Objects.equals( this.task, task ))
            return;
        logAndSendUpdate( "task", this.task, task );
        this.task = task;
    }

    @Override
    public void logAndSendUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {EventLogging.DefaultImpls.logAndSendUpdate( this, property, oldValue, newValue );}

    @Override
    public void remove() {MenuProperty.remove( this );}

    @NotNull
    @Override
    public TargetedRequest.Target getTarget() {return TargetedRequest.Target.BUTTON;}

    @Override
    public void logUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {Log.buttonUpdate( uuid, property, oldValue, newValue );}

    @Override
    public void sendUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {LiveComponent.DefaultImpls.sendUpdate( this, property, oldValue, newValue );}

    @Override
    public void update( @NotNull JsonObject json ) {
        if (json.has( "icon" ))
            back.update( json.getAsJsonObject( "icon" ) );
        if (json.has( "label" ))
            front.update( json.getAsJsonObject( "label" ) );
        if (json.has( "task" ))
            task = Task.fromJson( json.getAsJsonObject( "task" ) );
    }

    @NotNull
    @Override
    public JsonObject toRequest() {
        JsonObject json = serialize();
        json.add( "icon", back.toRequest() );
        return json;
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

        JsonObject json = new JsonObject();

        json.addProperty( "uuid", uuid.toString() );
        json.addProperty( "x", posX );
        json.addProperty( "y", posY );
        if (task != null)
            json.add( "task", task.serialize() );
        json.add( "icon", back.serialize() );
        json.add( "label", front.serialize() );

        return json;
    }
}