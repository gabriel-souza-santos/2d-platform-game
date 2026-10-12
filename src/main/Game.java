package main;

import java.util.concurrent.TimeUnit;

public class Game implements Runnable {
    public static long FPS_SET = 120;

    private static volatile boolean keepRunning;
    private static Thread gameThread;

    private static GameWindow window;
    private static GamePanel panel;

    public Game() {
        initComponents();
        startGameLoop();
    }

    public void initComponents() {
        panel = new GamePanel();
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
    public void run() {
        long timePerFrameNS = TimeUnit.SECONDS.toNanos(1) / FPS_SET;
        long startTimeNS = System.nanoTime();

        while (keepRunning) {
            long elapsedTimeNS = System.nanoTime() - startTimeNS;

            if (elapsedTimeNS >= timePerFrameNS) {
                panel.repaint();
                startTimeNS = System.nanoTime();
            }
        }
    }
}
