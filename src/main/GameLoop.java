package main;

import engine.Updatable;

public class GameLoop implements Runnable {
    public static final long TARGET_FPS = 120;
    public static final long TARGET_UPS = 120;

    private volatile boolean keepRunning = false;
    private volatile long timePlayedNS = 0;
    private volatile int currentFPS = 0;
    private volatile int currentUPS = 0;

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

    public int getCurrentFPS() {
        return currentFPS;
    }

    public int getCurrentUPS() {
        return currentUPS;
    }

    @Override
    public void run() {
        double timePerFrameNS = 1_000_000_000.0 / TARGET_FPS;
        double timePerUpdateNS = 1_000_000_000.0 / TARGET_UPS;
        double deltaTimeS = 1.0 / TARGET_UPS;

        long lastTimeNS = System.nanoTime();
        double deltaRender = 0.0;
        double deltaUpdate = 0.0;

        long statsTimerNS = lastTimeNS;
        int frames = 0;
        int updates = 0;

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
                updates++;
            }

            if (deltaRender >= 1.0) {
                panel.repaint();
                deltaRender--;
                frames++;
            }

            if (currentTimeNS - statsTimerNS >= 1_000_000_000L) {
                currentFPS = frames;
                currentUPS = updates;

                frames = 0;
                updates = 0;

                statsTimerNS += 1_000_000_000L;
            }

            // FIXME: Busy-wait spin loop — this pegs one CPU core at 100% usage.
            //   Calculate the remaining time until the next frame/update is due and sleep
            //   using Thread.sleep() or LockSupport.parkNanos() to yield the CPU.
        }
    }
}
