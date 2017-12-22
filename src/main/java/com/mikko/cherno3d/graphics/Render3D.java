package com.mikko.cherno3d.graphics;

import com.mikko.cherno3d.Game;

public class Render3D extends Render {

    public double[] zBuffer;
    private double renderDistance = 5000.0;
    
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
        double ceilingPosition = 800;
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
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
                
                /*
                    This if statement is used to give limits to where to stop rendering.
                    It can be used with the z (depth), x (width) and y (height)
                    variables. You can easily see what it does by changing the
                    values in the if() check. Basically used with the z variable
                    to limit the rendering distance.
                */
                if(z > 500) {
                    pixels[x+y*width] = 0;
                }
                
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
            
            if(brightness > 255) {
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
