/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck.menus;

import com.google.gson.JsonObject;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.plist.ProfileButton;
import me.knighthat.interactivedeck.connection.Connection;
import me.knighthat.interactivedeck.file.Profile;
import static me.knighthat.interactivedeck.file.Settings.*;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.observable.Observable;
import me.knighthat.interactivedeck.utils.ColorUtils;
import me.knighthat.interactivedeck.utils.GlobalVars;
import me.knighthat.interactivedeck.utils.UuidUtils;
import org.jetbrains.annotations.NotNull;
/**
 *
 * @author knighthat
 */
public class MainMenu extends javax.swing.JFrame {

    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        super(GlobalVars.name() + " - " + GlobalVars.version());
        initComponents();

        initButtonObserver();

        MenuProperty.observeActive( profilesList::setSelectedItem );

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bSelected.value().ifPresent( IButton::toggleSelect );

                Json.dump( FILE.getName(), () -> {
                    JsonObject json = new JsonObject();
                    json.addProperty( "address", ADDRESS );
                    json.addProperty( "port", PORT );
                    json.addProperty( "buffer", BUFFER.length );
                    json.add( "selected_color", ColorUtils.toJson( SELECTED_COLOR ) );

                    return json;
                } );
                MenuProperty
                    .profiles()
                    .forEach( profile ->  Json.dump( profile.uuid + ".profile", profile ) );

                super.windowClosing(e);
            }
        });

        // Show default profile
        MenuProperty.active(MenuProperty.defaultProfile());

        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel profilesSection = new javax.swing.JPanel();
        profilesList = new me.knighthat.interactivedeck.component.plist.ProfilesComboBox();
        me.knighthat.interactivedeck.component.plist.ProfileButton addProfileButton = new ProfileButton(ProfileButton.ButtonType.ADD);
        me.knighthat.interactivedeck.component.plist.ProfileButton removeProfileButton = new ProfileButton(ProfileButton.ButtonType.REMOVE);
        me.knighthat.interactivedeck.component.plist.ProfileButton configureProfileButton = new ProfileButton(ProfileButton.ButtonType.CONFIGURE);
        iBtnSection = new javax.swing.JPanel();
        btnModifierSection = new javax.swing.JPanel();
        javax.swing.JPanel statusSection = new javax.swing.JPanel();
        me.knighthat.interactivedeck.component.netstatus.ConStatus conStatus = Connection.component();
        notificationCenter1 = new me.knighthat.interactivedeck.menus.NotificationCenter();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setResizable(false);

        profilesSection.setBackground(new java.awt.Color(36, 36, 36));
        profilesSection.setPreferredSize(new java.awt.Dimension(1000, 50));

        profilesList.setBackground(new java.awt.Color(51, 51, 51));
        profilesList.setMaximumSize(new java.awt.Dimension(300, 30));
        profilesList.setMinimumSize(new java.awt.Dimension(300, 30));
        profilesList.setPreferredSize(new java.awt.Dimension(300, 30));
        profilesList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilesListActionPerformed(evt);
            }
        });

        addProfileButton.setMaximumSize(new java.awt.Dimension(30, 30));
        addProfileButton.setMinimumSize(new java.awt.Dimension(30, 30));
        addProfileButton.setPreferredSize(new java.awt.Dimension(30, 30));
        addProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addButtonClicked(evt);
            }
        });

        removeProfileButton.setMaximumSize(new java.awt.Dimension(30, 30));
        removeProfileButton.setMinimumSize(new java.awt.Dimension(30, 30));
        removeProfileButton.setPreferredSize(new java.awt.Dimension(30, 30));
        removeProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeProfilesButtonClicked(evt);
            }
        });

        configureProfileButton.setMaximumSize(new java.awt.Dimension(30, 30));
        configureProfileButton.setMinimumSize(new java.awt.Dimension(30, 30));
        configureProfileButton.setPreferredSize(new java.awt.Dimension(30, 30));
        configureProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                configureProfileButtonClicked(evt);
            }
        });

        javax.swing.GroupLayout profilesSectionLayout = new javax.swing.GroupLayout(profilesSection);
        profilesSection.setLayout(profilesSectionLayout);
        profilesSectionLayout.setHorizontalGroup(
            profilesSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilesSectionLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(profilesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(configureProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(538, 538, 538))
        );
        profilesSectionLayout.setVerticalGroup(
            profilesSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilesSectionLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(profilesSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(configureProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profilesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        getContentPane().add(profilesSection, java.awt.BorderLayout.NORTH);

        iBtnSection.setBackground(new java.awt.Color(51, 51, 51));
        iBtnSection.setDoubleBuffered(false);
        iBtnSection.setMaximumSize(new java.awt.Dimension(750, 520));
        iBtnSection.setMinimumSize(new java.awt.Dimension(750, 520));
        iBtnSection.setPreferredSize(new java.awt.Dimension(750, 520));
        iBtnSection.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(iBtnSection, java.awt.BorderLayout.WEST);

        btnModifierSection.setBackground(new java.awt.Color(36, 36, 36));
        btnModifierSection.setDoubleBuffered(false);
        btnModifierSection.setMaximumSize(new java.awt.Dimension(250, 520));
        btnModifierSection.setMinimumSize(new java.awt.Dimension(250, 520));
        btnModifierSection.setPreferredSize(new java.awt.Dimension(250, 520));
        btnModifierSection.setLayout(new java.awt.BorderLayout());
        getContentPane().add(btnModifierSection, java.awt.BorderLayout.CENTER);

        statusSection.setBackground(new java.awt.Color(36, 36, 36));
        statusSection.setAlignmentX(0.0F);
        statusSection.setAlignmentY(0.0F);
        statusSection.setPreferredSize(new java.awt.Dimension(1000, 30));

        javax.swing.GroupLayout statusSectionLayout = new javax.swing.GroupLayout(statusSection);
        statusSection.setLayout(statusSectionLayout);
        statusSectionLayout.setHorizontalGroup(
            statusSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusSectionLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(conStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 349, Short.MAX_VALUE)
                .addComponent(notificationCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        statusSectionLayout.setVerticalGroup(
            statusSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusSectionLayout.createSequentialGroup()
                .addGroup(statusSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(notificationCenter1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(statusSection, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonClicked( java.awt.event.MouseEvent evt ) {//GEN-FIRST:event_addButtonClicked
        PopupMenu dialog = new AddProfilePopup( this );
        dialog.setVisible( true );
    }//GEN-LAST:event_addButtonClicked

    private void removeProfilesButtonClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeProfilesButtonClicked
        Profile selected = (Profile) profilesList.getSelectedItem();
        if (selected == null || selected.isDefault)
            return;
        selected.remove();
        updateProfilesList();

        Profile profile = MenuProperty.defaultProfile();
        MenuProperty.active(profile);
    }//GEN-LAST:event_removeProfilesButtonClicked

    private void configureProfileButtonClicked( java.awt.event.MouseEvent evt ) {//GEN-FIRST:event_configureProfileButtonClicked
        PopupMenu dialog = new ProfileConfigurationPopup( this );
        dialog.setVisible( true );
    }//GEN-LAST:event_configureProfileButtonClicked

    private void profilesListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilesListActionPerformed
        Profile profile = (Profile) profilesList.getSelectedItem();
        if (profile != null && evt.getActionCommand().equals( "comboBoxChanged" ))
            updateButtons( profile );
    }//GEN-LAST:event_profilesListActionPerformed

    void iBtnClickEvent(java.awt.event.MouseEvent evt) {
        IButton selected = (IButton) evt.getComponent();

        bSelected.value().ifPresentOrElse( currentlySelected -> {

            currentlySelected.toggleSelect();
            bSelected.value(currentlySelected == selected ? null : selected);

        }, () -> bSelected.value(selected) );

        String deb = "Button %s@x:%s,y:%s clicked!";
        String shortUuid = UuidUtils.lastFiveChars( selected.uuid );
        Log.deb( deb.formatted( shortUuid, selected.x, selected.y) );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnModifierSection;
    private javax.swing.JPanel iBtnSection;
    private me.knighthat.interactivedeck.menus.NotificationCenter notificationCenter1;
    private me.knighthat.interactivedeck.component.plist.ProfilesComboBox profilesList;
    // End of variables declaration//GEN-END:variables
    private final @NotNull Observable<IButton> bSelected = Observable.of( null );;

    public void updateButtons(@NotNull Profile profile) {
        iBtnSection.removeAll();

        bSelected.value().ifPresent( IButton::toggleSelect );
        bSelected.value(null);

        GridBagConstraints constraints = genConstraints( profile );
        profile.buttons().forEach((button) -> {
            if (button.getMouseListeners().length == 0)
                button.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        iBtnClickEvent(e);
                    }
                });

            constraints.gridx = button.x;
            constraints.gridy = button.y;
            this.iBtnSection.add(button, constraints);
        });

        iBtnSection.revalidate();
        iBtnSection.repaint();
    }

    @NotNull GridBagConstraints genConstraints(@NotNull Profile profile) {
        int gap = profile.gap();
        int spaceX = profile.columns() * (IButton.DIMENSION.width + gap) - gap;     // Horizontal space (includes gaps) taken by buttons
        int spaceY = profile.rows() * (IButton.DIMENSION.height + gap) - gap;       // Vertical space (includes gaps taken by buttons
        Dimension sectionSize = iBtnSection.getPreferredSize();                     // Area buttons can be shown

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = spaceX >= sectionSize.width ? 1D : 0D;
        constraints.weighty = spaceY >= sectionSize.height ? 1D : 0D;
        constraints.ipadx = gap;
        constraints.ipady = gap;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;

        return constraints;
    }

    void initButtonObserver() {
        bSelected.observe( btn -> {
            btnModifierSection.removeAll();
            btnModifierSection.revalidate();
            btnModifierSection.repaint();

            if (btn == null) return;

            btn.toggleSelect();

            ModifierContainer panel = new ModifierContainer(btn);
            btnModifierSection.add( panel, BorderLayout.PAGE_START );
        } );
    }

    public void updateProfilesList() {
        profilesList.removeAll();
        ComboBoxModel<Profile> model = new DefaultComboBoxModel<>(MenuProperty.profileArray());
        profilesList.setModel( model );
        profilesList.revalidate();
        profilesList.repaint();
    }
}
