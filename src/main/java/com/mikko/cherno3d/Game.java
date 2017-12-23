package com.mikko.cherno3d;

import com.mikko.cherno3d.input.Controller;
import java.awt.event.KeyEvent;

public class Game {
    
    public int time;
    public Controller controls;
    
    public Game() {
        controls = new Controller();
    }
    
    public void tick(boolean[] key) {
        /*
            You can make the square move faster (<-- testing phase) by adding more than +1 to the
            variable "time" for example "time += 4"
        */
        time++;
        /*
            Here we use the boolean array "key[]" (See InputHandler.java) to find if the keys we have
            defined some function for (the constants KeyEvent.XXXX) are pressed or not.
        */
        boolean forward = key[KeyEvent.VK_W];
        boolean back = key[KeyEvent.VK_S];
        boolean left = key[KeyEvent.VK_A];
        boolean right = key[KeyEvent.VK_D];
        boolean jump = key[KeyEvent.VK_SPACE];
        boolean crouch = key[KeyEvent.VK_CONTROL];
        boolean sprint = key[KeyEvent.VK_SHIFT];
        
        controls.tick(forward, back, left, right, jump, crouch, sprint);
    }
}
