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

import me.knighthat.interactivedeck.component.Flexible;
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

public class ButtonModifierContainer extends JPanel implements IButtonProperty, Flexible {

    private final @NotNull SVGIconTabbedPane tabbedPane;

    private final @NotNull ModifierPanel textModifier;
    private final @NotNull ModifierPanel iconModifier;
    private final @NotNull ModifierPanel taskModifier;

    public ButtonModifierContainer() {
        this.tabbedPane = new SVGIconTabbedPane();
        this.textModifier = new TextModifier();
        this.iconModifier = new IconModifier();
        this.taskModifier = new TaskModifier();
        initComponents();
    }

    private void initComponents() {
        setDimension( this, 250, 520 );
        setBackground( ColorUtils.DEFAULT_DARK );
        setLayout( new BorderLayout() );

        tabbedPane.setUI( new TabUI() );
        tabbedPane.addTab( Icons.INTERNAL.TAB_TEXT, textModifier );
        tabbedPane.addTab( Icons.INTERNAL.TAB_ICON, iconModifier );
        tabbedPane.addTab( Icons.INTERNAL.TAB_TASK, taskModifier );

        add( tabbedPane, BorderLayout.PAGE_START );
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        textModifier.updateSelectedButton( button );
        iconModifier.updateSelectedButton( button );
        taskModifier.updateSelectedButton( button );
        setVisible( true );
    }

    @Override
    public void setVisible( boolean b ) {
        tabbedPane.setVisible( b );
    }

    private static class SVGIconTabbedPane extends JTabbedPane {
        public void addTab( @NotNull SVGDocument svg, @NotNull Component component ) {
            svg.getRootElement().setAttribute( "width", "48px" );
            svg.getRootElement().setAttribute( "height", "28px" );
            BufferedImage bufferedImage = SVGParser.toBufferedImage( svg );
            super.addTab( "", new ImageIcon( bufferedImage ), component );
        }
    }
}
