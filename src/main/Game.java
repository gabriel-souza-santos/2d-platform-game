package main;

import engine.Renderable;
import engine.Updatable;
import entity.Player;
import input.KeyboardInput;
import input.MouseInput;
import ui.DebugOverlay;

import java.awt.Graphics2D;

public class Game implements Renderable, Updatable {
    private Thread loopThread;
    private GameLoop runner;

    private GameWindow window;
    private GamePanel panel;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private Player player;

    private DebugOverlay debugScreen;

    // FIXME: Starting a thread inside the constructor could create a race condition with object initialization.
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

        debugScreen = new DebugOverlay(runner);
    }

    private void startGameLoop() {
        loopThread = new Thread(runner::run);
        loopThread.start();
        runner.start();
    }

    private void stopGameLoop() {
        // FIXME: Add thread cleanup and gameThread.join() on loop termination to avoid dangling background threads.
    }

    @Override
    public void update(double deltaTimeS) {
        player.update(deltaTimeS);
        keyboard.endFrame();
        mouse.endFrame();
    }

    @Override
    public void render(Graphics2D g2d) {
        player.render(g2d);
        debugScreen.render(g2d);
    }
}
