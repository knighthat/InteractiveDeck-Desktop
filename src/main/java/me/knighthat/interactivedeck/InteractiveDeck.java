/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck;

import me.knighthat.interactivedeck.connection.Client;
import me.knighthat.interactivedeck.connection.wireless.WirelessController;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.menus.MainMenu;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static me.knighthat.interactivedeck.vars.SysVars.*;

/**
 * @author knighthat
 */
public class InteractiveDeck {

    public static @Nullable Client client = null;

    static {
        Thread.currentThread().setName("MAIN");
    }

    public static void main( String[] args ) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        SwingUtilities.invokeLater(() -> {
            Thread.currentThread().setName("GUI");
            new MainMenu().setVisible(true);
        });

        Log.deb("DEBUG mode is enabled!");

        Log.info("Java runtime version: " + JRE);
        Log.info("Running on: " + PLATFORM);
        Log.info("Working directory: " + WORK_DIR);

        if (!WORK_DIR.exists()) {
            Log.info("Working folder is not exist! Making one..");
            if (!WORK_DIR.mkdirs()) {
                Log.err("Couldn't create working directory at " + WORK_DIR.getAbsolutePath());
                return;
            }
        }

        // Temporary suspend custom settings
        // SettingsFile settings = SettingsFile.init();
        // Settings.loadSettings(settings);

        new WirelessController().start();
    }
}
