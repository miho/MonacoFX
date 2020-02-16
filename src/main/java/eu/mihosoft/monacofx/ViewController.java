package eu.mihosoft.monacofx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import netscape.javascript.JSObject;

public final class ViewController {

    private final Editor editor;

    //private final ObjectProperty<Position> cursorPositionProperty = new SimpleObjectProperty<>();
    private final IntegerProperty scrollPositionProperty = new SimpleIntegerProperty();

    public ViewController(Editor editor) {
        this.editor = editor;
    }

    void setEditor(JSObject window, JSObject editor) {
         // initial scroll
        editor.call("setScrollPosition", getScrollPosition());
        // scroll changes -> js
        scrollPositionProperty().addListener((ov) -> {
            editor.call("setScrollPosition", getScrollPosition());
        });
         // scroll changes <- js
        window.setMember("scrollChangeListener", new JFunction( args -> {
            int pos = (int) editor.call("getScrollTop");
            setScrollPosition(pos);
            return null;
        }));
    }

    public void setScrollPosition(int posIdx) {
        //editor.setScrollPosition({scrollTop: 0});
        //editor.getEngine().executeScript("editorView.setScrollPosition({scrollTop: " + posIdx + "});");
        scrollPositionProperty().set(posIdx);
    }

    public int getScrollPosition() {
        // return editor.getScrollTop();
        return scrollPositionProperty().get();
    }

    public void scrollToLine(int line) {
        // editor.revealLine(line);
        editor.getJSEditor().call("revealLine", line);
    }

    public void scrollToLineCenter(int line) {
        // editor.revealLineInCenter(15);
        editor.getJSEditor().call("revealLineInCenter", line);
    }

    // ObjectProperty<Position> cursorPositionProperty() {
    //     return cursorPositionProperty;
    // }

    public IntegerProperty scrollPositionProperty() {
        return scrollPositionProperty;
    }
}
