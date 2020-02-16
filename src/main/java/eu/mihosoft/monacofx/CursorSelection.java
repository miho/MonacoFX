package eu.mihosoft.monacofx;

public class CursorSelection {
    public final Selection primarySelection;
    public final Selection[] secondarySelections;

    public CursorSelection(Selection primarySelection, Selection[] secondarySelections) {
        this.primarySelection = primarySelection;
        this.secondarySelections = secondarySelections;
    }
}
