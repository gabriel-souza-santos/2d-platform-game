package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                // TODO
            }

            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                // TODO
            }

            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                // TODO
            }

            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                // TODO
            }

            default -> {
                // TODO
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO
    }
}
