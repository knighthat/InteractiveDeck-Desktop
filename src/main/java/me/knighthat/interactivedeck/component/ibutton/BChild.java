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

import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.lib.component.LiveComponent;
import me.knighthat.lib.connection.request.TargetedRequest;
import me.knighthat.lib.json.JsonSerializable;
import me.knighthat.lib.logging.EventLogging;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

abstract class BChild extends JLabel implements JsonSerializable, LiveComponent {

    private static final @NotNull Dimension DIMENSION = new Dimension( 120, 120 );

    @NotNull IButton owner;

    public BChild( @NotNull IButton owner ) {
        this.owner = owner;

        setPreferredSize( DIMENSION );
        setMinimumSize( DIMENSION );
        setMaximumSize( DIMENSION );

        setOpaque( false );
    }

    @NotNull
    @Override
    public UUID getUuid() {return owner.getUuid();}

    @NotNull
    @Override
    public TargetedRequest.Target getTarget() {return TargetedRequest.Target.BUTTON;}

    @Override
    public void sendUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {LiveComponent.DefaultImpls.sendUpdate( this, property, oldValue, newValue );}

    @Override
    public void logAndSendUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {
        if (oldValue instanceof Color)
            oldValue = ColorUtils.toJson( (Color) oldValue );
        if (newValue instanceof Color)
            newValue = ColorUtils.toJson( (Color) newValue );

        EventLogging.DefaultImpls.logAndSendUpdate( this, property, oldValue, newValue );
    }

    @Override
    public void logUpdate( @NotNull String property, @Nullable Object oldValue, @Nullable Object newValue ) {Log.buttonUpdate( getUuid(), property, oldValue, newValue );}
}
