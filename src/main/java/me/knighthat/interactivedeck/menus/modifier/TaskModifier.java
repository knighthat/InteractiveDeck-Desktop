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
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.logging.Log;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.task.BashExecutor;
import me.knighthat.interactivedeck.task.GotoPage;
import me.knighthat.interactivedeck.task.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

import static me.knighthat.interactivedeck.file.Settings.SETTINGS;

public class TaskModifier extends ModifierPanel {

    private JRadioButton runScriptButton;
    private JTextField runScriptInput;
    private JRadioButton gotoButton;
    private ProfilesComboBox profilesList;

    private void buttonStateChange( @NotNull ItemEvent event ) {
        if (!( event.getSource() instanceof JRadioButton source ))
            return;

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

    private void applyButtonClicked() {
        Task task = null;

        if (this.gotoButton.isSelected())
            task = createGotoPageTask();
        if (this.runScriptButton.isSelected())
            task = createBashExecutorTask();

        button.task( task );
    }

    private @Nullable Task createGotoPageTask() {
        Profile selected = (Profile) profilesList.getSelectedItem();
        return selected != null ? new GotoPage( selected.uuid ) : null;
    }

    private @Nullable Task createBashExecutorTask() {
        String filePath = this.runScriptInput.getText();
        if (filePath.isBlank())
            return null;

        File file = new File( filePath );
        if (!file.exists()) {
            Log.warn( "File \"" + file.getAbsolutePath() + "\" does NOT exist!" );
            return null;
        }
        return new BashExecutor( file );
    }

    private void addSection( @NotNull Consumer<JPanel> component, @NotNull Consumer<GridBagConstraints> conCons ) {
        JPanel panel = new JPanel( new GridBagLayout() );
        setDimension( panel, 250, 150 );
        panel.setOpaque( false );
        component.accept( panel );
        addContent( panel, comp -> {}, conCons );
    }

    private void addSection( @NotNull JRadioButton button, @NotNull JComponent input, int gridy ) {
        button.setFont( SETTINGS.UIFont() );
        button.setForeground( Color.WHITE );
        button.addItemListener( this::buttonStateChange );
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridy = 0;
        buttonConstraints.anchor = GridBagConstraints.NORTHWEST;

        setDimension( input, 200, 40 );
        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridy = 1;
        inputConstraints.anchor = GridBagConstraints.NORTH;

        addSection(
                panel -> {
                    panel.add( button, buttonConstraints );
                    panel.add( input, inputConstraints );
                },
                constraints -> constraints.gridy = gridy
        );
    }

    private void loadTask( @NotNull IButton button ) {
        runScriptButton.setSelected( false );
        runScriptInput.setText( "" );

        gotoButton.setSelected( false );
        if (profilesList.getModel().getSize() == 0) {
            gotoButton.setEnabled( false );
            profilesList.setEnabled( false );
        } else
            profilesList.setSelectedIndex( 0 );

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

    @Override
    protected void configureLayout( @NotNull GridBagLayout layout ) {
        layout.columnWidths = new int[]{ 210 };
    }

    @Override
    protected void loadContent() {
        runScriptButton = new JRadioButton( "Execute" );
        runScriptInput = new JTextField();
        addSection( runScriptButton, runScriptInput, 0 );

        gotoButton = new JRadioButton( "Go To Page" );
        profilesList = new ProfilesComboBox();
        addSection( gotoButton, profilesList, 1 );


        addSection(
                panel -> {
                    JButton applyButton = new JButton( "Apply" );
                    setDimension( applyButton, 140, 30 );
                    applyButton.addMouseListener( new MouseAdapter() {
                        @Override
                        public void mouseClicked( MouseEvent e ) {
                            applyButtonClicked();
                        }
                    } );
                    panel.add( applyButton );
                },
                constraints -> constraints.gridy = 2
        );
    }

    @Override
    protected void loadProperties( @NotNull IButton button ) {
        MenuProperty.profile( button.profile ).ifPresent( profile -> {
            ComboBoxModel<Profile> model = new DefaultComboBoxModel<>( MenuProperty.profileArray() );
            profilesList.removeAll();
            profilesList.setModel( model );
            profilesList.removeItem( profile );
            profilesList.revalidate();
            profilesList.repaint();
        } );
        loadTask( button );
    }
}
