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
import me.knighthat.interactivedeck.component.task.TaskConfigurator;
import me.knighthat.interactivedeck.component.task.TaskConfiguratorPanel;
import me.knighthat.interactivedeck.component.task.TaskSelector;
import me.knighthat.interactivedeck.component.ui.UIButton;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.task.TaskManager;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class TaskModifier extends ModifierPanel<BorderLayout, Void> {

    private final @NotNull TaskSelector taskSelector;
    private final @NotNull TaskConfiguratorPanel configuratorPanel;

    public TaskModifier() {
        this.configuratorPanel = new TaskConfiguratorPanel();
        this.taskSelector = new TaskSelector( configuratorPanel );
    }

    private void addContent( @NotNull JComponent component,
                             @NotNull Consumer<JComponent> componentProps,
                             @MagicConstant( flagsFromClass = BorderLayout.class ) String position ) {
        componentProps.accept( component );
        container().add( component, position );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        taskSelector.updateSelectedButton( button );
        for (TaskConfigurator configurator : configuratorPanel.configurators())
            configurator.updateSelectedButton( button );
    }

    @Override
    protected @NotNull BorderLayout initLayout() {
        return new BorderLayout();
    }

    @Override
    public @NotNull Void constraints() {
        return null;
    }

    @Override
    public void initComponents() {
        addContent(
                new JPanel( new GridBagLayout() ),
                panel -> {
                    panel.setOpaque( false );

                    setDimension( panel, 250, 105 );
                    setDimension( taskSelector, 230, 90 );

                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.CENTER;
                    constraints.insets = new Insets( 5, 0, 10, 0 );

                    panel.add( taskSelector, constraints );
                },
                BorderLayout.NORTH
        );
        addContent(
                configuratorPanel,
                comp -> {
                    comp.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
                },
                BorderLayout.CENTER
        );
        addContent(
                new JPanel( new GridBagLayout() ),
                panel -> {
                    panel.setOpaque( false );
                    setDimension( panel, 250, 70 );

                    UIButton button = new UIButton( "Apply" );
                    setDimension( button, 150, 40 );
                    button.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            configuratorPanel.active().ifPresent( configurator -> {
                                IButton button = TaskModifier.super.button;
                                Task task = TaskManager.create( configurator.taskType, configurator.taskParams() );
                                button.setTask( task );
                            } );
                        }
                    } );
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.CENTER;

                    panel.add( button, constraints );
                },
                BorderLayout.SOUTH
        );
    }
}
