package input;

import java.awt.event.KeyEvent;
import java.util.Optional;

public enum KeyType {
    W(KeyEvent.VK_W),
    A(KeyEvent.VK_A),
    S(KeyEvent.VK_S),
    D(KeyEvent.VK_D),

    UP(KeyEvent.VK_UP),
    LEFT(KeyEvent.VK_LEFT),
    DOWN(KeyEvent.VK_DOWN),
    RIGHT(KeyEvent.VK_RIGHT),

    SPACE(KeyEvent.VK_SPACE),
    ENTER(KeyEvent.VK_ENTER);

    private final int keyCode;

    KeyType(int keyCode) {
        this.keyCode = keyCode;
    }

    public static Optional<KeyType> fromCode(int keyCode) {
        for (KeyType key : KeyType.values()) {
            if (key.getCode() == keyCode) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }

    public int getCode() {
        return keyCode;
    }
}
