package main;

import engine.Updatable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public final class GameLoop implements Runnable {
    public static final long TARGET_FPS = 120;
    public static final long TARGET_UPS = 120;

    private static final long NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);
    private static final long SPIN_THRESHOLD_NS = TimeUnit.MILLISECONDS.toNanos(1);

    private volatile boolean keepRunning = false;
    private volatile long timePlayedNS = 0;
    private volatile int currentFps = 0;
    private volatile int currentUps = 0;

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

    public int getCurrentFps() {
        return currentFps;
    }

    public int getCurrentUps() {
        return currentUps;
    }

    @Override
    public void run() {
        long timePerFrameNS = NANOS_PER_SECOND / TARGET_FPS;
        long timePerUpdateNS = NANOS_PER_SECOND / TARGET_UPS;
        double deltaTimeS = 1.0 / TARGET_UPS;

        long lastTimeNS = System.nanoTime();
        double deltaRender = 0.0;
        double deltaUpdate = 0.0;

        long statsTimerNS = lastTimeNS;
        int frames = 0;
        int updates = 0;

        long nextUpdateNS = lastTimeNS + (long) timePerUpdateNS;
        long nextFrameNS = lastTimeNS + (long) timePerFrameNS;

        while (keepRunning) {
            long currentTimeNS = System.nanoTime();
            long elapsedTimeNS = currentTimeNS - lastTimeNS;

            lastTimeNS = currentTimeNS;
            deltaRender += (double) elapsedTimeNS / timePerFrameNS;
            deltaUpdate += (double) elapsedTimeNS / timePerUpdateNS;
            timePlayedNS += elapsedTimeNS;

            while (deltaUpdate >= 1.0) {
                game.update(deltaTimeS);
                nextUpdateNS += timePerUpdateNS;
                deltaUpdate--;
                updates++;
            }

            if (deltaRender >= 1.0) {
                panel.repaint();
                nextFrameNS += timePerFrameNS;
                deltaRender--;
                frames++;
            }

            if (currentTimeNS - statsTimerNS >= NANOS_PER_SECOND) {
                currentFps = frames;
                currentUps = updates;

                frames = 0;
                updates = 0;

                statsTimerNS += NANOS_PER_SECOND;
            }

            long deadlineNS = Math.min(nextUpdateNS, nextFrameNS);
            waitUntil(deadlineNS);
        }
    }

    private void waitUntil(long deadlineNS) {
        long remainingNS = deadlineNS - System.nanoTime();

        while (remainingNS > SPIN_THRESHOLD_NS) {
            if (!keepRunning) {
                return;
            }
            LockSupport.parkNanos(remainingNS - SPIN_THRESHOLD_NS);
            remainingNS = deadlineNS - System.nanoTime();
        }

        while (deadlineNS - System.nanoTime() > 0) {
            if (!keepRunning) {
                return;
            }
            Thread.onSpinWait();
        }
    }
}
