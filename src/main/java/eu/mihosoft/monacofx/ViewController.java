/*
 * MIT License
 *
 * Copyright (c) 2020 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
