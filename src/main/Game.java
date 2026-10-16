package main;

import engine.GameLoop;
import engine.Renderable;
import engine.Updatable;
import entity.Player;
import input.KeyboardInput;
import input.MouseInput;
import ui.DebugOverlay;
import ui.GamePanel;
import ui.GameWindow;

import javax.swing.SwingUtilities;
import java.awt.Graphics2D;

public final class Game implements Renderable, Updatable {
    private final Thread loopThread;
    private final GameLoop runner;

    private final GameWindow window;
    private final GamePanel panel;

    private final KeyboardInput keyboard;
    private final MouseInput mouse;

    private final Player player;

    private final DebugOverlay debugScreen;

    private Game() {
        keyboard = new KeyboardInput();
        mouse = new MouseInput();

        player = new Player(keyboard, mouse);

        panel = new GamePanel(this::render, keyboard, mouse);
        window = new GameWindow(panel);

        runner = new GameLoop(this::update, panel::repaint);
        loopThread = new Thread(runner::run, "game-loop");

        debugScreen = new DebugOverlay(runner, keyboard);
    }

    static void launch() {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }

    @Override
    public void update(double deltaTimeS) {
        player.update(deltaTimeS);
        debugScreen.update(deltaTimeS);
        keyboard.endFrame();
        mouse.endFrame();
    }

    @Override
    public void render(Graphics2D g2d) {
        player.render(g2d);
        debugScreen.render(g2d);
    }

    private void start() {
        runner.start();
        loopThread.start();
    }

    private void stop() {
        // FIXME: No thread cleanup — the game-loop thread is never stopped or joined.
        //   The game-loop is a non-daemon thread, so it keeps the JVM alive even after the window closes
        //   (unless System.exit() is called by EXIT_ON_CLOSE). This prevents graceful shutdown, state
        //   saving, or resource flushing. Call runner.stop() to signal the loop, then loopThread.join()
        //   to wait for it to finish. Consider also adding a JFrame WindowListener or shutdown hook.
    }
}
