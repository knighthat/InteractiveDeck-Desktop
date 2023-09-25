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

package me.knighthat.interactivedeck.component.input;

import me.knighthat.interactivedeck.component.Flexible;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class instance represents an input that triggers event
 * {@link ChangeEvent} when user presses 'Enter' key or the
 * input loses its focus.
 */
public class FunctionalTextInput extends JFormattedTextField implements Flexible {

    public FunctionalTextInput( int width, int height ) {
        super();
        setDimension( this, width, height );
        addDefaultEventListeners();
    }

    private void addDefaultEventListeners() {
        addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent e ) {
                if (e.getKeyCode() == 10)
                    fireChangeEvent();
            }
        } );
        addFocusListener( new FocusAdapter() {
            @Override
            public void focusLost( FocusEvent e ) {
                fireChangeEvent();
            }
        } );
    }

    public void addChangeEvent( @NotNull ChangeListener event ) {
        listenerList.add( ChangeListener.class, event );
    }

    private void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent( this );
        for (ChangeListener listener : listenerList.getListeners( ChangeListener.class ))
            listener.stateChanged( event );
    }
}
