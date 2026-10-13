package main;

import engine.Renderable;
import input.KeyboardInput;
import input.MouseInput;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GamePanel{
    private final JPanel panel;

    public GamePanel(Renderable game, KeyboardInput keyboard, MouseInput mouse) {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                game.render(g);
            }
        };

        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        panel.addMouseWheelListener(mouse);
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
