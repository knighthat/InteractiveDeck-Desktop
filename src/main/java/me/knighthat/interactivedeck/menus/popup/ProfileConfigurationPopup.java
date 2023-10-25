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

import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.profile.Profile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public final class ProfileConfigurationPopup extends ProfilePopup {

    public static ProfileConfigurationPopup INSTANCE;

    private final @NotNull JTextField displayNameInput;
    private final @NotNull JSpinner columns;
    private final @NotNull JSpinner rows;
    private final @NotNull JSpinner gap;

    public ProfileConfigurationPopup( @NotNull Window window ) {
        super( window, "Profile Configuration", "Apply", "Cancel" );
        this.displayNameInput = new JTextField();
        this.columns = new JSpinner();
        this.rows = new JSpinner();
        this.gap = new JSpinner();
    }

    private void spinner( @NotNull JSpinner spinner, @NotNull String name, int gridy, int min ) {
        addContent(
                new UILabel( name ),
                label -> {
                    setDimension( label, 150, 20 );
                    label.setForeground( Color.BLACK );
                },
                constraints -> {
                    constraints.gridy = gridy;
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets( 10, 0, 0, 0 );
                }
        );
        addContent(
                spinner,
                comp -> {
                    setDimension( comp, 150, 30 );
                    SpinnerModel model = new SpinnerNumberModel( min, min, 10, 1 );
                    ( (JSpinner) comp ).setModel( model );
                },
                constraints -> constraints.gridy = gridy + 1
        );
    }

    private int validate( @NotNull Object obj, int min ) {
        if (!( obj instanceof Integer number ))
            return min;
        return number < min ? min : Math.min( number, 10 );
    }

    @Override
    public void initComponents() {
        addContent(
                new UILabel( "Display Name" ),
                label -> {
                    setDimension( label, 150, 20 );
                    label.setForeground( Color.BLACK );
                },
                constraints -> constraints.anchor = GridBagConstraints.WEST
        );
        addContent(
                displayNameInput,
                comp -> setDimension( comp, 150, 30 ),
                constraints -> constraints.gridy = 1
        );
        spinner( columns, "Columns", 2, 1 );
        spinner( rows, "Rows", 4, 1 );
        spinner( gap, "Gap", 6, 0 );
    }

    @Override
    protected void loadProfile( @NotNull Profile profile ) {
        displayNameInput.setText( profile.getDisplayName() );
        columns.setValue( profile.getColumns() );
        rows.setValue( profile.getRows() );
        gap.setValue( profile.getGap() );
        displayNameInput.requestFocus();
    }

    @Override
    protected void positiveButtonClickEvent( @NotNull MouseEvent event ) {
        profile.setDisplayName( displayNameInput.getText() );
        profile.setColumns( validate( columns.getValue(), 1 ) );
        profile.setRows( validate( rows.getValue(), 1 ) );
        profile.setGap( validate( gap.getValue(), 0 ) );

        MainMenu menu = (MainMenu) getOwner();
        menu.profileSection().updateList();
        menu.buttonsDisplaySection().updateButtons( profile );

        finish();
    }
}
