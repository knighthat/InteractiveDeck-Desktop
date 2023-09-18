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

package me.knighthat.interactivedeck.menus.modifier;

import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.input.HexColorTextField;
import me.knighthat.interactivedeck.menus.popup.WarningPopup;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class IconModifier extends ModifierPanel {

    private HexColorTextField bdInput;
    private HexColorTextField bgInput;
    private HexColorTextField fgInput;

    private void updateColor( @NotNull HexColorTextField target, @NotNull Color color ) {
        Color contrast = ColorUtils.getContrast( color );
        String bgHex = ColorUtils.toHex( color );
        target.setBackground( color );
        target.setForeground( contrast );
        target.setText( bgHex );
    }

    private void applyColor( @NotNull Object source ) {
        if (!( source instanceof HexColorTextField input ))
            return;

        String hexCode = input.getText().trim();
        if (hexCode.length() < 7) {
            WarningPopup.showWarning( "Hex string must have 7 digits of 0-9 a-f A-F" );
            return;
        }

        Color color = ColorUtils.fromHex( hexCode );
        if (input == fgInput)
            button.foreground( color );
        else if (input == bgInput)
            button.background( color );
        else
            button.border( color );

        updateSelectedButton( button );
    }

    private void addContent( @NotNull String name, @NotNull HexColorTextField field, int gridy ) {
        addContent(
                new JLabel( name ),
                label -> {
                    label.setForeground( Color.WHITE );
                    label.setFont( SETTINGS.UIFont() );
                },
                constraints -> {
                    constraints.gridy = gridy;
                    constraints.anchor = GridBagConstraints.LINE_START;
                }
        );
        addContent(
                field,
                comp -> {
                    setDimension( comp, 210, 40 );
                    comp.addFocusListener( new FocusAdapter() {
                        @Override
                        public void focusLost( FocusEvent e ) {
                            applyColor( e.getSource() );
                        }
                    } );
                    comp.addKeyListener( new KeyAdapter() {
                        @Override
                        public void keyPressed( KeyEvent e ) {
                            if (e.getKeyCode() != 10)
                                return;
                            applyColor( e.getSource() );
                        }
                    } );
                },
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets( 0, 0, 40, 0 );
                }
        );
    }

    @Override
    protected void configureLayout( @NotNull GridBagLayout layout ) {
        layout.columnWidths = new int[]{ 150 };
    }

    @Override
    protected void loadContent() {
        this.fgInput = new HexColorTextField();
        addContent( "Foreground", fgInput, 0 );
        this.bgInput = new HexColorTextField();
        addContent( "Background", bgInput, 2 );
        this.bdInput = new HexColorTextField();
        addContent( "Border", bdInput, 4 );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        Color background = this.button.background();
        updateColor( bgInput, background );

        Color foreground = this.button.foreground();
        updateColor( fgInput, foreground );

        Color border = this.button.border();
        updateColor( bdInput, border );
    }
}
