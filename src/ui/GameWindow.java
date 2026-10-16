package ui;

import javax.swing.JFrame;

public final class GameWindow {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    private final GamePanel panel;
    private final JFrame frame;

    public GameWindow(GamePanel panel) {
        this.panel = panel;
        frame = new JFrame();

        frame.add(panel.asComponent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        frame.setLocationRelativeTo(null); // Centers the window, must be call after setting size
        frame.setVisible(true); // Must be the last operation
    }

    public GamePanel getPanel() {
        return panel;
    }

    public JFrame asComponent() {
        return frame;
    }
}
