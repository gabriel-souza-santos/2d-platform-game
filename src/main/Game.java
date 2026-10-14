package main;

import engine.Renderable;
import engine.Updatable;
import entity.Player;
import input.KeyboardInput;
import input.MouseInput;

import java.awt.Graphics2D;

public class Game implements Renderable, Updatable {
    private Thread loopThread;
    private GameLoop gameLoop;

    private GameWindow window;
    private GamePanel panel;

    private KeyboardInput keyboard;
    private MouseInput mouse;

    private Player player;

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
        panel.requestFocus(); // Gets the input focus

        gameLoop = new GameLoop(this::update, panel);
    }

    private void startGameLoop() {
        loopThread = new Thread(gameLoop::run);
        loopThread.start();
        gameLoop.start();
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
        // TODO: Show FPS and UPS on the screen
        player.render(g2d);
    }
}
