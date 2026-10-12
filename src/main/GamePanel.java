package main;

import input.KeyboardInput;
import input.MouseInput;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GamePanel{
    private final JPanel panel;
    private final KeyboardInput keyboard;
    private final MouseInput mouse;

    public GamePanel() {
        keyboard = new KeyboardInput();
        mouse = new MouseInput();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                render(g);
            }
        };

        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        panel.addMouseWheelListener(mouse);
    }

    public void render(Graphics g) {
        g.fillRect(100, 100, 200, 200);
    }

    public void repaint() {
        panel.repaint();
    }

    public void requestFocus() {
        panel.requestFocusInWindow();
    }

    public JPanel asComponent() {
        return panel;
    }
}
