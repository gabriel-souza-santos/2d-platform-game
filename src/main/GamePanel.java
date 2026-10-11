package main;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GamePanel{
    private final JPanel panel;

    public GamePanel() {
        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                render(g);
            }
        };
    }

    public void render(Graphics g) {
        g.fillRect(100, 100, 200, 200);
    }

    public void repaint() {
        panel.repaint();
    }

    public JPanel asComponent() {
        return panel;
    }
}
