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
import me.knighthat.interactivedeck.component.icon.Icons;
import me.knighthat.interactivedeck.component.input.HexColorTextField;
import me.knighthat.interactivedeck.menus.popup.ColorPallet;
import me.knighthat.interactivedeck.svg.SVGParser;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class IconModifier extends ModifierPanel {

    private HexColorTextField bdInput;
    private HexColorTextField bgInput;
    private HexColorTextField fgInput;

    private void applyColor( @NotNull HexColorTextField input ) {
        Color color = input.getBackground();
        if (input == fgInput)
            button.foreground( color );
        else if (input == bgInput)
            button.background( color );
        else
            button.border( color );
    }

    private void addContent( @NotNull String name, @NotNull HexColorTextField field, int gridy, @NotNull BufferedImage icon ) {
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
        Insets insets = new Insets( 0, 0, 40, 0 );
        addContent(
                field,
                comp -> {
                    setDimension( comp, 150, 40 );
                    field.addColorChangeEvent( event -> applyColor( field ) );
                },
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = insets;
                }
        );
        addContent(
                new JLabel( new ImageIcon( icon ) ),
                comp -> {
                    setDimension( comp, 40, 40 );

                    ( (JLabel) comp ).setHorizontalAlignment( SwingConstants.RIGHT );
                    ( (JLabel) comp ).setVerticalAlignment( SwingConstants.CENTER );

                    comp.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            ColorPallet.INSTANCE.present( field );
                        }
                    } );
                },
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.anchor = GridBagConstraints.BASELINE;
                    constraints.insets = insets;
                }
        );
    }

    @Override
    protected void configureLayout( @NotNull GridBagLayout layout ) {
        layout.columnWidths = new int[]{ 150, 40 };
    }

    @Override
    protected void loadContent() {
        SVGDocument document = Icons.INTERNAL.COLOR_PALETTE;
        Element root = document.getRootElement();
        root.setAttribute( "width", "25px" );
        root.setAttribute( "height", "25px" );
        BufferedImage bufferedImage = SVGParser.toBufferedImage( document );

        this.fgInput = new HexColorTextField();
        addContent( "Foreground", fgInput, 0, bufferedImage );
        this.bgInput = new HexColorTextField();
        addContent( "Background", bgInput, 2, bufferedImage );
        this.bdInput = new HexColorTextField();
        addContent( "Border", bdInput, 4, bufferedImage );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        bgInput.setColor( button.background() );
        fgInput.setColor( button.foreground() );
        bdInput.setColor( button.border() );
    }
}
