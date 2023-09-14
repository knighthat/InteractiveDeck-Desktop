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

package me.knighthat.interactivedeck.component.tab;

import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class TabUI extends BasicTabbedPaneUI {

    private void paintTransparent( @NotNull Graphics g, int x, int y, int w, int h ) {
        g.setColor( ColorUtils.TRANSPARENT );
        g.fillRect( x, y, w, h );
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new SpacedLayout();
    }

    @Override
    protected void paintTabBorder( Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected ) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor( Color.WHITE );
        g2d.drawLine( x, y + 10, x, y + h - 2 );                              // Left Post
        g2d.drawLine( x + w - 1, y + 10, x + w - 1, y + h - 2 );        // Right Post
        g2d.drawLine( x + 10, y, x + w - 11, y );                              // Top Post
        QuadCurve2D leftCurve = new QuadCurve2D.Float(
                x, y + 10,
                x, y,
                x + 10, y
        );
        g2d.draw( leftCurve );
        QuadCurve2D rightCurve = new QuadCurve2D.Float(
                x + w - 11, y,
                x + w - 1, y,
                x + w - 1, y + 10
        );
        g2d.draw( rightCurve );
    }

    @Override
    protected void paintContentBorderLeftEdge( Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h ) {
        paintTransparent( g, x, y, w, h );
    }

    @Override
    protected void paintContentBorderRightEdge( Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h ) {
        paintTransparent( g, x, y, w, h );
    }

    private class SpacedLayout extends BasicTabbedPaneUI.TabbedPaneLayout {

        private static final int GAP = 5;

        @Override
        protected void calculateTabRects( int tabPlacement, int tabCount ) {
            super.calculateTabRects( tabPlacement, tabCount );

            for (int i = 0 ; i < rects.length ; i++)
                rects[i].x += i * GAP;
        }
    }
}
