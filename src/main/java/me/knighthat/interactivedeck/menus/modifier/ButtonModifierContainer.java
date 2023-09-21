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
import me.knighthat.interactivedeck.component.tab.TabUI;
import me.knighthat.interactivedeck.svg.SVGParser;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ButtonModifierContainer extends JPanel implements IButtonProperty {

    private final @NotNull SVGIconTabbedPane pane;
    private final @NotNull TextModifier textModifier;
    private final @NotNull IconModifier iconModifier;
    private final @NotNull TaskModifier taskModifier;

    public ButtonModifierContainer() {
        this.pane = new SVGIconTabbedPane();
        this.textModifier = new TextModifier();
        this.iconModifier = new IconModifier();
        this.taskModifier = new TaskModifier();
        initComponents();
        pane.setVisible( false );
    }

    private void initComponents() {
        Dimension dimension = new Dimension( 250, 520 );
        setMaximumSize( dimension );
        setMinimumSize( dimension );
        setPreferredSize( dimension );
        setBackground( ColorUtils.DEFAULT_DARK );
        setLayout( new BorderLayout() );

        pane.setUI( new TabUI() );
        pane.addTab( Icons.INTERNAL.TAB_TEXT, textModifier );
        pane.addTab( Icons.INTERNAL.TAB_ICON, iconModifier );
        pane.addTab( Icons.INTERNAL.TAB_TASK, taskModifier );

        add( pane, BorderLayout.PAGE_START );
    }

    public void updateSelectedButton( @NotNull IButton button ) {
        textModifier.updateSelectedButton( button );
        iconModifier.updateSelectedButton( button );
        taskModifier.updateSelectedButton( button );
        setVisible( true );
    }

    @Override
    public void setVisible( boolean aFlag ) {
        pane.setVisible( aFlag );
    }

    private class SVGIconTabbedPane extends JTabbedPane {
        public void addTab( @NotNull SVGDocument svg, @NotNull Component component ) {
            svg.getRootElement().setAttribute( "width", "48px" );
            svg.getRootElement().setAttribute( "height", "28px" );
            BufferedImage bufferedImage = SVGParser.toBufferedImage( svg );
            super.addTab( "", new ImageIcon( bufferedImage ), component );
        }
    }
}
