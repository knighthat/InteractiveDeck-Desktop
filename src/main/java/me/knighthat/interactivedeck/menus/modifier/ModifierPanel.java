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

import me.knighthat.interactivedeck.component.ibutton.IButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public abstract class ModifierPanel extends JPanel implements IButtonProperty {

    protected IButton button;

    public ModifierPanel() {
        setOpaque( false );

        GridBagLayout layout = new GridBagLayout();
        configureLayout( layout );
        setLayout( layout );

        loadContent();
    }

    protected abstract void configureLayout( @NotNull GridBagLayout layout );

    protected abstract void loadContent();

    protected abstract void loadProperties( @NotNull IButton button );

    protected void setDimension( @NotNull JComponent component, int width, int height ) {
        Dimension dimension = new Dimension( width, height );
        component.setMinimumSize( dimension );
        component.setMaximumSize( dimension );
        component.setPreferredSize( dimension );
    }

    protected void addContent( @NotNull JComponent component, @NotNull Consumer<JComponent> conComp, @NotNull Consumer<GridBagConstraints> conCons ) {
        GridBagConstraints constraints = new GridBagConstraints();
        conCons.accept( constraints );
        conComp.accept( component );
        add( component, constraints );
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        this.button = button;
        loadProperties( button );
    }
}