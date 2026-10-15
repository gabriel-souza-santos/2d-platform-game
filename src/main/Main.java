package main;

public final class Main {
    // FIXME: Swing components (JFrame, JPanel) must be created and manipulated only on the EDT.
    //   Constructing Game here runs on the main thread, violating Swing's single-thread rule.
    //   This can cause deadlocks during component realization, rendering artifacts, or sporadic crashes.
    //   Wrap in SwingUtilities.invokeLater(() -> new Game()) to ensure EDT construction.
    public static void main(String[] args) {
        new Game();
    }
}
