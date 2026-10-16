package ui;

import engine.Renderable;
import engine.Repaintable;
import input.KeyboardInput;
import input.MouseInput;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

public final class GamePanel implements Repaintable {
    private final JPanel panel;

    public GamePanel(Renderable game, KeyboardInput keyboard, MouseInput mouse) {
        panel = buildPanel(game);

        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        panel.addMouseWheelListener(mouse);

        panel.setFocusable(true);
        panel.requestFocusInWindow(); // Gets the input focus
    }

    @Override
    public void repaint() {
        panel.repaint();
    }

    public JPanel asComponent() {
        return panel;
    }

    private JPanel buildPanel(Renderable game) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create(); // Swing provides Graphics2D since Java 1.2
                // TODO: Add rendering hints

                try {
                    game.render(g2d);
                } finally {
                    g2d.dispose();
                }
            }
        };
    }
}
