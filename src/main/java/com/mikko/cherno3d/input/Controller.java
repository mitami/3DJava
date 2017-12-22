package com.mikko.cherno3d.input;

public class Controller {

    public double x, z, rotation, xa, za, rotationa;
    /*
        Again we are using static booleans so we can use and change their values
        in the "Display.java" class' "run()" method, without creating a new
        instance of this class, to implement movement of the camera with the mouse.
    */
    public static boolean turnLeft = false;
    public static boolean turnRight = false;

    public void tick(boolean forward, boolean back, boolean left, boolean right) {
        double rotationSpeed = 0.025;
        double walkSpeed = 1.0;
        double xMove = 0.0;
        double zMove = 0.0;

        if (forward) {
            zMove++;
        }
        if (back) {
            zMove--;
        }
        if (right) {
            xMove++;
        }
        if (left) {
            xMove--;
        }
        if (turnLeft) {
            rotationa -= rotationSpeed;
        }
        if (turnRight) {
            rotationa += rotationSpeed;
        }

        xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += xa;
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotationa *= 0.5;
    }
}
