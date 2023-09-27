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

import me.knighthat.interactivedeck.component.ContentContainer;
import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.ui.UIButton;
import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.menus.Interactive;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public abstract class Popup extends JDialog implements Flexible, ContentContainer<GridBagConstraints>, Interactive<GridBagLayout> {

    protected final @NotNull JPanel contentContainer;
    protected final @NotNull JPanel buttonContainer;

    public Popup( @NotNull Window window, @NotNull String title ) {
        super( window );

        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setBackground( new Color( 51, 51, 51 ) );

        getRootPane().setWindowDecorationStyle( JRootPane.NONE );
        setUndecorated( true );

        setupTitle( title );
        this.contentContainer = new JPanel();
        setupContentContainer();
        this.buttonContainer = new JPanel();
        setupButtonContainer();
    }

    protected abstract void loadButtons();

    private void setupTitle( @NotNull String title ) {
        UILabel label = new UILabel( title );
        label.setForeground( Color.BLACK );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        setDimension( label, 300, 30 );
        getContentPane().add( label, BorderLayout.PAGE_START );
    }

    private void setupContentContainer() {
        contentContainer.setOpaque( false );
        contentContainer.setLayout( new GridBagLayout() );
    }

    private void setupButtonContainer() {
        setDimension( buttonContainer, 300, 50 );
        buttonContainer.setOpaque( false );

        FlowLayout layout = new FlowLayout( FlowLayout.RIGHT, 20, 15 );
        layout.setAlignOnBaseline( true );
        buttonContainer.setLayout( layout );
    }

    protected void finish() {
        setVisible( false );
        dispose();
    }

    public void addButton( @NotNull Consumer<UIButton> consumer ) {
        UIButton button = new UIButton();
        setDimension( button, 80, 25, true, true, false );
        consumer.accept( button );
        buttonContainer.add( button );
    }

    public void present() {
        initComponents();
        getContentPane().add( contentContainer, BorderLayout.CENTER );
        loadButtons();
        getContentPane().add( buttonContainer, BorderLayout.SOUTH );
        setVisible( true );
        pack();

        setLocationRelativeTo( getOwner() );
    }

    @Override
    public void setupLayout( @NotNull GridBagLayout layout ) {
    }

    @Override
    public @NotNull JPanel container() {
        return contentContainer;
    }

    @Override
    public @NotNull GridBagConstraints constraints() {
        return new GridBagConstraints();
    }
}
