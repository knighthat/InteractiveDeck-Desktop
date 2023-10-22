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
import me.knighthat.interactivedeck.task.RunJarFile;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class RunJarFileTaskConfigurator extends FileExecutableTaskConfigurator {

    private JTextField vmArgsInput;
    private JTextField appArgsInput;

    public RunJarFileTaskConfigurator() {
        super( RunJarFile.class );
        setBackground( ColorUtils.DEFAULT_DARK );
    }

    private void addSection( @NotNull String label, @NotNull JTextField input, int gridy ) {
        addContent(
                new UILabel( label ),
                comp -> ( (JLabel) comp ).setLabelFor( input ),
                constraints -> {
                    constraints.gridy = gridy;
                    constraints.anchor = GridBagConstraints.LINE_START;
                }
        );
        addContent(
                input,
                comp -> setDimension( comp, 210, 40 ),
                constraints -> {
                    constraints.gridy = gridy + 1;
                    constraints.insets = new Insets( 0, 0, 20, 0 );
                }
        );
    }

    private @NotNull String mergeArgs( @NotNull String[] args ) {
        StringJoiner joiner = new StringJoiner( " " );
        for (String arg : args)
            joiner.add( arg );
        return joiner.toString();
    }

    private @NotNull String[] splitArgs( @NotNull String args ) {
        if (args.isBlank())
            return new String[0];

        List<String> argList = new ArrayList<>( 0 );
        for (String arg : args.split( " " ))
            argList.add( arg.trim() );
        return argList.toArray( String[]::new );
    }

    @Override
    public @Nullable Object[] taskParams() {
        String path = pathInput.getText().trim();
        String[] args = splitArgs( appArgsInput.getText() );
        String[] vmArgs = splitArgs( vmArgsInput.getText() );
        return new Object[]{ path, args, vmArgs };
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        super.updateSelectedButton( button );

        if (button.getTask() instanceof RunJarFile task) {
            String vmArgs = mergeArgs( task.vmArgs() );
            vmArgsInput.setText( vmArgs );

            String appArgs = mergeArgs( task.args() );
            appArgsInput.setText( appArgs );
        }
    }

    @Override
    public void initComponents() {
        addSection( "JAR File", pathInput = new JTextField(), 0 );
        addSection( "VM Arguments", vmArgsInput = new JTextField(), 2 );
        addSection( "Application Arguments", appArgsInput = new JTextField(), 4 );
    }
}
