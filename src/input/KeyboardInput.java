package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class KeyboardInput implements KeyListener {
    private final Set<KeyAction> heldKeys;

    public KeyboardInput() {
        heldKeys = EnumSet.noneOf(KeyAction.class);
    }

    public boolean isHeld(KeyAction key) {
        return heldKeys.contains(key);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Optional<KeyAction> key = KeyAction.fromKeyCode(e.getKeyCode());
        key.ifPresent(heldKeys::add);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Optional<KeyAction> key = KeyAction.fromKeyCode(e.getKeyCode());
        key.ifPresent(heldKeys::remove);
    }
}
