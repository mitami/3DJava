package com.mikko.cherno3d.graphics;

import com.mikko.cherno3d.Game;
import java.util.Random;

public class Screen extends Render {

    private Render test;
    private Render3D renderer;

    public Screen(int width, int height) {
        super(width, height);
        Random random = new Random();
        test = new Render(256, 256);
        renderer = new Render3D(width, height);

        for (int i = 0; i < 256 * 256; i++) {
            test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
        }
    }

    public void render(Game game) {

        /*This loop sets the pixels to 0 each frame, so right before the new 
            picture is drawn in a new place, all the pixels are set to 0, so
            the old picture disappears. This removes the trail the "moving"
            picture leaves behind. The trail is actually the pictures drawn in
            the previous frames which are left there.
         */
        for (int i = 0; i < width * height; i++) {
            pixels[i] = 0;
        }

        /*
            This loop actually draws the picture using the method "draw()". The values "anim" and "anim2"
            are used as the offset for the pixels that are being drawn. This
            means that as these values change (according to the time variable)
            the point where the drawing of the picture is started from, changes.
            This makes it so that the picture appears to be moving as its drawn
            again right next to where it previously was. (See the method "draw()"
            in the class "Render.java")
         */
        for (int i = 0; i < 50; i++) {
            int anim = (int) (Math.sin((game.time + i) % 1000.0 / 100) * 100);
            int anim2 = (int) (Math.cos((game.time + i) % 1000.0 / 100) * 100);
            
        }
        
        renderer.floor(game);
        renderer.renderDistanceLimiter();
        renderer.walls();
        draw(renderer, 0, 0);
    }

}
