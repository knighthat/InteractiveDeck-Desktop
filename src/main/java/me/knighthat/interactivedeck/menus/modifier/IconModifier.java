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
import me.knighthat.interactivedeck.component.ui.IconLabel;
import me.knighthat.interactivedeck.component.ui.UILabel;
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

public class IconModifier extends ModifierPanel<GridBagLayout, GridBagConstraints> {

    private final @NotNull BufferedImage bufferedIcon;
    private final @NotNull HexColorTextField bdInput;
    private final @NotNull HexColorTextField bgInput;
    private final @NotNull HexColorTextField fgInput;
    private final @NotNull Insets sectionSpacing;

    public IconModifier() {
        SVGDocument document = Icons.INTERNAL.COLOR_PALETTE;
        Element root = document.getRootElement();
        root.setAttribute( "width", "25px" );
        root.setAttribute( "height", "25px" );
        this.bufferedIcon = SVGParser.toBufferedImage( document );

        this.bdInput = new HexColorTextField();
        this.bgInput = new HexColorTextField();
        this.fgInput = new HexColorTextField();

        this.sectionSpacing = new Insets( 0, 0, 40, 0 );
    }

    private void addSection( @NotNull String name, @NotNull HexColorTextField input, int gridy ) {
        addContent(
                new UILabel( name ),
                label -> ( (JLabel) label ).setLabelFor( input ),
                constraints -> {
                    constraints.gridy = gridy;
                    constraints.anchor = GridBagConstraints.LINE_START;
                }
        );
        addContent(
                input,
                c -> {
                    setDimension( c, 150, 40 );
                    input.addColorChangeEvent( event -> applyColor( input ) );
                },
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = sectionSpacing;
                }
        );
        addContent(
                new IconLabel( bufferedIcon, SwingConstants.RIGHT, SwingConstants.CENTER ),
                icon -> {
                    setDimension( icon, 40, 40 );
                    icon.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            ColorPallet.INSTANCE.present( input );
                        }
                    } );
                },
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.insets = sectionSpacing;
                }
        );
    }

    private void applyColor( @NotNull HexColorTextField input ) {
        Color color = input.getBackground();
        if (input == fgInput)
            button.foreground( color );
        else if (input == bgInput)
            button.background( color );
        else
            button.border( color );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        bgInput.setColor( button.background() );
        fgInput.setColor( button.foreground() );
        bdInput.setColor( button.border() );
    }

    @Override
    protected @NotNull GridBagLayout initLayout() {
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{ 150, 40 };
        return layout;
    }

    @Override
    public @NotNull GridBagConstraints constraints() {
        return new GridBagConstraints();
    }

    @Override
    public void initComponents() {
        addSection( "Foreground", fgInput, 0 );
        addSection( "Background", bgInput, 2 );
        addSection( "Border", bdInput, 4 );
    }
}
