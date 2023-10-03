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

import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.menus.modifier.ButtonModifierContainer;
import me.knighthat.interactivedeck.menus.popup.*;
import me.knighthat.interactivedeck.utils.GlobalVars;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

/**
 * @author knighthat
 */
public class MainMenu extends javax.swing.JFrame {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonModifierContainer buttonModifier;
    private ButtonsDisplaySection buttonsDisplaySection;
    private ProfileSection profileSection;
    // End of variables declaration//GEN-END:variables

    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        super( GlobalVars.name() + " - " + GlobalVars.version() );

        AddProfilePopup.INSTANCE = new AddProfilePopup( this );
        RemoveProfilePopup.INSTANCE = new RemoveProfilePopup( this );
        ProfileConfigurationPopup.INSTANCE = new ProfileConfigurationPopup( this );
        WarningPopup.INSTANCE = new WarningPopup( this );
        AppSettingsPopup.INSTANCE = new AppSettingsPopup( this );
        ColorPallet.INSTANCE = new ColorPallet( this );

        initComponents();

        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e ) {
                buttonsDisplaySection.unselectAll();

                CompletableFuture.runAsync( () -> {
                    Thread.currentThread().setName( "FILE" );

                    Json.dump( SETTINGS );
                    MenuProperty.profiles().forEach( Json::dump );
                } ).join();

                super.windowClosing( e );
            }
        } );

        // Show default profile
        MenuProperty.active( MenuProperty.defaultProfile() );

        setLocationRelativeTo( null );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        profileSection = new me.knighthat.interactivedeck.menus.ProfileSection();
        buttonsDisplaySection = new me.knighthat.interactivedeck.menus.ButtonsDisplaySection();
        buttonModifier = new me.knighthat.interactivedeck.menus.modifier.ButtonModifierContainer();
        ConnectionStatusSection connectionStatusSection = new ConnectionStatusSection();

        setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
        setBackground( new java.awt.Color( 153, 153, 153 ) );
        setCursor( new java.awt.Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
        setMaximumSize( new java.awt.Dimension( 1000, 600 ) );
        setMinimumSize( new java.awt.Dimension( 1000, 600 ) );
        setResizable( false );
        getContentPane().add( profileSection, java.awt.BorderLayout.PAGE_START );
        getContentPane().add( buttonsDisplaySection, java.awt.BorderLayout.WEST );
        getContentPane().add( buttonModifier, java.awt.BorderLayout.EAST );
        getContentPane().add( connectionStatusSection, java.awt.BorderLayout.PAGE_END );
    }// </editor-fold>//GEN-END:initComponents

    public @NotNull ButtonModifierContainer buttonModifiers() {
        return this.buttonModifier;
    }

    public @NotNull ProfileSection profileSection() {return this.profileSection;}

    public @NotNull ButtonsDisplaySection buttonsDisplaySection() {return this.buttonsDisplaySection;}
}
