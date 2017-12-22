package com.mikko.cherno3d.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener{
    
    /*
        The boolean array represents the keys and the false/true tells if the key
        is pressed at the moment or not. See methods "keyPressed()" and 
        "keyReleased()"
    */
    public boolean[] key = new boolean[68836];
    public static int mouseX;
    public static int mouseY;

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    /*
        This method reads the keyCode of the pressed key and saves it in the
        boolean array called "key[]" in the spot that is equal to the value of
        the keyCode itself. (See the creation of the array above) Later the
        boolean value of if the key id pressed or not can be found by searching
        the "key[]" array with the value of the desired keys keycode. If the spot
        in the array is true, then the key is pressed.
    */
    @Override
    public void keyPressed(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        if(keyCode > 0 && keyCode < key.length) {
            key[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        if(keyCode > 0 && keyCode < key.length) {
            key[keyCode] = false;
        }
    }

    @Override
    public void focusGained(FocusEvent fe) {
        
    }

    /*
        Makes it so that when you lose focus of the animation window (click
        sowhere outside of it) the key presses no longer affect to rendering.
    */
    @Override
    public void focusLost(FocusEvent fe) {
        for (int i = 0; i < key.length; i++) {
            key[i] = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }
    
}
