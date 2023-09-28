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

package me.knighthat.interactivedeck.component.icon;

import me.knighthat.interactivedeck.component.ui.IconLabel;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;

public class TaskIcon extends IconLabel {

    static Dimension borderRadius;

    private final @NotNull Class<? extends Task> taskType;

    private boolean isSelected = false;

    public <T extends Task> TaskIcon( @NotNull SVGDocument document, @NotNull Class<T> taskType ) {
        super( document, 25, 25, SwingConstants.CENTER, SwingConstants.CENTER );
        borderRadius = new Dimension( 15, 15 );
        this.taskType = taskType;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelect( boolean b ) {
        this.isSelected = b;
        repaint();
    }

    public void toggleSelect() {
        isSelected = !isSelected;
        repaint();
    }

    public @NotNull Class<?> taskType() {
        return this.taskType;
    }

    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );

        int width = getWidth() - 1;
        int height = getHeight() - 1;

        g.setColor( ColorUtils.TRANSPARENT );
        g.drawRect( 0, 0, width, height );

        if (!isSelected)
            return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor( ColorUtils.fromHex( "#6c757d" ) );
        g2d.drawRoundRect( 0, 0, width, height, borderRadius.width, borderRadius.height );
    }
}
