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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.connection.request.TargetedRequest;
import me.knighthat.interactivedeck.connection.request.UpdateRequest;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

abstract class BChild extends JLabel implements JsonSerializable {

    private static final @NotNull Dimension DIMENSION = new Dimension( 120, 120 );

    @NotNull IButton owner;

    public BChild( @NotNull IButton owner ) {
        this.owner = owner;

        setPreferredSize( DIMENSION );
        setMinimumSize( DIMENSION );
        setMaximumSize( DIMENSION );

        setOpaque( false );
    }

    protected void sendUpdate( @NotNull Consumer<JsonObject> consumer ) {
        JsonObject json = new JsonObject();
        consumer.accept( json );

        new UpdateRequest(
                TargetedRequest.Target.BUTTON,
                owner.uuid,
                json
        ).send();
    }

    protected @NotNull Color colorFromJson( @NotNull JsonObject json, String property ) {
        JsonArray array = json.getAsJsonArray( property );
        return ColorUtils.fromJson( array );
    }

    protected void sendAndLog( @NotNull String property, @NotNull Color oldColor, @NotNull Color newColor ) {
        Log.buttonUpdate(
                owner.uuid,
                property,
                ColorUtils.toHex( oldColor ),
                ColorUtils.toHex( newColor )
        );
        sendUpdate( json -> json.add( property, ColorUtils.toJson( newColor ) ) );
    }
}
