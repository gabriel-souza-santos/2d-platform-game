package main;

public class Game {
    private static GameWindow window;
    private static GamePanel panel;

    public Game() {
        panel = new GamePanel();
        window = new GameWindow(panel);
    }
}
