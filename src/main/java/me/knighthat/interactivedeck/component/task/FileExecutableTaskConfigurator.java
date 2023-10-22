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
import me.knighthat.interactivedeck.task.ExecutableFile;
import me.knighthat.interactivedeck.task.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class FileExecutableTaskConfigurator extends TaskConfigurator {

    protected JTextField pathInput;

    public FileExecutableTaskConfigurator( @NotNull Class<? extends Task> taskType ) {
        super( taskType );
    }

    public @Nullable Object[] taskParams() {
        return new Object[]{ pathInput.getText().trim() };
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        pathInput.setText( "" );
        if (!( button.getTask() instanceof ExecutableFile file ))
            return;
        pathInput.setText( file.filePath() );
    }
}
