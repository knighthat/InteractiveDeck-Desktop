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

package me.knighthat.interactivedeck.component.input;

import me.knighthat.interactivedeck.menus.popup.WarningPopup;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HexColorTextField extends JFormattedTextField {

    private final @NotNull List<ColorChangedEvent> events = new ArrayList<>();

    private @NotNull Color color = Color.WHITE;

    public HexColorTextField() {
        super();
        setupFormatter();
        addDefaultEventListeners();
    }

    private void setupFormatter() {
        try {
            MaskFormatter formatter = new MaskFormatter( "'#HHHHHH" );

            setFormatterFactory( new DefaultFormatterFactory( formatter ) );
            setColumns( 7 );
        } catch (ParseException e) {
            Log.wexc( "Failed to parse HEX string", e, false );
        }
    }

    private void addDefaultEventListeners() {
        addFocusListener( new FocusAdapter() {
            @Override
            public void focusLost( FocusEvent e ) {
                updateColor();
            }
        } );
        addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent e ) {
                if (e.getKeyCode() == 10)
                    updateColor();
            }
        } );
    }

    private void fireColorChangedEvent( @NotNull Color color ) {
        events.forEach( e -> e.onColorChanged( color ) );
    }

    /**
     * Sets background, foreground and border color based on input color.<br>
     * Unlike {@link #updateColor()} or {@link #updateColor(Color)}, this method does not trigger
     * {@link ColorChangedEvent}
     *
     * @param color new color
     */
    public void setColor( @NotNull Color color ) {
        this.color = color;
        setBackground( color );
        setForeground( ColorUtils.getContrast( color ) );
        setText( ColorUtils.toHex( color ) );
    }

    /**
     * Extracts HEX value from {@link #getText()} and validates before
     * settings background, foreground, and border color. Finally, triggers {@link ColorChangedEvent}.<br>
     * If {@link ColorChangedEvent} is undesirable, use {@link #setColor(Color)}.<br>
     * Or use {@link #updateColor(Color)} if different color is preferred.
     */
    public void updateColor() {
        String hex = getText().trim();
        if (hex.length() < 7) {
            String warning = "Hex string must have 7 digits of 0-9 a-f A-F";
            WarningPopup.showWarning( warning );
            setColor( color );
            return;
        }
        Color color = ColorUtils.fromHex( hex );
        setColor( color );

        fireColorChangedEvent( color );
    }

    /**
     * Sets background, foreground and border color based
     * on input color, then triggers {@link ColorChangedEvent}.<br>
     *
     * @param color new color
     */
    public void updateColor( @NotNull Color color ) {
        setColor( color );
        fireColorChangedEvent( color );
    }

    public void addColorChangeEvent( @NotNull ColorChangedEvent event ) {
        this.events.add( event );
    }
}

