package main;

import javax.swing.JFrame;
import java.awt.Dimension;

public class GameWindow {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    private Dimension windowSize;
    private JFrame frame;

    public GameWindow(GamePanel panel) {
        windowSize = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame = new JFrame();

        frame.add(panel.asComponent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowSize);

        frame.setLocationRelativeTo(null); // Centers the window, must be call after setting size
        frame.setVisible(true); // Must be the last operation
    }

    public JFrame asComponent() {
        return frame;
    }
}
