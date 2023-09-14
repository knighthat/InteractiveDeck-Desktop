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
import me.knighthat.interactivedeck.file.Profiles;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.menus.MenuProperty;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class AddProfilePopup extends YesNoPopup {

    private JTextField displayNameInput;

    public AddProfilePopup( @NotNull Window window ) {
        super( window, "Add Profile", "Create", "Cancel" );
    }

    private void createProfile() {
        String fromUser = displayNameInput.getText().trim();
        if (fromUser.isBlank())
            return;  // TODO Notify user about empty input

        Profile profile = Profiles.create( fromUser );
        MenuProperty.add( profile );
        ( (MainMenu) super.getOwner() ).updateProfilesList();
        MenuProperty.active( profile );

        finish();
    }

    @Override
    protected void loadContent() {
        addContent( new JLabel( "Display Name:" ), label -> {}, constraints -> {
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets( 0, 0, 0, 10 );
        } );

        addContent(
                new JTextField(),
                comp -> {
                    setDimension( comp, 150, 30 );

                    this.displayNameInput = (JTextField) comp;

                    comp.addKeyListener( new KeyAdapter() {
                        public void keyPressed( KeyEvent evt ) {
                            if (evt.getKeyCode() == 27)
                                finish();
                            else if (evt.getKeyCode() == 10)
                                createProfile();
                        }
                    } );
                },
                constraints -> {
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.anchor = GridBagConstraints.EAST;
                }
        );
    }

    @Override
    public void present() {
        super.present();
        displayNameInput.setText( "" );
        displayNameInput.requestFocus();
    }

    @Override
    protected void positiveButtonClickEvent( @NotNull MouseEvent event ) {
        createProfile();
    }
}
