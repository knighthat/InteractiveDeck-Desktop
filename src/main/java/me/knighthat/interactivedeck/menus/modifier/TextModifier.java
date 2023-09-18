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

package me.knighthat.interactivedeck.menus.modifier;

import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.font.FontFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class TextModifier extends ModifierPanel {

    private JTextField labelInput;
    private JComboBox<String> fontSelector;
    private JSpinner sizeSpinner;
    private JToggleButton boldButton;
    private JToggleButton italicButton;
    private JToggleButton underlineButton;

    private void applyLabel( @NotNull JComponent source ) {
        if (source instanceof JTextComponent textComponent)
            button.text( textComponent.getText() );
    }

    private void apply( @Nullable String family, @Nullable Integer style, @Nullable Integer size ) {
        Font curFont = button.font();
        button.font(
                new Font(
                        family == null ? curFont.getFamily() : family,
                        style == null ? curFont.getStyle() : style,
                        size == null ? curFont.getSize() : size
                )
        );
    }

    private void stylingButtonSelectedEvent( @NotNull ChangeEvent event ) {
        int bold = boldButton.isSelected() ? Font.BOLD : Font.PLAIN;
        int italic = italicButton.isSelected() ? Font.ITALIC : Font.PLAIN;
        apply( null, bold | italic, null );
    }

    private @NotNull JToggleButton addToggleButton( @NotNull String name, boolean isEnabled, int gridx ) {
        JToggleButton button = new JToggleButton( name );
        addContent(
                button,
                comp -> {
                    comp.setEnabled( isEnabled );
                    ( (JToggleButton) comp ).addChangeListener( this::stylingButtonSelectedEvent );
                },
                constraints -> {
                    constraints.gridx = gridx;
                    constraints.gridy = 3;
                }
        );
        return button;
    }

    private void addLabelInput() {
        addContent(
                labelInput = new JTextField(),
                input -> {
                    setDimension( input, 140, 40 );
                    input.addKeyListener( new KeyAdapter() {
                        @Override
                        public void keyPressed( KeyEvent e ) {
                            if (e.getKeyCode() == 10)
                                applyLabel( input );
                        }
                    } );
                    input.addFocusListener( new FocusAdapter() {
                        @Override
                        public void focusLost( FocusEvent e ) {
                            applyLabel( input );
                        }
                    } );
                },
                constraints -> {
                    constraints.gridy = 1;
                    constraints.gridwidth = 3;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.anchor = GridBagConstraints.NORTH;
                }
        );
    }

    private void addFontSelector() {
        addContent(
                fontSelector = new JComboBox<>( FontFactory.availableFamilyNames() ),
                selector -> {
                    setDimension( selector, 140, 30 );
                    ( (JComboBox<?>) selector ).addActionListener( event -> {
                        Object selected = fontSelector.getSelectedItem();
                        if (selected != null)
                            apply( selected.toString(), null, null );
                    } );
                },
                constraints -> {
                    constraints.gridy = 2;
                    constraints.gridwidth = 2;
                }
        );
    }

    private void addSizeSpinner() {
        addContent(
                sizeSpinner = new JSpinner(),
                spinner -> {
                    setDimension( spinner, 55, 30 );
                    ( (JSpinner) spinner ).addChangeListener( event -> {
                        Object value = sizeSpinner.getValue();
                        if (value instanceof Integer integer)
                            apply( null, null, integer );
                    } );
                },
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 2;
                    constraints.anchor = GridBagConstraints.LINE_END;
                }
        );
    }

    @Override
    protected void configureLayout( @NotNull GridBagLayout layout ) {
        layout.columnWidths = new int[]{ 70, 70, 70 };
        layout.rowHeights = new int[]{ 5, 60, 50 };
    }

    @Override
    protected void loadContent() {
        // Button's label input
        addContent(
                new JLabel( "Label" ),
                label -> {
                    label.setForeground( Color.WHITE );
                    label.setFont( SETTINGS.UIFont() );
                },
                constraints -> constraints.anchor = GridBagConstraints.LINE_START
        );
        addLabelInput();

        // Button's Font & Size
        addFontSelector();
        addSizeSpinner();

        // Button's Font's Style
        boldButton = addToggleButton( "B", true, 0 );
        italicButton = addToggleButton( "I", true, 1 );
        underlineButton = addToggleButton( "U", false, 2 );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        labelInput.setText( button.text() );

        Font curFont = button.font();
        fontSelector.setSelectedItem( curFont.getFamily() );
        sizeSpinner.setValue( curFont.getSize() );
        boldButton.setSelected( curFont.isBold() );
        italicButton.setSelected( curFont.isItalic() );
        underlineButton.setSelected( false );
    }
}
