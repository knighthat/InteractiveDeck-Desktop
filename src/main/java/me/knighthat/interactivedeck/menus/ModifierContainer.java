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
package me.knighthat.interactivedeck.menus;

import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.icon.Icons;
import me.knighthat.interactivedeck.component.tab.TabUI;
import me.knighthat.interactivedeck.menus.modifier.IconModifierPanel;
import me.knighthat.interactivedeck.menus.modifier.TaskModifierPanel;
import me.knighthat.interactivedeck.menus.modifier.TextModifierPanel;
import me.knighthat.interactivedeck.svg.SVGParser;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author knighthat
 */
public class ModifierContainer extends JPanel {

    private final @NotNull IButton selected;

    public ModifierContainer( @NotNull IButton selected ) {
        this.selected = selected;

        initComponents();
    }

    private void initComponents() {
        TextModifierPanel textModifier = new TextModifierPanel( selected );
        IconModifierPanel iconModifier = new IconModifierPanel( selected );
        TaskModifierPanel taskModifier = new TaskModifierPanel( selected );
        var modifierTabbedPane = new JTabbedPane() {
            public void addTab( @NotNull SVGDocument svg, @NotNull Component component ) {
                svg.getRootElement().setAttribute( "width", "48px" );
                svg.getRootElement().setAttribute( "height", "28px" );
                BufferedImage bufferedImage = SVGParser.toBufferedImage( svg );
                super.addTab( "", new ImageIcon( bufferedImage ), component );
            }
        };

        Dimension dimension = new Dimension( 250, 520 );
        setMaximumSize( dimension );
        setMinimumSize( dimension );
        setPreferredSize( dimension );
        setOpaque( false );
        setLayout( new BorderLayout() );

        modifierTabbedPane.setUI( new TabUI() );
        modifierTabbedPane.addTab( Icons.INTERNAL.TAB_TEXT, textModifier );
        modifierTabbedPane.addTab( Icons.INTERNAL.TAB_ICON, iconModifier );
        modifierTabbedPane.addTab( Icons.INTERNAL.TAB_TASK, taskModifier );

        add( modifierTabbedPane, BorderLayout.PAGE_START );
    }
}
