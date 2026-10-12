package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class KeyboardInput implements KeyListener {
    private final Set<KeyAction> heldKeys;
    private final Set<KeyAction> pressedKeys;

    public KeyboardInput() {
        heldKeys = EnumSet.noneOf(KeyAction.class);
        pressedKeys = EnumSet.noneOf(KeyAction.class);
    }

    public boolean isHeld(KeyAction key) {
        return heldKeys.contains(key);
    }

    public boolean isPressed(KeyAction key) {
        return pressedKeys.contains(key);
    }

    /** Must be called once at the end of every tick */
    public void endFrame() {
        pressedKeys.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Optional<KeyAction> mapped = KeyAction.fromKeyCode(e.getKeyCode());
        if (mapped.isEmpty()) {
            return;
        }

        KeyAction key = mapped.get();
        if (heldKeys.contains(key)) {
            return; // Not a new press
        }

        heldKeys.add(key);
        pressedKeys.add(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Optional<KeyAction> key = KeyAction.fromKeyCode(e.getKeyCode());
        key.ifPresent(heldKeys::remove);
    }
}
