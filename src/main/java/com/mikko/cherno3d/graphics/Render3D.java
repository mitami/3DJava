package com.mikko.cherno3d.graphics;

import com.mikko.cherno3d.Game;
import com.mikko.cherno3d.input.Controller;
import java.util.Random;

public class Render3D extends Render {

    public double[] zBuffer;
    private double renderDistance = 5000.0;
    private double forward, right, cosine, sine, up, walking;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void floor(Game game) {
        /*
            The variables floorPosition and ceilingPosition can be used to make
            the ceiling or floor appear to be higher or lower.
         */
        double floorPosition = 8.0;
        /*
            If we change the ceilingPosition variable to a big enough value, the
            renderDistanceLimiter() method will cut it out altogether.
         */
        double ceilingPosition = 8.0;
        forward = game.controls.z;
        right = game.controls.x;
        up = game.controls.y;
        /*
            Called "walking" but actually this represents the "head bob" while
            the camera is moving, and doesn't have any effect on the walking
            speed etc.
         */
        walking = Math.sin(game.time / 6.0) * 0.5;

        double rotation = game.controls.rotation;
        /*
            The sine and cosine together are used to achieve a circular motion.
            The sine and cosine wave are kind of the opposites, so when the sine
            wave changes direction at the half circle, the cosine does the same
            but in the opposite direction. So by using them both, we can make
            a whole circle. (-- Google for more info on this --)
         */
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);

        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;

            double z = (floorPosition + up) / ceiling;
            if (Controller.walk) {
                z = (floorPosition + up + walking) / ceiling;
            }

            if (ceiling < 0) {
                z = (ceilingPosition - up) / -ceiling;
                if (Controller.walk) {
                    z = (ceilingPosition - up - walking) / -ceiling;
                }
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];

                /*
                    This if statement is used to give limits to where to stop rendering.
                    It can be used with the z (depth), x (width) and y (height)
                    variables. You can easily see what it does by changing the
                    values in the if() check. Basically used with the z variable
                    to limit the rendering distance.
                 */
                if (z > 500) {
                    pixels[x + y * width] = 0;
                }

            }
        }
        //Controller.walk = false;

        /*
            Here in "walls()" we are creating (rendering) additional objects on the screen
            (things other than the ceiling or floor)
            
         */
    }

    public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
        double upCorrect = 0.062;
        double rightCorrect = 0.062;
        double forwardCorrect = 0.062;
        double walkCorrect = -0.062;
        
        double xcLeft = ((xLeft) - (right * rightCorrect)) * 2;
        double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTL;
        double yCornerBL;
        if(Controller.walk) {
            yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
            yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
        } else {
            yCornerTL = ((-yHeight) - (-up * upCorrect)) * 2;
            yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect)) * 2;
        }
        
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = ((xRight) - (right * rightCorrect)) * 2;
        double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTR;
        double yCornerBR;
        if(Controller.walk) {
            yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
            yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
        } else {
            yCornerTR = ((-yHeight) - (-up * upCorrect)) * 2;
            yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect)) * 2;
        }
        
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

        if (xPixelLeft >= xPixelRight) {
            return;
        }

        int xPixelLeftInt = (int) (xPixelLeft);
        int xPixelRightInt = (int) (xPixelRight);

        if (xPixelLeftInt < 0) {
            xPixelLeftInt = 0;
        }
        if (xPixelRightInt > width) {
            xPixelRightInt = width;
        }

        double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
        double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
        double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
        double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

        double tex1 = 1.0 / rotLeftSideZ;
        double tex2 = 1.0 / rotRightSideZ;
        double tex3 = 0.0 / rotLeftSideZ;
        double tex4 = 8.0 / rotRightSideZ - tex3;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

            int xTexture = (int) ((tex3 + tex4 * pixelRotation) / (tex1 + (tex2 - tex1) * pixelRotation));

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if (yPixelTopInt < 0) {
                yPixelTopInt = 0;
            }
            if (yPixelBottomInt > height) {
                yPixelBottomInt = height;
            }

            for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
                double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                int yTexture = (int) (8 * pixelRotationY);
                pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 200;
                zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
            }
        }
    }

    /*
        This method gradually darkens the pixels the further down the distance they are,
        so that when the render distance limit is reached and the black pixels
        start, the transition will appear more subtle instead of just changing
        from full coloured pixels to entirely black ones like in the if statement
        above.
     */
    public void renderDistanceLimiter() {
        for (int i = 0; i < width * height; i++) {
            int colour = pixels[i];
            int brightness = (int) (renderDistance / (zBuffer[i]));

            if (brightness < 0) {
                brightness = 0;
            }

            if (brightness > 255) {
                brightness = 255;
            }

            int r = (colour >> 16) & 0xff;
            int g = (colour >> 8) & 0xff;
            int b = (colour) & 0xff;

            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}
