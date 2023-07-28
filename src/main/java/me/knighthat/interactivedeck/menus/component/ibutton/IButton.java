/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.menus.component.ibutton;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static me.knighthat.interactivedeck.utils.ColorConverter.TRANSPARENT;
import static me.knighthat.interactivedeck.vars.Settings.SELECTED_COLOR;

public class IButton extends JComponent {

    private final @NotNull BIcon icon;
    private final @NotNull BLabel label;

    private boolean isSelected = false;

    {
        icon = new BIcon();
        label = new BLabel("Placeholder");
    }

    public IButton() {
        setOpaque(false);
        setForeground(TRANSPARENT);

        setLayout(new OverlayLayout(this));
        add(label, 0);
        add(icon, 1);
    }

    public void select() {
        this.isSelected = true;
        this.icon.repaint(SELECTED_COLOR, this.icon.inner());
    }

    public void unselect() {
        this.isSelected = false;
        this.icon.repaint(this.icon.inner(), this.icon.inner());
    }

    public void background( @NotNull Color bg ) {
        this.icon.repaint(null, bg);
    }


    public @NotNull Color background() {
        return this.icon.inner();
    }

    public void foreground( @NotNull Color fg ) {
        this.label.setForeground(fg);
    }

    public @NotNull Color foreground() {
        return this.label.getForeground();
    }

    public void text( @NotNull String text ) {
        this.label.text(text);
    }

    public @NotNull String text() {
        return this.label.text();
    }
}

class BChild extends JComponent {

    public BChild() {
        Dimension dimension = new Dimension(120, 120);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        setOpaque(false);
    }
}
