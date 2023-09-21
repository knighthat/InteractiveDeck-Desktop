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

package me.knighthat.interactivedeck.component.plist;


import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.utils.UuidUtils;

import javax.swing.*;
import java.awt.*;

public class PBoxRenderer extends JLabel implements ListCellRenderer<Profile> {

    public Component getListCellRendererComponent( JList<? extends Profile> list, Profile profile, int i, boolean isSelected, boolean cellHasFocus ) {
        String display = "No available profile";
        if (profile != null)
            display = "%s (%s)".formatted( profile.displayName(), UuidUtils.lastFiveChars( profile.uuid ) );

        super.setText( display );
        super.setBackground( isSelected ? list.getSelectionBackground() : list.getBackground() );
        super.setForeground( isSelected ? list.getSelectionForeground() : list.getForeground() );
        return this;
    }
}
