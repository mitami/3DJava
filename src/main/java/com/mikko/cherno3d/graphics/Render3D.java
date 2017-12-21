package com.mikko.cherno3d.graphics;

import com.mikko.cherno3d.Game;

public class Render3D extends Render {

    public Render3D(int width, int height) {
        super(width, height);
    }

    public void floor(Game game) {
        /*
            The variables floorPosition and ceilingPosition can be used to make
            the ceiling or floor appear to be higher or lower.
        */
        double floorPosition = 8.0;
        double ceilingPosition = 8.0;
        double forward = game.controls.z;
        double right = game.controls.x;
        
        double rotation = game.controls.rotation;
        /*
            The sine and cosine together are used to achieve a circular motion.
            The sine and cosine wave are kind of the opposites, so when the sine
            wave changes direction at the half circle, the cosine does the same
            but in the opposite direction. So by using them both, we can make
            a whole circle. (-- Google for more info on this --)
        */
        double cosine = Math.cos(rotation);
        double sine = Math.sin(rotation);

        for (int y = 0; y < height; y++) {
            double ceiling = (y - height / 2.0) / height;

            double z = floorPosition / ceiling;

            if (ceiling < 0) {
                z = ceilingPosition / -ceiling;
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
            }
        }
    }
}
