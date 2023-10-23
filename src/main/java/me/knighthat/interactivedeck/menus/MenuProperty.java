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

import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.lib.logging.Log;
import me.knighthat.lib.observable.Observable;
import me.knighthat.lib.observable.Observer;
import me.knighthat.lib.util.ShortUUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public class MenuProperty {

    private static final @NotNull InternalProperty INTERNAL = new InternalProperty();

    private static Profile DEFAULT;

    public static @NotNull @Unmodifiable Set<Profile> profiles() {
        return Set.copyOf( INTERNAL.profiles );
    }

    public static @NotNull Profile[] profileArray() {
        return INTERNAL.profiles.toArray( Profile[]::new );
    }

    public static @NotNull Optional<Profile> profile( @NotNull UUID uuid ) {
        Profile profile = null;
        for (Profile p : profiles())
            if (p.getUuid().equals( uuid )) {
                profile = p;
                break;
            }
        return Optional.ofNullable( profile );
    }

    public static void add( @NotNull Profile profile ) {
        INTERNAL.profiles.add( profile );
        INTERNAL.buttons.addAll( profile.getButtons() );

        if (profile.isDefault())
            DEFAULT = profile;
    }

    public static void remove( @NotNull Profile profile ) {
        profile.getButtons().forEach( INTERNAL.buttons::remove );
        INTERNAL.profiles.remove( profile );
    }

    public static @NotNull Optional<IButton> button( @NotNull UUID uuid ) {
        IButton result = null;
        for (IButton btn : INTERNAL.buttons)
            if (btn.getUuid().equals( uuid )) {
                result = btn;
                break;
            }
        return Optional.ofNullable( result );
    }

    public static void add( @NotNull IButton button ) {
        INTERNAL.buttons.add( button );
    }

    public static void remove( @NotNull IButton button ) {
        INTERNAL.buttons.remove( button );
    }

    public static @NotNull Optional<Profile> active() {
        return INTERNAL.active.getValue();
    }

    public static void active( @NotNull Profile profile ) {
        String shortUuid = ShortUUID.from( profile.getUuid() );
        String info = "Now showing %s (%s) with %s button(s)";
        Log.info( info.formatted( profile.getDisplayName(), shortUuid, profile.getButtons().size() ) );

        INTERNAL.active.setValue( profile );
    }

    public static void observeActive( @NotNull Observer<Profile> observer ) {
        INTERNAL.active.observe( observer );
    }

    public static Profile defaultProfile() {
        return DEFAULT;
    }
}

class InternalProperty {

    final @NotNull Collection<Profile> profiles;
    final @NotNull Collection<IButton> buttons;
    final @NotNull Observable<Profile> active;

    InternalProperty() {
        profiles = new HashSet<>();
        active = Observable.of( null );
        buttons = new HashSet<>();
    }
}
