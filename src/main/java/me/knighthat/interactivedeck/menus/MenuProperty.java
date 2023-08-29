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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MenuProperty {

    private static final @NotNull MenuProperty internal = new MenuProperty();

    private final @NotNull Set<Profile> profiles;
    private final @NotNull Set<IButton> buttons;
    private @Nullable Profile active;

    MenuProperty() {
        profiles = new HashSet<>();
        active = null;
        buttons = new HashSet<>();
    }

    public static @NotNull @Unmodifiable Set<Profile> profiles() {
        return Set.copyOf( internal.profiles );
    }

    public static @NotNull @Unmodifiable Set<Profile> profiles( @NotNull Predicate<Profile> condition ) {
        Set<Profile> profiles = new HashSet<>();
        internal.profiles.forEach( p -> {
            if (condition.test( p ))
                profiles.add( p );
        } );
        return profiles;
    }

    public static @NotNull Profile[] profileArray() {
        return internal.profiles.toArray( Profile[]::new );
    }

    public static void add( @NotNull Profile profile ) {
        internal.profiles.add( profile );
        internal.buttons.addAll( profile.buttons() );
    }

    public static void remove( @NotNull Profile profile ) {
        profile.buttons().forEach( internal.buttons::remove );
        internal.profiles.remove( profile );
    }

    public static @NotNull Optional<Profile> profile( @NotNull UUID uuid ) {
        Profile result = null;
        for (Profile p : internal.profiles)
            if (p.uuid().equals( uuid )) {
                result = p;
                break;
            }
        return Optional.ofNullable( result );
    }

    public static @NotNull @Unmodifiable Set<IButton> buttons() {
        return Set.copyOf( internal.buttons );
    }

    public static @NotNull Optional<IButton> button( @NotNull UUID uuid ) {
        IButton result = null;
        for (IButton btn : internal.buttons)
            if (btn.uuid().equals( uuid )) {
                result = btn;
                break;
            }
        return Optional.ofNullable( result );
    }

    public static void add( @NotNull IButton button ) {
        internal.buttons.add( button );
    }

    public static void remove( @NotNull IButton button ) {
        internal.buttons.remove( button );
    }

    public static @NotNull Profile active() {
        if (internal.active == null)
            throw new NullPointerException( "Active profile is not supposed to be null!" );
        return internal.active;
    }

    public static void active( @NotNull Profile profile ) {
        internal.active = profile;
    }
}
