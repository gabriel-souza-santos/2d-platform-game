package entity;

import engine.Renderable;
import engine.Updatable;
import input.KeyAction;
import input.KeyboardInput;
import input.MouseInput;
import world.Coordinate;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Player implements Renderable, Updatable {
    private static final String SPRITESHEET_PATH = "/images/player/soldier-sword-shield.png";

    private final KeyboardInput keyboard;
    private final MouseInput mouse;

    private final long deltaX = 1;
    private final long deltaY = 1;
    private final Coordinate position;

    private boolean isMoving;
    private int currentSpriteIndex;
    private AnimationType currentAnimation;
    private OrientationType currentOrientation;
    private final BufferedImage[] sprites;

    public Player(KeyboardInput keyboard, MouseInput mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;

        position = Coordinate.from(Coordinate.SPAWN);

        isMoving = false;
        currentAnimation = AnimationType.IDLE;
        currentOrientation = OrientationType.NONE;
        currentSpriteIndex = 0;

        sprites = loadSpritesFrom(SPRITESHEET_PATH).orElseGet(() -> {
            System.err.println("Error: Couldn't correctly load sprites of file " + SPRITESHEET_PATH);
            System.exit(1);
            return null; // This line is required for compilation but will never execute
        });
    }

    @Override
    public void render(Graphics2D g2d) {
        renderSprite(g2d);
    }

    @Override
    public void update(double deltaTimeS) {
        updateOrientation();
        updatePosition();
        updateAnimation();
        updateSprite();
    }

    // FIXME: Mixed coordinates systems
    // TODO: Separate and map player and render coordinates
    private void renderSprite(Graphics g2d) {
        BufferedImage sprite = getSprite(currentAnimation, currentSpriteIndex);
        g2d.drawImage(sprite, (int)position.x, (int)position.y, null);
    }

    private void updateOrientation() {
        isMoving = true;

        if (keyboard.isHeld(KeyAction.UP)) {
            currentOrientation = OrientationType.UP;
        } else if (keyboard.isHeld(KeyAction.DOWN)) {
            currentOrientation = OrientationType.DOWN;
        } else if (keyboard.isHeld(KeyAction.LEFT)) {
            currentOrientation = OrientationType.LEFT;
        } else if (keyboard.isHeld(KeyAction.RIGHT)) {
            currentOrientation = OrientationType.RIGHT;
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

    private void updateAnimation() {
        if (isMoving) {
            currentAnimation = AnimationType.MOVE;
        } else {
            currentAnimation = AnimationType.IDLE;
        }
    }

    private void updateSprite() {
        currentSpriteIndex++;
        if (currentSpriteIndex >= AnimationType.getSpriteAmount(currentAnimation)) {
            currentSpriteIndex = 0;
        }
    }

    private Optional<BufferedImage[]> loadSpritesFrom(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("Error: Spritesheet not found at " + path);
                return Optional.empty();
            }

            BufferedImage spriteSheet = ImageIO.read(stream);
            if (spriteSheet == null) {
                System.err.println("Error: File at " + path + " could not be decoded as an image.");
                return Optional.empty();
            }

            int rows = AnimationType.getCount();
            int columns = AnimationType.getMaxSpriteAmount();

            BufferedImage[] sprites = new BufferedImage[rows * columns];

            int spriteWidth = spriteSheet.getWidth() / columns;
            int spriteHeight = spriteSheet.getHeight() / rows;

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    int atIndex = (row * columns) + column;
                    int atX = column * spriteWidth;
                    int atY = row * spriteHeight;

                    sprites[atIndex] = spriteSheet.getSubimage(atX, atY, spriteWidth, spriteHeight);
                }
            }

            return Optional.of(sprites);

        } catch (IOException e) {
            System.err.println("Error reading spritesheet at " + path + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    /** Warning: this method do not perform bound checking */
    private BufferedImage getSprite(AnimationType type, int frame) {
        int index = (type.getIndex() * AnimationType.getMaxSpriteAmount()) + frame;
        return sprites[index];
    }
}
