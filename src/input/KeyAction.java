package input;

import java.awt.event.KeyEvent;
import java.util.Optional;

public enum KeyAction {
    UP(KeyEvent.VK_W),
    LEFT(KeyEvent.VK_A),
    DOWN(KeyEvent.VK_S),
    RIGHT(KeyEvent.VK_D),

    ARROW_UP(KeyEvent.VK_UP),
    ARROW_LEFT(KeyEvent.VK_LEFT),
    ARROW_DOWN(KeyEvent.VK_DOWN),
    ARROW_RIGHT(KeyEvent.VK_RIGHT),

    SPACE(KeyEvent.VK_SPACE),
    ENTER(KeyEvent.VK_ENTER);

    private final int keyCode;

    KeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public static Optional<KeyAction> fromKeyCode(int keyCode) {
        for (KeyAction action : KeyAction.values()) {
            if (action.getKeyCode() == keyCode) {
                return Optional.of(action);
            }
        }
        return Optional.empty();
    }

    public int getKeyCode() {
        return keyCode;
    }
}
