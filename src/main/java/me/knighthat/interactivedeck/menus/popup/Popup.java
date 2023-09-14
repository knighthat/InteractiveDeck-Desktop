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

public abstract class Popup extends JDialog {

    protected JPanel contentContainer;
    protected JPanel buttonContainer;

    public Popup( @NotNull Window window, @NotNull String title ) {
        super( window );

        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setBackground( new Color( 51, 51, 51 ) );

        getRootPane().setWindowDecorationStyle( JRootPane.NONE );
        setUndecorated( true );

        setupTitle( title );
        setupContentContainer();
        setupButtonContainer();
    }

    protected abstract void loadContent();

    protected abstract void loadButtons();

    private void setupTitle( @NotNull String title ) {
        JLabel label = new JLabel( title );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        setDimension( label, 300, 30 );
        getContentPane().add( label, BorderLayout.PAGE_START );
    }

    private void setupContentContainer() {
        this.contentContainer = new JPanel();
        contentContainer.setOpaque( false );
        contentContainer.setLayout( new GridBagLayout() );
    }

    private void setupButtonContainer() {
        this.buttonContainer = new JPanel();
        setDimension( buttonContainer, 300, 50 );
        buttonContainer.setOpaque( false );

        FlowLayout layout = new FlowLayout( FlowLayout.RIGHT, 20, 15 );
        layout.setAlignOnBaseline( true );
        buttonContainer.setLayout( layout );
    }

    protected void finish() {
        setVisible( false );
    }

    public @NotNull JButton addButton( @NotNull Consumer<JButton> consumer ) {
        JButton button = new JButton();
        setDimension( button, 80, 25 );
        consumer.accept( button );
        buttonContainer.add( button );

        return button;
    }

    public @NotNull JComponent addContent( @NotNull JComponent component, @NotNull Consumer<JComponent> conComp, @NotNull Consumer<GridBagConstraints> conCons ) {
        GridBagConstraints constraints = new GridBagConstraints();
        conCons.accept( constraints );
        conComp.accept( component );
        contentContainer.add( component, constraints );

        return component;
    }

    public void setDimension( @NotNull JComponent component, int width, int height ) {
        Dimension dimension = new Dimension( width, height );
        component.setMaximumSize( dimension );
        component.setMinimumSize( dimension );
        component.setPreferredSize( dimension );
    }

    public void present() {
        loadContent();
        getContentPane().add( contentContainer, BorderLayout.CENTER );
        loadButtons();
        getContentPane().add( buttonContainer, BorderLayout.SOUTH );
        setVisible( true );
        pack();

        setLocationRelativeTo( getOwner() );
    }
}
