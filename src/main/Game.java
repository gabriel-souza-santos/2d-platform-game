package main;

public class Game implements Runnable {
    public static long TARGET_FPS = 120;
    public static long TARGET_UPS = 120;

    private static volatile boolean keepRunning;
    private static Thread gameThread;

    private static GameWindow window;
    private static GamePanel panel;

    // FIXME: Starting a thread inside the constructor creates a race condition with object initialization.
    // TODO: Refactor to use a static factory method (e.g., Game.launch()) or explicit game.start() call in Main.
    public Game() {
        initComponents();
        startGameLoop();
    }

    public void initComponents() {
        panel = new GamePanel();
        window = new GameWindow(panel);
        panel.requestFocus(); // Gets the input focus
    }

    public void startGameLoop() {
        keepRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGameLoop() {
        keepRunning = false;
    }

    public void update() {
        // TODO
    }

    public void render() {
        // TODO
        panel.repaint();
    }

    @Override
    public void run() {
        double timePerFrameNS = 1_000_000_000.0 / TARGET_FPS;
        double timePerUpdateNS = 1_000_000_000.0 / TARGET_UPS;

        double deltaRender = 0.0;
        double deltaUpdate = 0.0;

        long lastTimeNS = System.nanoTime();

        while (keepRunning) {
            long currentTimeNS = System.nanoTime();
            long elapsedTimeNS = currentTimeNS - lastTimeNS;

            lastTimeNS = currentTimeNS;
            deltaRender += elapsedTimeNS / timePerFrameNS;
            deltaUpdate += elapsedTimeNS / timePerUpdateNS;

            while (deltaUpdate >= 1) {
                update();
                deltaUpdate--;
            }

            if (deltaRender >= 1) {
                render();
                deltaRender--;
            }
        }
        // TODO: calculate remaining frame time and sleep to reduce CPU usage without dropping target FPS/UPS.
        // FIXME: Add thread cleanup and gameThread.join() on loop termination to avoid dangling background threads.
    }
}
