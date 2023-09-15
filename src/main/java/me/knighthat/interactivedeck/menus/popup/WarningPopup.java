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

import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class WarningPopup extends PersistentDataPopup {

    public static WarningPopup INSTANCE;

    private JLabel label;

    public WarningPopup( @NotNull Window window ) {
        super( window, "Warning!!!" );
    }

    public static void showWarning( @NotNull String warning ) {
        INSTANCE.present();
        String formatted = "<html>" + warning + "</html>";
        INSTANCE.label.setText( formatted );
    }

    @Override
    protected void loadContent() {
        addContent(
                label = new JLabel(),
                comp -> setDimension( comp, 250, 100 ),
                constraints -> {}
        );
    }

    @Override
    protected void loadButtons() {
        addButton( button ->
                button.addActionListener( event -> {
                    Log.deb( event.getActionCommand() );
                    finish();
                } )
        );
    }
}