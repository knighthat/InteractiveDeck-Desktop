/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package me.knighthat.interactivedeck.utils;

import java.awt.Color;
import java.util.jar.Attributes;

/**
 *
 * @author knighthat
 */
public enum Status {
    
    DISCONNECTED(Color.RED, "Disconnected"),
    CONNECTED(Color.GREEN, "Connected"),
    UNKNOWN(Color.GRAY, "ERROR");
    
    private final Color color;
    private final String status;
    
    Status(Color color, String status) {
        this.color = color;
        this.status = status;
    }
    
    public final Color getColor() {
        return this.color;
    }

    public final String getStatus() {
        return this.status;
    }    
}
