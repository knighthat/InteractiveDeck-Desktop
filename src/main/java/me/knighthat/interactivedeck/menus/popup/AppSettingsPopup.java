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

import me.knighthat.interactivedeck.component.IntegerFilter;
import me.knighthat.interactivedeck.component.IpAddressFilter;
import me.knighthat.interactivedeck.component.input.HexColorTextField;
import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.font.FontFactory;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.StringJoiner;

import static me.knighthat.interactivedeck.settings.Settings.SETTINGS;

public class AppSettingsPopup extends YesNoPopup {

    public static AppSettingsPopup INSTANCE;

    private final @NotNull JTextField[] addressInputs;
    private final @NotNull JTextField portInput;
    private final @NotNull JComboBox<Integer> bufferInput;
    private final @NotNull HexColorTextField selectedColorInput;
    private final @NotNull JComboBox<String> UIFontInput;
    private final @NotNull JComboBox<String> defaultButtonFontInput;

    public AppSettingsPopup( @NotNull Window window ) {
        super( window, "Settings", "Apply", "Cancel" );
        this.addressInputs = new JTextField[4];
        this.portInput = new JTextField();
        this.bufferInput = new JComboBox<>();
        this.selectedColorInput = new HexColorTextField();
        this.UIFontInput = new JComboBox<>( FontFactory.availableFamilyNames() );
        this.defaultButtonFontInput = new JComboBox<>( FontFactory.availableFamilyNames() );

        GridBagLayout layout = (GridBagLayout) contentContainer.getLayout();
        layout.columnWidths = new int[]{ 40, 10, 40, 10, 40, 10, 40, 10, 50 };
    }

    @Override
    public void initComponents() {
        // IP Address
        addContent(
                new UILabel( "IP Address" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.gridwidth = 8;
                }
        );
        DocumentFilter filter = new IpAddressFilter();
        for (int i = 0 ; i < addressInputs.length ; i++) {
            JTextField input = new JTextField( 3 );
            addContent(
                    addressInputs[i] = input,
                    comp -> {
                        setDimension( comp, 40, 30 );

                        input.setHorizontalAlignment( JTextField.RIGHT );
                        ( (AbstractDocument) input.getDocument() ).setDocumentFilter( filter );
                    },
                    constraints -> constraints.gridy = 1
            );
            String labelString = i < addressInputs.length - 1 ? "." : ":";
            addContent(
                    new UILabel( labelString ),
                    label -> label.setForeground( Color.BLACK ),
                    constraints -> constraints.gridy = 1
            );
        }

        // Port
        addContent(
                new UILabel( "Port" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.gridy = 0;
                }
        );
        addContent(
                portInput,
                comp -> {
                    setDimension( comp, 50, 30 );

                    DocumentFilter integerFilter = new IntegerFilter( 0x0, 0xffff );
                    ( (AbstractDocument) portInput.getDocument() ).setDocumentFilter( integerFilter );
                },
                constraints -> {
                    constraints.gridy = 1;
                    constraints.anchor = GridBagConstraints.LINE_START;
                }
        );

        // Stream Buffer
        Insets insets = new Insets( 20, 0, 0, 0 );
        addContent(
                new UILabel( "Buffer (KB)" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 2;
                    constraints.gridwidth = 6;
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.insets = insets;
                }
        );
        addContent(
                bufferInput,
                comp -> {
                    setDimension( comp, 100, 30 );

                    for (int i : new int[]{ 128, 256, 512, 1024, 2048, 4096, 8192 })
                        bufferInput.addItem( i );
                },
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 3;
                    constraints.gridwidth = 6;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                }
        );

        // Selected Button Color
        addContent(
                new UILabel( "Selected Button Color" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 4;
                    constraints.gridwidth = 6;
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.insets = insets;
                }
        );
        addContent(
                selectedColorInput,
                comp -> setDimension( comp, 100, 30 ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 5;
                    constraints.gridwidth = 6;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                }
        );

        // UI Font
        addContent(
                new UILabel( "UI Font" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 6;
                    constraints.gridwidth = 6;
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.insets = insets;
                }
        );
        addContent(
                UIFontInput,
                comp -> setDimension( comp, 100, 30 ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 7;
                    constraints.gridwidth = 6;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                }
        );

        // Default Font of Button
        addContent(
                new UILabel( "Default Button Font" ),
                label -> label.setForeground( Color.BLACK ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 8;
                    constraints.gridwidth = 6;
                    constraints.anchor = GridBagConstraints.LINE_START;
                    constraints.insets = insets;
                }
        );
        addContent(
                defaultButtonFontInput,
                comp -> setDimension( comp, 100, 30 ),
                constraints -> {
                    constraints.gridx = 2;
                    constraints.gridy = 9;
                    constraints.gridwidth = 6;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                }
        );

        // Attention Notice
        addContent(
                new UILabel( "Changing settings requires restart to take effect!" ),
                label -> {
                    setDimension( label, 250, 50 );

                    Font font = new Font( SETTINGS.getUiFont().getFamily(), Font.ITALIC, 9 );
                    label.setFont( font );
                    label.setForeground( Color.BLACK );

                    ( (JLabel) label ).setVerticalAlignment( SwingConstants.BOTTOM );
                    ( (JLabel) label ).setHorizontalAlignment( SwingConstants.CENTER );
                },
                constraints -> {
                    constraints.gridy = 10;
                    constraints.gridwidth = 9;
                }
        );
    }

    @Override
    protected void positiveButtonClickEvent( @NotNull MouseEvent event ) {
        StringJoiner builder = new StringJoiner( "." );
        for (JTextField input : addressInputs)
            builder.add( input.getText() );
        SETTINGS.setAddress( builder.toString() );

        int port = Integer.parseInt( portInput.getText() );
        SETTINGS.setPort( port );

        Object bufferSize = bufferInput.getSelectedItem();
        if (bufferSize != null)
            SETTINGS.setBufferSize( (int) bufferSize );

        Color selectedColor = ColorUtils.fromHex( selectedColorInput.getText() );
        SETTINGS.setSelectedColor( selectedColor );

        Object uiFontFamily = UIFontInput.getSelectedItem();
        if (uiFontFamily != null) {
            Font font = new Font( (String) uiFontFamily, Font.PLAIN, 14 );
            SETTINGS.setUiFont( font );
        }

        Object defBtnFontFamily = defaultButtonFontInput.getSelectedItem();
        if (defBtnFontFamily != null) {
            Font font = new Font( (String) defBtnFontFamily, Font.PLAIN, 14 );
            SETTINGS.setDefaultButtonFont( font );
        }
    }

    @Override
    public void present() {
        super.present();

        byte[] ipArray = SETTINGS.addressInBytes();
        for (int i = 0 ; i < ipArray.length ; i++)
            addressInputs[i].setText( String.valueOf( ipArray[i] ) );

        String port = String.valueOf( SETTINGS.getPort() );
        portInput.setText( port );

        bufferInput.setSelectedItem( SETTINGS.getBufferSize() );

        String selectedHex = ColorUtils.toHex( SETTINGS.getSelectedColor() );
        selectedColorInput.setText( selectedHex.substring( 1 ) );

        String UIFontFamily = SETTINGS.getUiFont().getFamily();
        UIFontInput.setSelectedItem( UIFontFamily );

        String defBtnFont = SETTINGS.getDefaultButtonFont().getFamily();
        defaultButtonFontInput.setSelectedItem( defBtnFont );

        setLocationRelativeTo( getOwner() );
    }
}
