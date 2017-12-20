package com.mikko.cherno3d.graphics;

import com.mikko.cherno3d.Display;

public class Render {

    public final int width;
    public final int height;
    public final int[] pixels;

    public Render(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    /*
        This method changes the values of the integers in the int array "pixels[]"
        Each integer in the "pixels[]" represents one pixel to be drawn on the screen.
        The integers "xOffset" and "yOffset" determine the starting point for each
        vertical and horizontal row of pixels.
     */
    public void draw(Render render, int xOffset, int yOffset) {
        for (int y = 0; y < render.height; y++) {
            int yPix = y + yOffset;
            if (yPix < 0 || yPix >= Display.HEIGHT) {
                continue;
            }

            for (int x = 0; x < render.width; x++) {
                int xPix = x + xOffset;
                if (xPix < 0 || xPix >= Display.WIDTH) {
                    continue;
                }

                /*
                    This alpha variable and the if-check enables the program to
                    not render some pixels in a way that gives "transparency" to
                    pixels that are empty. Now when the new picture is drawn partially 
                    on top of the old one, if the new one has empty pixels, they
                    wont overwrite the old pictures pixels at that spot with black,
                    and instead they leave it the way it is. (i think that's how
                    it works -- Check somewhere)
                */
                int alpha = render.pixels[x + y * render.width];

                if (alpha > 0) {
                    pixels[xPix + yPix * width] = alpha;
                }

            }
        }
    }
}
