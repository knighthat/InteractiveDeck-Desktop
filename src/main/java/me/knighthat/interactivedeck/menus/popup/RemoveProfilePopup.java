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
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.utils.UuidUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public final class RemoveProfilePopup extends ProfilePopup {

    public static RemoveProfilePopup INSTANCE;

    private final @NotNull UILabel message;

    public RemoveProfilePopup( @NotNull Window window ) {
        super( window, "Remove Profile", "Remove", "Cancel" );
        this.message = new UILabel();
    }

    @Override
    public void initComponents() {
        addContent(
                message,
                label -> {
                    label.setForeground( Color.BLACK );
                    setDimension( label, 250, 50 );
                },
                constraints -> {}
        );
    }

    @Override
    protected void loadProfile( @NotNull Profile profile ) {
        String msg = "<html>You are about to remove profile %s (%s). Do you want to continue?</html>";
        String uuid = UuidUtils.lastFiveChars( profile.uuid );
        message.setText( msg.formatted( profile.displayName(), uuid ) );
    }

    @Override
    protected void positiveButtonClickEvent( @NotNull MouseEvent event ) {
        profile.remove();
        ( (MainMenu) getOwner() ).updateProfilesList();
        MenuProperty.active( MenuProperty.defaultProfile() );
    }

    @Override
    protected void positiveButton( @NotNull JButton button ) {
        super.positiveButton( button );
        setDimension( button, 100, 25 );
    }
}
