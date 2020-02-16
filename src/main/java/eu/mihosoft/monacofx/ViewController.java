package eu.mihosoft.monacofx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public final class ViewController {

    private final Editor editor;

    private final ObjectProperty<Position> cursorPositionProperty = new SimpleObjectProperty<>();

    public ViewController(Editor editor) {
        this.editor = editor;
    }

    public void setScrollPosition(int posInx) {
        //editor.setScrollPosition({scrollTop: 0});
    }

    public int getScrollPosition() {
        // return editor.getScrollPosition();

        return 0;
    }

    public void scrollToLine(int line) {
        // editor.revealLine(line);
    }

    public void scrollToLineCenter(int line) {
        // editor.revealLineInCenter(15);
    }

    ObjectProperty<Position> cursorPositionProperty() {
        return cursorPositionProperty;
    }
}
