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

import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.persistent.Persistent;
import me.knighthat.interactivedeck.profile.Profile;
import me.knighthat.lib.logging.Log;
import me.knighthat.lib.observable.Observable;
import me.knighthat.lib.util.ShortUUID;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonsDisplaySection extends JPanel implements Flexible {

    private final @NotNull Observable<IButton> selected;

    public ButtonsDisplaySection() {
        selected = Observable.of( null );

        setBackground( new Color( 51, 51, 51 ) );
        setDimension( this, 750, 520 );
        setLayout( new GridBagLayout() );

        selected.observe( ( old, button ) -> {
            MainMenu menu = (MainMenu) getTopLevelAncestor();
            if (button != null) {
                menu.buttonModifiers().updateSelectedButton( button );
                button.toggleSelect();
            } else
                menu.buttonModifiers().setVisible( false );
        } );

        Persistent.observeActive( ( oldP, newP ) -> {
            if (newP != null)
                updateButtons( newP );
        } );
    }

    private @NotNull GridBagConstraints genConstraints( @NotNull Profile profile ) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.0D;
        constraints.weighty = 0.0D;
        constraints.ipadx = profile.getGap();
        constraints.ipady = profile.getGap();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;

        return constraints;
    }

    private void iBtnClickEvent( java.awt.event.MouseEvent evt ) {
        IButton selected = (IButton) evt.getComponent();

        this.selected.getValue().ifPresentOrElse( currentlySelected -> {

            currentlySelected.toggleSelect();
            this.selected.setValue( currentlySelected == selected ? null : selected );

        }, () -> this.selected.setValue( selected ) );

        String pName = Persistent.findProfile( selected.getProfile() )
                                 .map( Profile::getDisplayName )
                                 .orElse( ShortUUID.from( selected.getUuid() ) );
        String log = "Button at [x=%s, y=%s] of profile \"%s\" clicked.";
        Log.deb( log.formatted( selected.getPosX(), selected.getPosY(), pName ) );
    }

    public void updateButtons( @NotNull Profile profile ) {
        removeAll();
        unselectAll();

        GridBagConstraints constraints = genConstraints( profile );
        profile.getButtons().forEach( ( button ) -> {
            if (button.getMouseListeners().length == 0)
                button.addMouseListener( new MouseAdapter() {
                    public void mouseClicked( MouseEvent e ) {
                        iBtnClickEvent( e );
                    }
                } );

            constraints.gridx = button.getPosX();
            constraints.gridy = button.getPosY();
            add( button, constraints );
        } );

        revalidate();
        repaint();

        Log.deb( "Buttons updated" );
    }

    public void unselectAll() {
        selected.getValue().ifPresent( IButton::toggleSelect );
        selected.setValue( null );
    }
}
