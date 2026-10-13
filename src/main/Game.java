package main;

import engine.Renderable;
import engine.Updatable;
import input.KeyboardInput;
import input.MouseInput;

import java.awt.Graphics;

public class Game implements Runnable, Renderable, Updatable {
    public static long TARGET_FPS = 120;
    public static long TARGET_UPS = 120;

    private volatile boolean keepRunning;
    private Thread gameThread;

    private GameWindow window;
    private GamePanel panel;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    // FIXME: Starting a thread inside the constructor creates a race condition with object initialization.
    // TODO: Refactor to use a static factory method (e.g., Game.launch()) or explicit game.start() call in Main.
    public Game() {
        initComponents();
        startGameLoop();
    }

    public void initComponents() {
        keyboard = new KeyboardInput();
        mouse = new MouseInput();

        panel = new GamePanel(this, keyboard, mouse);
        window = new GameWindow(panel);
        panel.requestFocus(); // Gets the input focus
    }

    public void startGameLoop() {
        keepRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGameLoop() {
        keepRunning = false;
    }

    @Override
    public void update(double deltaTimeS) {
        // TODO

        keyboard.endFrame();
        mouse.endFrame();
    }

    @Override
    public void render(Graphics g) {
        // TODO
    }

    @Override
    public void run() {
        double timePerFrameNS = 1_000_000_000.0 / TARGET_FPS;
        double timePerUpdateNS = 1_000_000_000.0 / TARGET_UPS;
        double deltaTimeS = 1.0 / TARGET_UPS;

        double deltaRender = 0.0;
        double deltaUpdate = 0.0;
        long lastTimeNS = System.nanoTime();

        while (keepRunning) {
            long currentTimeNS = System.nanoTime();
            long elapsedTimeNS = currentTimeNS - lastTimeNS;

            lastTimeNS = currentTimeNS;
            deltaRender += elapsedTimeNS / timePerFrameNS;
            deltaUpdate += elapsedTimeNS / timePerUpdateNS;

            while (deltaUpdate >= 1) {
                update(deltaTimeS);
                deltaUpdate--;
            }

            if (deltaRender >= 1) {
                panel.repaint();
                deltaRender--;
            }
        }
        // TODO: calculate remaining frame time and sleep to reduce CPU usage without dropping target FPS/UPS.
        // FIXME: Add thread cleanup and gameThread.join() on loop termination to avoid dangling background threads.
    }
}
