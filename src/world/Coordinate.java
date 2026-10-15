package world;

public final class Coordinate {
    public static final Coordinate SPAWN = new Coordinate(0, 0);
    public long x, y;

    public Coordinate(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate from(Coordinate other) {
        return new Coordinate(other.x, other.y);
    }

    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }
}
