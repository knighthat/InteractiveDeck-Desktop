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
import me.knighthat.interactivedeck.component.plist.ProfileButton;
import me.knighthat.interactivedeck.component.plist.ProfileEvent;
import me.knighthat.interactivedeck.component.plist.ProfilesComboBox;
import me.knighthat.interactivedeck.component.setting.SettingButton;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.menus.popup.AddProfilePopup;
import me.knighthat.interactivedeck.menus.popup.AppSettingsPopup;
import me.knighthat.interactivedeck.menus.popup.ProfileConfigurationPopup;
import me.knighthat.interactivedeck.menus.popup.RemoveProfilePopup;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class ProfileSection extends JPanel implements Flexible {

    private final @NotNull ProfilesComboBox profiles;
    private final @NotNull ProfileButton addProfileButton;
    private final @NotNull ProfileButton removeProfileButton;
    private final @NotNull ProfileButton configProfileButton;
    private final @NotNull SettingButton settingButton;

    public ProfileSection() {
        this.profiles = new ProfilesComboBox();
        this.addProfileButton = new ProfileButton( ProfileButton.ButtonType.ADD );
        this.removeProfileButton = new ProfileButton( ProfileButton.ButtonType.REMOVE );
        this.configProfileButton = new ProfileButton( ProfileButton.ButtonType.CONFIGURE );
        this.settingButton = new SettingButton();

        setBackground( ColorUtils.DEFAULT_DARK );
        setDimension( this, 1000, 50 );

        initComponents();
        setupLayout();
    }

    // <editor-fold default-state="collapsed" desc="Init Components">
    private void initComponents() {
        profiles.addItemListener( event -> {
            if (!( event instanceof ProfileEvent pEvent ))
                return;
            if (pEvent.updateActive)
                MenuProperty.active( (Profile) event.getItem() );
        } );

        // Add profile button
        addProfileButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {AddProfilePopup.INSTANCE.present();}
        } );

        // Remove profile button
        removeProfileButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                Profile selected = (Profile) profiles.getSelectedItem();
                if (selected == null || selected.isDefault())
                    return;
                RemoveProfilePopup.INSTANCE.present( selected );
            }
        } );

        // Profile configuration button
        configProfileButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                MenuProperty.active().ifPresent( ProfileConfigurationPopup.INSTANCE::present );
            }
        } );

        // Settings button
        settingButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {AppSettingsPopup.INSTANCE.present();}
        } );

        MenuProperty.observeActive( ( oldP, newP ) -> {
            if (newP != null)
                profiles.setSelectedProfile( newP, false );
        } );
    }
    // </editor-fold>

    // <editor-fold default-state="collapsed" desc="Setup Layout">
    private void setupLayout() {
        GroupLayout layout = new GroupLayout( this );
        setLayout( layout );

        GroupLayout.SequentialGroup horizontalSequentialG = layout.createSequentialGroup();
        horizontalSequentialG
                .addGap( 30 )
                .addComponent( profiles )
                .addGap( 18 )
                .addComponent( addProfileButton )
                .addPreferredGap( UNRELATED )
                .addComponent( removeProfileButton )
                .addPreferredGap( UNRELATED )
                .addComponent( configProfileButton )
                .addGap( 500 )
                .addComponent( settingButton )
                .addGap( 20 );
        layout.setHorizontalGroup( horizontalSequentialG );

        GroupLayout.SequentialGroup verticalSequentialG = layout.createSequentialGroup();
        verticalSequentialG
                .addGap( 10 )
                .addGroup(
                        layout
                                .createParallelGroup( LEADING, false )
                                .addComponent( configProfileButton )
                                .addComponent( removeProfileButton )
                                .addComponent( addProfileButton )
                                .addComponent( profiles )
                                .addComponent( settingButton )
                );
        layout.setVerticalGroup( verticalSequentialG );
    }
    // </editor-fold>

    public void addProfile( @NotNull Profile profile ) {
        if (MenuProperty.profiles().contains( profile ))
            return;

        MenuProperty.add( profile );
        profiles.addItem( profile );
        MenuProperty.active( profile );
    }

    public void removeProfile( @NotNull Profile profile ) {
        if (!MenuProperty.profiles().contains( profile ))
            return;

        profiles.removeItem( profile );
        MenuProperty.active().ifPresent( p -> {
            if (p != profile)
                return;
            MenuProperty.active( MenuProperty.defaultProfile() );
        } );
        profile.remove();
    }

    public void updateList() {
        profiles.repaint();
        Log.deb( "Profiles list updated!" );
    }
}
