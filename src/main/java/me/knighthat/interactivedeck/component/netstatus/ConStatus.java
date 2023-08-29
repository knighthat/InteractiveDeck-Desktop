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
package me.knighthat.interactivedeck.component.netstatus;

import me.knighthat.interactivedeck.connection.Status;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ConStatus extends JComponent {

    static final @NotNull StatusIndicator INDICATOR;
    static final @NotNull StatusLabel LABEL;

    static {
        INDICATOR = new StatusIndicator();
        INDICATOR.setBounds( 0, 0, 15, 30 );

        LABEL = new StatusLabel();
        LABEL.setBounds( 15, 0, 100, 30 );
    }

    public ConStatus() {
        Dimension cDimension = new Dimension( 115, 30 );
        setPreferredSize( cDimension );
        setMinimumSize( cDimension );
        setMaximumSize( cDimension );
        setOpaque( false );

        add( INDICATOR );
        add( LABEL );
    }

    public void update( @NotNull Status status ) {
        INDICATOR.setBackground( status.getColor() );
        LABEL.setText( status.getStatus() );
        LABEL.setForeground( status.getColor() );
    }
}
