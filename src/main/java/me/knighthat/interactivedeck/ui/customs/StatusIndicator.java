/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.knighthat.interactivedeck.ui.customs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import me.knighthat.interactivedeck.utils.Status;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author knighthat
 */
public class StatusIndicator extends JLabel {

    private int d = 10;  // Diameter
    
    private @NotNull Status status = Status.UNKNOWN;    
    
    private final JLabel statusLabel;

    public StatusIndicator(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }    
    
    public void setStatus(Status status) {
        this.status = status;
        setBackground(status.getColor());
        this.statusLabel.setText(status.getStatus());
    }
    
    public Status getStatus() {
        return this.status;
    } 
   
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics.setColor(getBackground());
        
        double r = d / 2;
        int wStart = (int) Math.floor((getWidth() / 2) - r);
        int hStart = (int) Math.floor((getHeight() / 2) - r);
   
        graphics.fillOval(wStart, hStart, d, d);
       
    }    
}
