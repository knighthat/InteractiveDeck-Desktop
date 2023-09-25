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
import me.knighthat.interactivedeck.component.plist.ProfilesComboBox;
import me.knighthat.interactivedeck.component.ui.UIButton;
import me.knighthat.interactivedeck.component.ui.UIComponent;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.task.BashExecutor;
import me.knighthat.interactivedeck.task.GotoPage;
import me.knighthat.interactivedeck.task.Task;
import me.knighthat.interactivedeck.task.TaskManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TaskModifier extends ModifierPanel {

    private final @NotNull JRadioButton runScriptButton;
    private final @NotNull JTextField runScriptInput;
    private final @NotNull JRadioButton gotoButton;
    private final @NotNull ProfilesComboBox profilesList;

    public TaskModifier() {
        this.runScriptButton = new JRadioButton( "Execute" );
        this.runScriptInput = new JTextField();
        this.gotoButton = new JRadioButton( "Go To Page" );
        this.profilesList = new ProfilesComboBox();
    }

    private void addSection( @NotNull Consumer<JPanel> component, @NotNull Consumer<GridBagConstraints> constraintsConsumer ) {
        JPanel panel = new JPanel( new GridBagLayout() );
        setDimension( panel, 250, 150 );
        panel.setOpaque( false );
        component.accept( panel );
        addContent( panel, comp -> {}, constraintsConsumer );
    }


    private void addTaskSection( @NotNull JRadioButton button, @NotNull JComponent input, int gridy ) {
        UIComponent.applyPresets( button );
        button.setForeground( Color.WHITE );
        button.addItemListener( this::buttonSelectedEvent );

        GridBagConstraints btnConstraints = new GridBagConstraints();
        btnConstraints.gridy = 0;
        btnConstraints.anchor = GridBagConstraints.NORTHWEST;

        setDimension( input, 200, 40 );

        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridy = 1;
        inputConstraints.anchor = GridBagConstraints.NORTH;

        addSection(
                panel -> {
                    panel.add( button, btnConstraints );
                    panel.add( input, inputConstraints );
                },
                constraints -> constraints.gridy = gridy
        );
    }

    private void buttonSelectedEvent( @NotNull ItemEvent event ) {
        JRadioButton source = (JRadioButton) event.getSource();

        if (source.isSelected()) {
            if (source == runScriptButton) {
                this.runScriptInput.setEnabled( true );
                profilesList.setEnabled( false );
                this.gotoButton.setSelected( false );
            } else if (source == gotoButton) {
                profilesList.setEnabled( true );
                runScriptInput.setEnabled( false );
                runScriptButton.setSelected( false );
            }
        } else {
            if (source == runScriptButton) {
                runScriptInput.setEditable( false );
            } else if (source == gotoButton) {
                profilesList.setEditable( false );
            }
        }
    }

    private void applyButtonClickedEvent( @NotNull MouseEvent event ) {
        Class<? extends Task> clazz = null;
        List<Object> params = new ArrayList<>();

        if (this.gotoButton.isSelected()) {
            clazz = GotoPage.class;
            params.add( profilesList.getSelectedItem() );
        }
        if (this.runScriptButton.isSelected()) {
            clazz = BashExecutor.class;
            params.add( runScriptInput.getText() );
        }

        if (clazz != null) {
            Task task = TaskManager.create( clazz, params.toArray() );
            button.task( task );
        }
    }

    @Override
    public void setupLayout( @NotNull GridBagLayout layout ) {
        layout.columnWidths = new int[]{ 250 };
        layout.rowHeights = new int[]{ 150, 150, 150 };
    }

    @Override
    public void initComponents() {
        addTaskSection( runScriptButton, runScriptInput, 0 );
        addTaskSection( gotoButton, profilesList, 1 );
        addSection(
                panel -> {
                    UIButton button = new UIButton( "Apply" );
                    setDimension( button, 140, 30 );
                    button.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            applyButtonClickedEvent( e );
                        }
                    } );

                    panel.add( button );
                },
                constraints -> constraints.gridy = 2
        );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        // Reset everything to default value first
        runScriptButton.setSelected( false );
        runScriptInput.setText( "" );

        gotoButton.setSelected( false );
        MenuProperty.profile( button.profile ).ifPresent( profilesList::reloadExcept );
        if (profilesList.getModel().getSize() == 0) {
            gotoButton.setEnabled( false );
            profilesList.setEnabled( false );
        } else
            profilesList.setSelectedIndex( 0 );

        // Display task
        Task task = button.task();
        if (task instanceof BashExecutor executor) {
            runScriptButton.setSelected( true );
            runScriptInput.setText( executor.path() );
        } else if (task instanceof GotoPage gotoPage) {
            gotoButton.setSelected( true );

            MenuProperty
                    .profile( gotoPage.target() )
                    .ifPresent( profilesList::setSelectedItem );
        }
    }
}
