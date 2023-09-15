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

import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.netstatus.NetStatus;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

public class ConnectionStatusSection extends JPanel implements Flexible {

    private final @NotNull NotificationCenter notificationCenter;
    //    private final @NotNull ConStatus connectionStatus;

    public ConnectionStatusSection() {
        this.notificationCenter = new NotificationCenter();
        //        this.connectionStatus = Connection.component();

        setBackground( ColorUtils.DEFAULT_DARK );
        setDimension( this, 1000, 30 );
        setLayout( new GroupLayout( this ) );

        setupLayout();
    }

    // <editor-fold default-state="collapsed" desc="Setup Layout">
    private void setupLayout() {
        GroupLayout layout = (GroupLayout) getLayout();

        GroupLayout.ParallelGroup horizontalParallelG = layout.createParallelGroup( LEADING );
        horizontalParallelG
                .addGroup(
                        layout
                                .createSequentialGroup()
                                .addGap( 30 )
                                .addComponent( NetStatus.ICON )
                                .addGap( 5 )
                                .addComponent( NetStatus.LABEL )
                                .addPreferredGap( RELATED, 349, Short.MAX_VALUE )
                                .addComponent( notificationCenter )
                                .addContainerGap()
                );
        layout.setHorizontalGroup( horizontalParallelG );

        GroupLayout.ParallelGroup verticalParallelG = layout.createParallelGroup( LEADING );
        verticalParallelG
                .addGroup(
                        layout
                                .createSequentialGroup()
                                .addGap( 5 )
                                .addComponent( NetStatus.ICON )
                )
                .addComponent( NetStatus.LABEL )
                .addComponent( notificationCenter );
        layout.setVerticalGroup( verticalParallelG );
    }
    // </editor-fold>

}
