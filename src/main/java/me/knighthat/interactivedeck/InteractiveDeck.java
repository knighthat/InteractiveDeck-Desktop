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
package me.knighthat.interactivedeck;

import me.knighthat.interactivedeck.component.icon.Icons;
import me.knighthat.interactivedeck.connection.wireless.WirelessController;
import me.knighthat.interactivedeck.file.Settings;
import me.knighthat.interactivedeck.font.FontFactory;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.menus.NotificationCenter;
import me.knighthat.interactivedeck.menus.popup.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

/**
 * @author knighthat
 */
public class InteractiveDeck {

    private final @NotNull Semaphore semaphore;

    private InteractiveDeck() {
        this.semaphore = new Semaphore( 0 );
    }

    public static void main( String[] args ) {
        Thread.currentThread().setName( "MAIN" );

        InteractiveDeck main = new InteractiveDeck();
        main.init();
        main.on();
        try {
            main.semaphore.acquire();
        } catch (InterruptedException e) {
            Log.exc( "Failed to keep the thread alive", e, true );
            Log.reportBug();
        }
        main.off();
    }

    /**
     * Setup stage
     * Everything required by the core components
     * (GUI, working dir, etc.) or before {@link #on()} stage
     * must be loaded here first.
     */
    private void init() {
        // System-wide Configurations
        Platform.init();
        WorkingDirectory.init();
        Settings.init();

        Log.start();

        // Decorations
        FontFactory.init();
        Icons.loadIcons();
        WorkingDirectory.loadProfiles();

        /* Set the Nimbus look and feel */
        //<editor-fold default-state="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals( info.getName() )) {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch (Exception e) {
            Log.exc( "Error occurs while loading Look&Feel", e, true );
        }
        //</editor-fold>
    }

    /**
     * Run stage
     * This is where program start functioning
     */
    private void on() {
        NotificationCenter.init();

        new WirelessController().start();

        MainMenu mainMenu = new MainMenu();

        // Load Popups
        AddProfilePopup.INSTANCE = new AddProfilePopup( mainMenu );
        RemoveProfilePopup.INSTANCE = new RemoveProfilePopup( mainMenu );
        ProfileConfigurationPopup.INSTANCE = new ProfileConfigurationPopup( mainMenu );
        WarningPopup.INSTANCE = new WarningPopup( mainMenu );
        AppSettingsPopup.INSTANCE = new AppSettingsPopup( mainMenu );
        ColorPallet.INSTANCE = new ColorPallet( mainMenu );

        mainMenu.setVisible( true );

        mainMenu.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed( WindowEvent e ) {
                semaphore.release();
            }
        } );
    }

    /**
     * Close stage
     * Finalize the process right before program stop
     */
    private void off() {
        // Save settings and profiles
        Json.dump( SETTINGS );
        MenuProperty.profiles().forEach( Json::dump );

        Log.stop();

        System.exit( 0 );
    }
}
