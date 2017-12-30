package com.mikko.cherno3d.input;

import com.mikko.cherno3d.Display;

public class Controller {

    public double x, y, z, rotation, xa, za, rotationa;
    /*
        Again we are using static booleans so we can use and change their values
        in the "Display.java" class' "run()" method, without creating a new
        instance of this class, to implement movement of the camera with the mouse.
     */
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean walk = false;

    public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
        double rotationSpeed = 0.0025 * Display.mouseSpeed;
        double walkSpeed = 0.5;
        double jumpHeight = 0.5;
        double crouchHeight = 0.3;
        double xMove = 0.0;
        double zMove = 0.0;
        walk = false;

        if (forward) {
            zMove++;
            walk = true;
        }
        if (back) {
            zMove--;
            walk = true;
        }
        if (right) {
            xMove++;
            walk = true;
        }
        if (left) {
            xMove--;
            walk = true;
        }
        if (turnLeft) {
            rotationa -= rotationSpeed;
        }
        if (turnRight) {
            rotationa += rotationSpeed;
        }

        

        if (sprint) {
            walkSpeed = 1;
        }
        if (jump) {
            y += jumpHeight;
            sprint = false;
            walkSpeed = 0.5;
            walk = false;
        }
        
        if (crouch) {
            y -= crouchHeight;
            //sprint = false;
            walkSpeed = 0.25;
        }
        
        
        
        xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += xa;
        y *= 0.9;
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotationa *= 0.5;

    }
}
