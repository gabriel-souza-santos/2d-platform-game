package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

// FIXME: Ensure consistent keyboard reads on threaded environment
public class KeyboardInput implements KeyListener {
    private final Set<KeyType> heldKeys;
    private final Set<KeyType> pressedKeys;

    public KeyboardInput() {
        heldKeys = EnumSet.noneOf(KeyType.class);
        pressedKeys = EnumSet.noneOf(KeyType.class);
    }

    public boolean isHeld(KeyType key) {
        return heldKeys.contains(key);
    }

    public boolean isPressed(KeyType key) {
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
        Optional<KeyType> mapped = KeyType.fromCode(e.getKeyCode());
        if (mapped.isEmpty()) {
            return;
        }

        KeyType key = mapped.get();
        if (heldKeys.contains(key)) {
            return; // Not a new press
        }

        heldKeys.add(key);
        pressedKeys.add(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Optional<KeyType> key = KeyType.fromCode(e.getKeyCode());
        key.ifPresent(heldKeys::remove);
    }
}
