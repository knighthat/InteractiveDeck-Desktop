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

import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.task.TaskAction;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TaskConfiguratorPanel extends JScrollPane implements Flexible {

    private static JPanel nullPanel;

    private final @NotNull Map<Class<? extends Task>, TaskConfigurator> configurators;

    private @Nullable TaskConfigurator active;

    public TaskConfiguratorPanel() {
        super( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.configurators = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        nullPanel = new JPanel();
        nullPanel.setBackground( ColorUtils.DEFAULT_DARK );
        initConfigurators();
        present( null );
    }

    private void initConfigurators() {
        for (TaskAction task : TaskAction.values())
            try {
                var constructor = task.configurator.getConstructor();
                TaskConfigurator configurator = constructor.newInstance();
                configurators.put( task.type, configurator );
            } catch (ReflectiveOperationException e) {
                Throwable throwable = e.getCause() != null ? e.getCause() : e;
                Log.exc( "Failed to setup configurator " + task.name(), throwable, true );
                Log.reportBug();
            }
    }

    public void present( @Nullable Class<? extends Task> taskType ) {
        if (taskType == null) {
            setViewportView( nullPanel );
            active = null;
            return;
        }

        active = configurators.get( taskType );
        if (active == null) {
            Log.err( "Configurator for " + taskType.getSimpleName() + " does not exist!" );
            Log.reportBug();
        } else
            setViewportView( active );
    }

    public @NotNull TaskConfigurator[] configurators() {
        return configurators.values().toArray( TaskConfigurator[]::new );
    }

    public @Nullable TaskConfigurator active() {
        return this.active;
    }
}
