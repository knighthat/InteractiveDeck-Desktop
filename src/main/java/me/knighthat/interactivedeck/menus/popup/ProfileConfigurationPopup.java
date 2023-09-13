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

import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.menus.MenuProperty;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileConfigurationPopup extends PopupMenu {

    private Profile profile;
    private JTextField displayNameInput;
    private JSpinner columns;
    private JSpinner rows;
    private JSpinner gap;

    public ProfileConfigurationPopup( @NotNull Window owner ) {
        super( owner, "Profile Configuration" );

        // Create and add "Create" button
        addButton( button -> {
            button.setText( "Apply" );
            button.addMouseListener( new MouseAdapter() {
                @Override
                public void mouseClicked( MouseEvent e ) {
                    apply();
                }
            } );
        } );

        // Create and add "Cancel" button
        addButton( button -> {
            button.setText( "Cancel" );
            button.addMouseListener( new MouseAdapter() {
                public void mouseClicked( MouseEvent evt ) {
                    finish();
                }
            } );
        } );
    }

    @Override
    void initContent() {
        profile = MenuProperty.active().orElse( MenuProperty.defaultProfile() );

        addContent(
                new JLabel( "Display Name" ),
                label -> setDimension( label, 150, 20 ),
                constraints -> constraints.anchor = GridBagConstraints.WEST
        );
        addContent(
                new JTextField(),
                comp -> {
                    setDimension( comp, 150, 30 );
                    displayNameInput = (JTextField) comp;
                    displayNameInput.setText( profile.displayName() );
                },
                constraints -> constraints.gridy = 1
        );


        columns = spinner( "Columns", 2, profile.columns(), 1 );
        rows = spinner( "Rows", 4, profile.rows(), 1 );
        gap = spinner( "Gap", 6, profile.gap(), 0 );
    }

    private @NotNull JSpinner spinner( @NotNull String name, int gridy, int curValue, int min ) {
        addContent(
                new JLabel( name ),
                label -> setDimension( label, 150, 20 ),
                constraints -> {
                    constraints.gridy = gridy;
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets( 10, 0, 0, 0 );
                }
        );
        return (JSpinner) addContent(
                new JSpinner(),
                comp -> {
                    setDimension( comp, 150, 30 );

                    SpinnerModel model = new SpinnerNumberModel( curValue, min, 10, 1 );
                    ( (JSpinner) comp ).setModel( model );
                },
                constraints -> constraints.gridy = gridy + 1
        );
    }

    private void apply() {
        profile.displayName( displayNameInput.getText() );
        profile.columns( validate( columns.getValue(), 1 ) );
        profile.rows( validate( rows.getValue(), 1 ) );
        profile.gap( validate( gap.getValue(), 0 ) );

        ( (MainMenu) getOwner() ).updateButtons( profile );

        finish();
    }

    int validate( @NotNull Object obj, int min ) {
        if (!( obj instanceof Integer number ))
            return min;
        return number < min ? min : Math.min( number, 10 );
    }
}