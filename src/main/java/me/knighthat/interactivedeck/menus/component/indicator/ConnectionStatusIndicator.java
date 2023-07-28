/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck.menus.component.indicator;

import me.knighthat.interactivedeck.utils.Status;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author knighthat
 */
public class ConnectionStatusIndicator extends JComponent {

    private final @NotNull Indicator indicator = new Indicator();
    private final @NotNull Label label = new Label();
    private @NotNull Status status = Status.UNKNOWN;

    public ConnectionStatusIndicator() {
        setBackground(new Color(36, 36, 36));
        setForeground(new Color(255, 255, 255));
    }

    public void initComponents() {
        indicator.setBounds(0, 0, 15, 30);
        label.setBounds(15, 0, 100, 30);

        add(indicator);
        add(label);
    }

    public void status( @NotNull Status status ) {
        this.status = status;
        indicator.setBackground(status.getColor());
        label.setText(status.getStatus());
        label.setForeground(status.getColor());
    }

    public @NotNull Status status() {
        return this.status;
    }
}

class Indicator extends JComponent {

    public Indicator() {
        Dimension dimension = new Dimension(15, 30);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }

    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(getBackground());

        // Indicator's Diameter
        int d = 10;
        int r = d / 2;
        int wStart = (int) Math.floor(( getWidth() / 2 ) - r);
        int hStart = (int) Math.floor(( getHeight() / 2 ) - r);

        graphics.fillOval(wStart, hStart, d, d);
    }
}

class Label extends JLabel {

    private final @NotNull Font font = new Font("Open Sans", 0, 14);

    public Label() {
        Dimension dimension = new Dimension(100, 30);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        setFont(font);
    }
}