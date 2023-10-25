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

package me.knighthat.interactivedeck.component.task;

import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.plist.ProfilesComboBox;
import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.persistent.Persistent;
import me.knighthat.interactivedeck.task.GotoPage;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GotoPageTaskConfigurator extends TaskConfigurator {

    private ProfilesComboBox profilesList;

    public GotoPageTaskConfigurator() {
        super( GotoPage.class );
        setBackground( ColorUtils.DEFAULT_DARK );
    }

    @Override
    public @Nullable Object[] taskParams() {
        Profile profile = (Profile) profilesList.getSelectedItem();
        return profile == null ? null : new Object[]{ profile.getUuid() };
    }

    @Override
    public void initComponents() {
        this.profilesList = new ProfilesComboBox();
        addContent(
                new UILabel( "Switch to profile:" ),
                label -> {},
                constraints -> constraints.anchor = GridBagConstraints.LINE_START
        );
        addContent(
                profilesList,
                comp -> setDimension( comp, 200, 40 ),
                constraints -> constraints.gridy = 1
        );
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        Persistent.getActive().ifPresent( profilesList::reloadExcept );

        if (profilesList.getModel().getSize() == 0) {
            profilesList.setEnabled( false );
            return;
        }

        if (button.getTask() instanceof GotoPage gotoPage)
            Persistent.findProfile( gotoPage.target() )
                      .ifPresentOrElse(
                              profilesList::setSelectedItem,
                              profilesList::selectDefaultProfile
                      );
    }
}
