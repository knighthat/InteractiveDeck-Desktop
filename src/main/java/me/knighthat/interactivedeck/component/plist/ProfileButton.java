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

import me.knighthat.interactivedeck.component.icon.HoverableSVGIcon;
import me.knighthat.interactivedeck.component.icon.Icons;
import org.jetbrains.annotations.NotNull;

public class ProfileButton extends HoverableSVGIcon {

    public ProfileButton( @NotNull ButtonType type ) {
        super( 30, 30 );

        icon = switch (type) {
            case ADD -> Icons.INTERNAL.PROFILE_ADD;
            case REMOVE -> Icons.INTERNAL.PROFILE_REMOVE;
            case CONFIGURE -> Icons.INTERNAL.PROFILE_CONFIGURE;
        };
        hoverIcon = switch (type) {
            case ADD -> Icons.INTERNAL.PROFILE_ADD_HOVER;
            case REMOVE -> Icons.INTERNAL.PROFILE_REMOVE_HOVER;
            case CONFIGURE -> Icons.INTERNAL.PROFILE_CONFIGURE_HOVER;
        };

        setDocument( icon );
    }

    public enum ButtonType {
        ADD, REMOVE, CONFIGURE
    }
}
