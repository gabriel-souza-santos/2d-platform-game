package main;

import engine.Renderable;
import input.KeyboardInput;
import input.MouseInput;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel {
    private final JPanel panel;

    public GamePanel(Renderable game, KeyboardInput keyboard, MouseInput mouse) {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Safety: Since Java 1.2, Swing passes a Graphics2D to paintComponent
                Graphics2D g2d = (Graphics2D) g.create();
                // TODO: Add rendering hints

                try {
                    game.render(g2d);
                } finally {
                    g2d.dispose();
                }
            }
        };

        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        panel.addMouseWheelListener(mouse);
        panel.setFocusable(true);
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
