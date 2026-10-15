package main;

import engine.Renderable;
import engine.Updatable;
import entity.Player;
import input.KeyboardInput;
import input.MouseInput;
import ui.DebugOverlay;

import java.awt.Graphics2D;

public final class Game implements Renderable, Updatable {
    private Thread loopThread;
    private GameLoop runner;

    private GameWindow window;
    private GamePanel panel;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private Player player;

    private DebugOverlay debugScreen;

    // FIXME: Starting a thread inside the constructor is a race condition with object initialization.
    //   The JMM only guarantees safe publication of 'final' fields after the constructor returns.
    //   Since player, keyboard, mouse, debugScreen, and panel are NOT final, the game-loop thread
    //   may see them as null or in a partially constructed state, causing NullPointerExceptions.
    //   Move thread startup to a separate start() method called after construction.
    public Game() {
        initComponents();
        startGameLoop();
    }

    private void initComponents() {
        keyboard = new KeyboardInput();
        mouse = new MouseInput();

        player = new Player(keyboard, mouse);

        panel = new GamePanel(this::render, keyboard, mouse);
        window = new GameWindow(panel);

        runner = new GameLoop(this::update, panel);

        debugScreen = new DebugOverlay(runner, keyboard);
    }

    private void startGameLoop() {
        // FIXME: Start order is inverted — runner.start() sets keepRunning = true, but it's called
        //   AFTER loopThread.start(). The game-loop thread may enter run(), see keepRunning == false,
        //   and exit the while-loop immediately before runner.start() executes.
        //   Call runner.start() before loopThread.start() to ensure the loop flag is set first.
        loopThread = new Thread(runner::run, "game-loop");
        loopThread.start();
        runner.start();
    }

    private void stopGameLoop() {
        // FIXME: No thread cleanup — the game-loop thread is never stopped or joined.
        //   The game-loop is a non-daemon thread, so it keeps the JVM alive even after the window closes
        //   (unless System.exit() is called by EXIT_ON_CLOSE). This prevents graceful shutdown, state
        //   saving, or resource flushing. Call runner.stop() to signal the loop, then loopThread.join()
        //   to wait for it to finish. Consider also adding a JFrame WindowListener or shutdown hook.
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
}
