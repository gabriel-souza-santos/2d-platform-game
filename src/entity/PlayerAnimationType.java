package entity;

public enum PlayerAnimationType {
    IDLE("/images/player/idle.png", 6),
    WALK("/images/player/walk.png", 8),
    JUMP("/images/player/jump.png", 6),
    BLOCK("/images/player/block.png", 6),
    FIRST_ATTACK("/images/player/first-attack.png", 8),
    SECOND_ATTACK("/images/player/second-attack.png", 8),
    HURT("/images/player/hurt.png", 4),
    DEATH("/images/player/death.png", 10);

    private final String filePath;
    private final int frameCount;

    PlayerAnimationType(String filePath, int frameCount) {
        this.filePath = filePath;
        this.frameCount = frameCount;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFrameCount() {
        return frameCount;
    }
}
