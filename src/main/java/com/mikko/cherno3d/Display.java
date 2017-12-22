package com.mikko.cherno3d;

import com.mikko.cherno3d.graphics.Render;
import com.mikko.cherno3d.graphics.Screen;
import com.mikko.cherno3d.input.Controller;
import com.mikko.cherno3d.input.InputHandler;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "3D test 0.01";

    private Thread thread;
    private Screen screen;
    private Game game;
    private BufferedImage img;
    private boolean running = false;
    private Render render;
    private int[] pixels;
    private InputHandler input;
    private int newX = 0;
    private int newY = 0;
    private int oldX = 0;
    private int oldY = 0;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        game = new Game();
        screen = new Screen(WIDTH, HEIGHT);
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        
        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    private void start() {
        if (running) {
            return;
        }

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            thread.join();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public void run() {
        /*
            These variables are for an fps counter. The counter prints the fps
            to the console.
         */
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    System.out.println(frames + "FPS");
                    previousTime += 1000;
                    frames = 0;
                }
            }
            if (ticked) {
                frames++;
            }
            render();
            
            newX = InputHandler.mouseX;
            if(newX > oldX) {
                Controller.turnRight = true;
            }
            if(newX < oldX) {
                Controller.turnLeft = true;
            }
            if(newX == oldX) {
                Controller.turnLeft = false;
                Controller.turnRight = false;
            }
            oldX = newX;

        }
    }

    private void tick() {
        game.tick(input.key);
    }

    /*
        This method calls the other "render()" method in the class "Screen"
        which modifies the values of the int pixels[] array.
     */
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();

        bs.show();
    }

    public static void main(String[] args) {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setResizable(false);
        frame.getContentPane().setCursor(blank);
        frame.pack();
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();

    }
}
