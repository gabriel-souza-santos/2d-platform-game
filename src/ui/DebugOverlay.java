package ui;

import engine.Renderable;
import main.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;

public class DebugOverlay implements Renderable {
    private final GameLoop runner;

    public DebugOverlay(GameLoop runner) {
        this.runner = runner;
    }

    @Override
    public void render(Graphics2D g2d) {
        renderStats(g2d);
    }

    private void renderStats(Graphics2D g2d) {
        String fps = Integer.toString(runner.getCurrentFPS());
        String ups = Integer.toString(runner.getCurrentUPS());

        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + fps, 10, 10);
        g2d.drawString("UPS: " + ups, 10, 20);
    }
}
