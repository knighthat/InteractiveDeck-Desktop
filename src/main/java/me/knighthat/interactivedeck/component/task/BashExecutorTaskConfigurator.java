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
import me.knighthat.interactivedeck.component.ui.UILabel;
import me.knighthat.interactivedeck.task.BashExecutor;
import me.knighthat.interactivedeck.task.TaskManager;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class BashExecutorTaskConfigurator extends TaskConfigurator {

    private JTextField pathInput;

    public BashExecutorTaskConfigurator() {
        super();
        setBackground( ColorUtils.DEFAULT_DARK );
    }

    @Override
    public void applyTo( @NotNull IButton button ) {
        String path = pathInput.getText().trim();
        button.task( TaskManager.create( BashExecutor.class, path ) );
    }

    @Override
    public void setupLayout( @NotNull GridBagLayout layout ) {
    }

    @Override
    public void initComponents() {
        this.pathInput = new JTextField();
        addContent(
                new UILabel( "Path to bash file" ),
                comp -> {},
                constraints -> constraints.anchor = GridBagConstraints.LINE_START
        );
        addContent(
                pathInput,
                comp -> setDimension( comp, 200, 40 ),
                constraints -> constraints.gridy = 1
        );
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        pathInput.setText( "" );
        if (!( button.task() instanceof BashExecutor bashExec ))
            return;
        pathInput.setText( bashExec.path() );
    }
}
