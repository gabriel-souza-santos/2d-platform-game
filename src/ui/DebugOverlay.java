package ui;

import engine.Renderable;
import engine.Updatable;
import input.KeyType;
import input.KeyboardInput;
import main.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.sun.management.OperatingSystemMXBean;

public final class DebugOverlay implements Renderable, Updatable {
    private static final double STATS_UPDATE_INTERVAL_S = 0.8;
    private double timeSinceLastStatsUpdateS = 0.0;

    private volatile boolean isActive = false;

    private String cachedFPS = "FPS: 0";
    private String cachedUPS = "UPS: 0";
    private String cachedRAM = "RAM: 0MiB / 0MiB";
    private String cachedCPU = "CPU: 0.0%";
    private String cachedTimePlayed = "Time Played: 0s";

    private final OperatingSystemMXBean osBean;

    private final KeyboardInput keyboard;
    private final GameLoop runner;

    public DebugOverlay(GameLoop runner, KeyboardInput keyboard) {
        this.runner = runner;
        this.keyboard = keyboard;
        osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public void render(Graphics2D g2d) {
        renderStats(g2d);
    }

    @Override
    public void update(double deltaTimeS) {
        toggleDebugScreen();

        if (!isActive) {
            return;
        }

        timeSinceLastStatsUpdateS += deltaTimeS;
        if (timeSinceLastStatsUpdateS >= STATS_UPDATE_INTERVAL_S) {
            timeSinceLastStatsUpdateS = 0.0;
            updateMetrics();
        }
    }

    private void renderStats(Graphics2D g2d) {
        if (!isActive) {
            return;
        }

        g2d.setColor(Color.BLACK);
        g2d.drawString(cachedFPS, 10, 12);
        g2d.drawString(cachedUPS, 10, 24);
        g2d.drawString(cachedRAM, 10, 36);
        g2d.drawString(cachedCPU, 10, 48);
        g2d.drawString(cachedTimePlayed, 10, 60);
    }

    private void toggleDebugScreen() {
        if (keyboard.isPressed(KeyType.F3)) {
            isActive = !isActive;
        }
    }

    private void updateMetrics() {
        cachedFPS = "FPS: " + runner.getCurrentFPS();
        cachedUPS = "UPS: " + runner.getCurrentUPS();

        Runtime runtime = Runtime.getRuntime();
        long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long maxMB = runtime.maxMemory() / (1024 * 1024);
        cachedRAM = "RAM: " + usedMB + "MiB / " + maxMB + "MiB";

        double cpuLoad = osBean.getProcessCpuLoad() * 100.0;
        cachedCPU = "CPU: " + (cpuLoad < 0 ? "N/A" : String.format("%.1f%%", cpuLoad));

        cachedTimePlayed = "Time Played: " + formatTimePlayed(runner.getTimePlayedNS());
    }

    private String formatTimePlayed(long timePlayedNS) {
        long hours = TimeUnit.NANOSECONDS.toHours(timePlayedNS);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(timePlayedNS) % 60;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(timePlayedNS) % 60;

        if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        } else {
            return String.format("%02ds", seconds);
        }
    }
}
