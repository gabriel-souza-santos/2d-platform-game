package ui;

import engine.Renderable;
import engine.Updatable;
import input.KeyType;
import input.KeyboardInput;
import main.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;

public class DebugOverlay implements Renderable, Updatable {
    private volatile boolean isActive = false;

    private final KeyboardInput keyboard;
    private final GameLoop runner;

    public DebugOverlay(GameLoop runner, KeyboardInput keyboard) {
        this.runner = runner;
        this.keyboard = keyboard;
    }

    @Override
    public void render(Graphics2D g2d) {
        renderStats(g2d);
    }

    @Override
    public void update(double deltaTimeS) {
        toggleDebugScreen();
    }

    private void renderStats(Graphics2D g2d) {
        if (!isActive) {
            return;
        }

        String fps = Integer.toString(runner.getCurrentFPS());
        String ups = Integer.toString(runner.getCurrentUPS());

        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + fps, 10, 10);
        g2d.drawString("UPS: " + ups, 10, 20);
    }

    private void toggleDebugScreen() {
        if (keyboard.isPressed(KeyType.F3)) {
            isActive = !isActive;
        }
    }
}
