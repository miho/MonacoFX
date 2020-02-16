package eu.mihosoft.monacofx;

public final class Selection {
    public final Position start;
    public final Position stop;

    public Selection(Position start, Position stop) {
        this.start = start;
        this.stop = stop;
    }
}
