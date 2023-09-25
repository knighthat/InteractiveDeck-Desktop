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

package me.knighthat.interactivedeck.menus.modifier;

import me.knighthat.interactivedeck.component.ContentContainer;
import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public abstract class ModifierPanel extends JPanel implements IButtonProperty, ContentContainer, Flexible {

    protected IButton button;
    private boolean isInitialized = false;

    protected ModifierPanel() {
        setOpaque( false );
        setDimension( this, 250, 520 );
    }

    protected abstract void setupLayout( @NotNull GridBagLayout layout );

    protected abstract void initComponents();

    protected abstract void loadProperties( @NotNull IButton button );

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        this.button = button;
        loadProperties( button );
    }

    @Override
    public @NotNull ModifierPanel container() {
        return this;
    }

    @Override
    public void setVisible( boolean b ) {
        if (!isInitialized) {
            GridBagLayout layout = new GridBagLayout();
            setupLayout( layout );
            setLayout( layout );

            initComponents();

            isInitialized = true;
        }
        super.setVisible( b );
    }
}
