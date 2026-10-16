package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public final class Animation {
    private final BufferedImage[] frames;
    private final int ticksPerFrame;

    private boolean isLooping = false;
    private boolean isFinished = false;

    private int currentFrame;
    private int currentTick;

    private Animation(BufferedImage[] frames, int ticksPerFrame) {
        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
    }

    public static Optional<Animation> load(String filePath, int frameCount, int ticksPerFrame) {
        try (java.io.InputStream stream = Animation.class.getResourceAsStream(filePath)) {
            if (stream == null) {
                System.err.println("Error: Spritesheet not found at " + filePath);
                return Optional.empty();
            }

            BufferedImage sheet = ImageIO.read(stream);
            if (sheet == null) {
                System.err.println("Error: File at " + filePath + " could not be decoded as an image.");
                return Optional.empty();
            }

            int frameWidth = sheet.getWidth() / frameCount;
            int frameHeight = sheet.getHeight();

            BufferedImage[] frames = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = sheet.getSubimage(frameWidth * i, 0, frameWidth, frameHeight);
            }

            return Optional.of(new Animation(frames, ticksPerFrame));

        } catch (IOException e) {
            System.err.println("Error reading spritesheet at " + filePath + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public void update() {
        if (isFinished) {
            return;
        }

        currentTick++;
        if (currentTick < ticksPerFrame) {
            return;
        }

        currentTick = 0;
        if (currentFrame + 1 < frames.length) {
            currentFrame++;
        } else if (isLooping) {
            currentFrame = 0;
        } else {
            isFinished = true;
        }
    }

    public void reset() {
        currentFrame = 0;
        currentTick = 0;
        isFinished = false;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }
}
