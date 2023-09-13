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

package me.knighthat.interactivedeck.menus.popup;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public abstract class PopupMenu extends JDialog {

    protected final @NotNull JPanel content;
    private final @NotNull JPanel buttonContainer;

    public PopupMenu( @NotNull Window owner, @NotNull String title ) {
        super( owner );

        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setBackground( new Color( 51, 51, 51 ) );

        getRootPane().setWindowDecorationStyle( JRootPane.NONE );
        setUndecorated( true );

        title( title );

        this.content = new JPanel();
        content.setOpaque( false );
        content.setLayout( new GridBagLayout() );
        initContent();
        getContentPane().add( content, BorderLayout.CENTER );

        this.buttonContainer = new JPanel();
        initButtons();
        getContentPane().add( buttonContainer, BorderLayout.SOUTH );

        pack();
        setLocationRelativeTo( owner );
    }

    abstract void initContent();

    void title( @NotNull String title ) {
        JLabel label = new JLabel( title );

        label.setHorizontalAlignment( SwingConstants.CENTER );

        Dimension dimension = new Dimension( 300, 30 );
        label.setMaximumSize( dimension );
        label.setMinimumSize( dimension );
        label.setPreferredSize( dimension );

        getContentPane().add( label, BorderLayout.PAGE_START );
    }

    void initButtons() {
        Dimension dimension = new Dimension( 300, 50 );
        buttonContainer.setMaximumSize( dimension );
        buttonContainer.setMinimumSize( dimension );
        buttonContainer.setPreferredSize( dimension );

        buttonContainer.setOpaque( false );

        FlowLayout layout = new FlowLayout( FlowLayout.RIGHT, 20, 15 );
        layout.setAlignOnBaseline( true );
        buttonContainer.setLayout( layout );
    }

    void addButton( @NotNull Consumer<JButton> consumer ) {
        JButton button = new JButton();

        Dimension dimension = new java.awt.Dimension( 80, 25 );
        button.setMaximumSize( dimension );
        button.setMinimumSize( dimension );
        button.setPreferredSize( dimension );

        consumer.accept( button );
        buttonContainer.add( button );
    }

    @NotNull JComponent addContent( @NotNull JComponent component, @NotNull Consumer<JComponent> conComp, @NotNull Consumer<GridBagConstraints> conCons ) {
        GridBagConstraints constraints = new GridBagConstraints();
        conCons.accept( constraints );
        conComp.accept( component );
        content.add( component, constraints );

        return component;
    }

    void setDimension( @NotNull JComponent component, int width, int height ) {
        Dimension dimension = new Dimension( width, height );
        component.setMaximumSize( dimension );
        component.setMinimumSize( dimension );
        component.setPreferredSize( dimension );
    }

    void finish() {
        this.setVisible( false );
    }
}
