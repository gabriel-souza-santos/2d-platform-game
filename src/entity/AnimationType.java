package entity;

public enum AnimationType {
    IDLE(0),
    MOVE(1),
    JUMP(2),
    DEFENSE(3),
    ATTACK(4),
    COMBO(5),
    DAMAGE(6),
    DEFEAT(7);

    /** Represents its row on the spritesheet */
    private final int index;
    private static final int MAX_SPRITE_AMOUNT = 10;

    AnimationType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static int getCount() {
        return values().length;
    }

    public static int getSpriteAmount(AnimationType type) {
        return switch (type) {
            case DAMAGE -> 4;
            case IDLE, JUMP, DEFENSE -> 6;
            case MOVE, ATTACK, COMBO -> 8;
            case DEFEAT -> 10;
            default -> 0;
        };
    }

    public static int getMaxSpriteAmount() {
        return MAX_SPRITE_AMOUNT;
    }
}
