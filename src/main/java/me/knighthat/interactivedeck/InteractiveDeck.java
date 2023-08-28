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

import me.knighthat.interactivedeck.connection.Client;
import me.knighthat.interactivedeck.connection.wireless.WirelessController;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.file.Settings;
import me.knighthat.interactivedeck.menus.MainMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author knighthat
 */
public class InteractiveDeck {

    public static @Nullable Client client = null;

    static {
        Thread.currentThread().setName( "MAIN" );
    }

    public static void main( String[] args ) {
        Log.deb( "DEBUG mode is enabled!" );

        WorkingDirectory.init();
        WorkingDirectory.loadProfiles();
        Settings.init();

        printSysConfig();

        new WirelessController().start();

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals( info.getName() )) {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch (Exception e) {
            Log.err( "Error occurs while loading graphical interface - Look&Feel" );
            Log.err( "Reason: " + e.getMessage() );
            e.printStackTrace();
        }
        //</editor-fold>

        new MainMenu().setVisible( true );
    }

    static void printSysConfig() {
        Log.info( "Java runtime version: " + jre() );
        Log.info( "Running on: " + platform() );
        Log.info( "Working directory: " + WorkingDirectory.path() );
    }

    static @NotNull String platform() {
        String osName = System.getProperty( "os.name" );
        String osVer = System.getProperty( "os.version" );
        String osArch = System.getProperty( "os.arch" );

        return String.format( "%s %s %s", osArch, osName, osVer );
    }

    static @NotNull String jre() {
        String vmName = System.getProperty( "java.vm.name" );
        String vmVer = System.getProperty( "java.vm.version" );

        return String.format( "%s %s", vmName, vmVer );
    }
}
