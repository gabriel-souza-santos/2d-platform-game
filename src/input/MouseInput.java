package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

// FIXME: Any mutable state added here will be accessed concurrently by two threads without synchronization:
//   - The EDT (Event Dispatch Thread) writes via mouse event callbacks.
//   - The game-loop thread reads via public query methods and clears via endFrame().
//   Use synchronized blocks, volatile fields, or snapshot-swapping to ensure thread-safe reads/writes.
public final class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {
    public void endFrame() {
        // TODO
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // TODO
    }
}
