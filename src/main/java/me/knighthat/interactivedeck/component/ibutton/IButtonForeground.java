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
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.FontUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public final class IButtonForeground extends BChild {

    IButtonForeground( @NotNull IButton owner ) {
        super( owner );
        setHorizontalAlignment( JLabel.CENTER );
        setForeground( Color.WHITE );
        setFont( SETTINGS.defaultButtonFont() );
    }

    public void update( @Nullable JsonObject json ) {
        if (json == null)
            return;

        String text = json.get( "text" ).getAsString();
        setText( text );

        Color foreground = ColorUtils.fromJson( json.get( "foreground" ) );
        setForeground( foreground );

        JsonObject fontJson = json.getAsJsonObject( "font" );
        Font font = FontUtils.fromJson( fontJson );
        setFont( font );
    }

    public void text( @NotNull String text ) {
        final String newText = text.strip();
        String oldText = getText().strip();
        if (newText.equals( oldText ))
            return;

        setText( newText );
        Log.buttonUpdate( owner.uuid, "text", oldText, newText );
        sendUpdate( json -> json.addProperty( "text", newText ) );
    }

    public void fontColor( @NotNull Color color ) {
        Color oldColor = getForeground();
        if (color.equals( oldColor ))
            return;

        sendAndLog( "foreground", oldColor, color );
    }

    public void font( @NotNull Font font ) {
        Font oldFont = getFont();
        if (font.getFamily().equals( oldFont.getFamily() ) &&
                font.getStyle() == oldFont.getStyle() &&
                font.getSize() == oldFont.getSize())
            return;

        super.setFont( font );

        String fontFormat = "[f=%s,s=%s,w=%s]";
        String oldFontStr = fontFormat.formatted( oldFont.getFamily(), oldFont.getSize(), oldFont.getStyle() );
        String newFont = fontFormat.formatted( font.getFamily(), font.getSize(), font.getStyle() );
        Log.buttonUpdate( owner.uuid, "font", oldFontStr, newFont );
        sendUpdate( json -> json.add( "font", FontUtils.toJson( getFont() ) ) );
    }

    @Override
    public @NotNull JsonObject serialize() {
        /* Template
         * {
         *      "text": $text,
         *      "color": [r, g, b],
         *      "font":
         *      {
         *          "name": $family,
         *          "weight": $style,
         *          "size": $size,
         *      }
         * }
         */
        JsonObject json = new JsonObject();
        json.addProperty( "text", getText() );
        json.add( "foreground", ColorUtils.toJson( getForeground() ) );
        json.add( "font", FontUtils.toJson( getFont() ) );

        return json;
    }
}
