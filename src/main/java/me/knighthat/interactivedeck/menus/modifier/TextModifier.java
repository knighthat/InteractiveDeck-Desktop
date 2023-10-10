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
import me.knighthat.interactivedeck.component.input.FunctionalTextInput;
import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.font.FontFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class TextModifier extends ModifierPanel<GridBagLayout, GridBagConstraints> {

    private final @NotNull FunctionalTextInput labelInput;
    private final @NotNull JComboBox<String> fontSelector;
    private final @NotNull JSpinner sizeSpinner;
    private final @NotNull ToggleStyleButton boldButton;
    private final @NotNull ToggleStyleButton italicButton;
    private final @NotNull ToggleStyleButton underlineButton;

    public TextModifier() {
        this.labelInput = new FunctionalTextInput( 140, 40 );
        this.fontSelector = new JComboBox<>( FontFactory.availableFamilyNames() );
        this.sizeSpinner = new JSpinner();
        this.boldButton = new ToggleStyleButton( "B", true );
        this.italicButton = new ToggleStyleButton( "I", true );
        this.underlineButton = new ToggleStyleButton( "U", false );
    }

    private void addStyleButton( @NotNull ToggleStyleButton button ) {
        addContent(
                button,
                c -> ( (ToggleStyleButton) c ).addChangeListener( this::stylingButtonSelectedEvent ),
                constraints -> constraints.gridy = 3
        );
    }

    private void stylingButtonSelectedEvent( @NotNull ChangeEvent event ) {
        int bold = boldButton.isSelected() ? Font.BOLD : Font.PLAIN;
        int italic = italicButton.isSelected() ? Font.ITALIC : Font.PLAIN;
        applyFont( null, bold | italic, null );
    }

    private void applyFont( @Nullable String family, @Nullable Integer style, @Nullable Integer size ) {
        Font curFont = button.foreground().getFont();
        Font newFont = new Font(
                family == null ? curFont.getFamily() : family,
                style == null ? curFont.getStyle() : style,
                size == null ? curFont.getSize() : size
        );
        button.foreground().font( newFont );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        labelInput.setText( button.foreground().getText() );

        Font curFont = button.foreground().getFont();
        fontSelector.setSelectedItem( curFont.getFamily() );
        sizeSpinner.setValue( curFont.getSize() );
        boldButton.setSelected( curFont.isBold() );
        italicButton.setSelected( curFont.isItalic() );
        underlineButton.setSelected( false );
    }

    @Override
    protected @NotNull GridBagLayout initLayout() {
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{ 70, 70, 70 };
        layout.rowHeights = new int[]{ 5, 60, 50 };
        return layout;
    }

    @Override
    public @NotNull GridBagConstraints constraints() {
        return new GridBagConstraints();
    }

    @Override
    public void initComponents() {
        // Input Label
        addContent(
                new UILabel( "Label" ),
                label -> {},
                constraints -> constraints.anchor = GridBagConstraints.LINE_START
        );

        // Input Field
        addContent(
                labelInput,
                c -> labelInput.addChangeEvent(
                        event -> button.foreground().text( labelInput.getText() ) ),
                constraints -> {
                    constraints.gridy = 1;
                    constraints.gridwidth = 3;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.anchor = GridBagConstraints.NORTH;
                }
        );

        // Font Selector
        addContent(
                fontSelector,
                c -> {
                    setDimension( c, 140, 30 );
                    ( (JComboBox<?>) c ).addActionListener( event -> {
                        Object selected = fontSelector.getSelectedItem();
                        if (selected != null)
                            applyFont( selected.toString(), null, null );
                    } );
                },
                constraints -> {
                    constraints.gridy = 2;
                    constraints.gridwidth = 2;
                }
        );

        // Size Spinner
        addContent(
                sizeSpinner,
                c -> {
                    setDimension( c, 55, 30 );
                    ( (JSpinner) c ).addChangeListener( event -> {
                        Object value = sizeSpinner.getValue();
                        if (value instanceof Integer integer)
                            applyFont( null, null, integer );
                    } );
                },
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 2;
                    constraints.anchor = GridBagConstraints.LINE_END;
                }
        );

        // Button's Font's Style
        addStyleButton( boldButton );
        addStyleButton( italicButton );
        addStyleButton( underlineButton );
    }

    private static class ToggleStyleButton extends JToggleButton {

        public ToggleStyleButton( @NotNull String text, boolean isEnabled ) {
            super( text );
            setEnabled( isEnabled );
        }
    }
}
