package main;

import engine.Updatable;

public class GameLoop implements Runnable {
    public static final long TARGET_FPS = 120;
    public static final long TARGET_UPS = 120;

    private volatile boolean keepRunning = false;
    private volatile long timePlayedNS = 0;

    private final Updatable game;
    private final GamePanel panel;

    public GameLoop(Updatable game, GamePanel panel) {
        this.game = game;
        this.panel = panel;
    }

    public void start() {
        keepRunning = true;
    }

    public void stop() {
        keepRunning = false;
    }

    public long getTimePlayedNS() {
        return timePlayedNS;
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
            timePlayedNS += elapsedTimeNS;

            while (deltaUpdate >= 1.0) {
                game.update(deltaTimeS);
                deltaUpdate--;
            }

            if (deltaRender >= 1.0) {
                panel.repaint();
                deltaRender--;
            }

            // TODO: calculate remaining frame time and sleep to reduce CPU usage without dropping target FPS/UPS.
        }
    }
}
