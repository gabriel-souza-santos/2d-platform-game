package entity;

import engine.Renderable;
import engine.Updatable;
import input.KeyType;
import input.KeyboardInput;
import input.MouseInput;
import main.GameLoop;
import world.Coordinate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

// FIXME: render() runs on the EDT (via paintComponent) while update() runs on the game-loop thread.
//   Both access shared mutable fields (position, currentAnimation, currentSpriteIndex, isMoving)
//   with no synchronization. This can cause visual tearing, inconsistent sprite/position combos,
//   or ArrayIndexOutOfBoundsException if currentSpriteIndex is read between increment and bounds reset.
//   Consider double-buffering game state, synchronizing on a lock, or copying a snapshot for rendering.
public final class Player implements Renderable, Updatable {
    private static final double SPRITE_SCALE = 3;

    private final KeyboardInput keyboard;
    private final MouseInput mouse;

    private final Coordinate position;
    private boolean isAttacking;
    private boolean isMoving;
    private boolean isFacingLeft;
    private long deltaX = 1;
    private long deltaY = 1;

    private final Map<PlayerAnimationType, Animation> animations;
    private PlayerAnimationType currentAnimation;
    private OrientationType currentOrientation;
    private int animationSpeed;

    public Player(KeyboardInput keyboard, MouseInput mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;

        position = Coordinate.from(Coordinate.SPAWN);

        isAttacking = false;
        isMoving = false;
        isFacingLeft = false;
        animationSpeed = (int) GameLoop.TARGET_FPS / 8;
        currentAnimation = PlayerAnimationType.IDLE;
        currentOrientation = OrientationType.NONE;

        animations = new EnumMap<>(PlayerAnimationType.class);
        loadAnimations();
    }

    @Override
    public void render(Graphics2D g2d) {
        renderAnimationFrame(g2d);
    }

    @Override
    public void update(double deltaTimeS) {
        updateOrientation();
        updatePosition();
        updateAttack();
        updateAnimation();
    }

    // FIXME: Player coordinates (world-space) are currently mixed with render coordinates (screen-space).
    // TODO: Decouple spatial logic by introducing a explicit mapping/conversion layer (e.g., worldToScreen).
    private void renderAnimationFrame(Graphics g2d) {
        BufferedImage frame = animations.get(currentAnimation).getCurrentFrame();

        int atX = (int) position.x;
        int atY = (int) position.y;

        int width = frame.getWidth() * (int) SPRITE_SCALE;
        int height = frame.getHeight() * (int) SPRITE_SCALE;

        if (isFacingLeft) {
            atX += width;
            width = -width;
        }

        g2d.drawImage(frame, atX, atY, width, height, null);
    }

    private void updateOrientation() {
        isMoving = true;

        // TODO: Add arrow keys
        if (keyboard.isHeld(KeyType.W)) {
            currentOrientation = OrientationType.UP;
        } else if (keyboard.isHeld(KeyType.S)) {
            currentOrientation = OrientationType.DOWN;
        } else if (keyboard.isHeld(KeyType.A)) {
            currentOrientation = OrientationType.LEFT;
            isFacingLeft = true;
        } else if (keyboard.isHeld(KeyType.D)) {
            currentOrientation = OrientationType.RIGHT;
            isFacingLeft = false;
        } else {
            currentOrientation = OrientationType.NONE;
            isMoving = false;
        }
    }

    private void updatePosition() {
        if (!isMoving) {
            return;
        }

        switch (currentOrientation) {
            case OrientationType.LEFT -> {
                position.x -= deltaX;
            }
            case OrientationType.RIGHT -> {
                position.x += deltaX;
            }
            case OrientationType.UP -> {
                position.y -= deltaY;
            }
            case OrientationType.DOWN -> {
                position.y += deltaY;
            }
        }
    }

    private void updateAttack() {
        Animation attackAnimation = animations.get(PlayerAnimationType.FIRST_ATTACK);

        // TODO: Switch to mouse click
        if (!isAttacking && keyboard.isPressed(KeyType.SPACE)) {
            isAttacking = true;
            attackAnimation.reset();
        }

        if (isAttacking && attackAnimation.isFinished()) {
            isAttacking = false;
        }
    }

    private void updateAnimation() {
        PlayerAnimationType nextAnimation;

        if (isMoving) {
            nextAnimation = PlayerAnimationType.WALK;
        } else if (isAttacking) {
            nextAnimation = PlayerAnimationType.FIRST_ATTACK;
        } else {
            nextAnimation = PlayerAnimationType.IDLE;
        }

        if (nextAnimation != currentAnimation) {
            currentAnimation = nextAnimation;
            animations.get(currentAnimation).reset();
        }

        animations.get(currentAnimation).update();
    }

    private void loadAnimations() {
        for (PlayerAnimationType type : PlayerAnimationType.values()) {
            String filePath = type.getFilePath();
            int frameCount = type.getFrameCount();

            Optional<Animation> animation = Animation.load(filePath, frameCount, animationSpeed);

            if (animation.isPresent()) {
                animations.put(type, animation.get());
            } else {
                throw new IllegalStateException("Couldn't load animation " + type + " from " + filePath);
            }
        }

        animations.get(PlayerAnimationType.IDLE).setLooping(true);
        animations.get(PlayerAnimationType.WALK).setLooping(true);
    }
}
