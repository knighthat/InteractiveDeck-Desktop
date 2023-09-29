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

import me.knighthat.interactivedeck.component.ContentContainer;
import me.knighthat.interactivedeck.component.Flexible;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.icon.TaskIcon;
import me.knighthat.interactivedeck.menus.modifier.IButtonProperty;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.task.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TaskSelector extends JScrollPane implements IButtonProperty, Flexible, ContentContainer<Void> {

    private final @NotNull JPanel iconDisplay;
    private final @NotNull TaskConfiguratorPanel configuratorPanel;
    private final @NotNull List<TaskIcon> icons;

    public TaskSelector( @NotNull TaskConfiguratorPanel linkTo ) {
        super( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        this.icons = new ArrayList<>();
        this.iconDisplay = new JPanel();
        this.configuratorPanel = linkTo;
        setupLayout();
        initComponents();
    }

    //<editor-fold desc="Layout">
    private void setupLayout() {
        ScrollPaneLayout layout = new ScrollPaneLayout();
        setLayout( layout );
        setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
    }
    //</editor-fold>

    //<editor-fold desc="Components">
    private void initComponents() {
        container().setDoubleBuffered( false );
        container().setBackground( new Color( 51, 51, 51 ) );
        container().setLayout( new GridLayout( 0, 4, 10, 10 ) );

        loadTaskIcons();

        setViewportView( container() );
    }
    //</editor-fold>

    private void selectIcon( @Nullable Class<? extends Task> clazz ) {
        for (TaskIcon icon : icons)
            icon.setSelect( icon.taskType() == clazz );
        configuratorPanel.present( clazz );
    }

    private void addIcon( @NotNull SVGDocument document, @NotNull Class<? extends Task> clazz ) {
        TaskIcon icon = new TaskIcon( document, clazz );
        icons.add( icon );
        addContent(
                icon,
                comp -> {
                    setDimension( icon, 40, 40 );

                    icon.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            selectIcon( clazz );
                        }
                    } );
                },
                constraints -> {}
        );
    }

    private void loadTaskIcons() {
        for (TaskAction task : TaskAction.values())
            addIcon( task.icon, task.type );
    }

    @Override
    public @NotNull JPanel container() {
        return iconDisplay;
    }

    @Override
    public @NotNull Void constraints() {
        return null;
    }

    @Override
    public void updateSelectedButton( @NotNull IButton button ) {
        Task task = button.task();
        selectIcon( task != null ? task.getClass() : null );
    }
}
