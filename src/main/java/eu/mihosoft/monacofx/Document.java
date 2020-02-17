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

import javafx.beans.property.*;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class Document {

    private WebEngine engine;
    private JSObject editor;
    private JSObject editorGlobal;
    private JSObject window;

    private final StringProperty textProperty = new SimpleStringProperty();
    private final StringProperty languageProperty = new SimpleStringProperty();
    private final IntegerProperty numberOfLinesProperty = new SimpleIntegerProperty();

    void setEditor(WebEngine engine, JSObject window, JSObject editor) {
        this.engine = engine;
        this.editor = editor;
        this.window = window;

        // initial text
        editor.call("setValue", getText());

        // text changes -> js
        textProperty.addListener((ov) -> {
            editor.call("setValue", getText());
        });

        // text changes <- js
        window.setMember("contentChangeListener", new JFunction( args -> {

            String text = (String) editor.call("getValue");
            if(text!=null) {
                setText(text);
                numberOfLinesProperty.setValue(text.split("\\R").length);
            }
            return null;
        }));

    }

    public StringProperty textProperty() {
        return textProperty;
    }

    public void setText(String text) {
        textProperty().set(text);
    }

    public String getText() {
        return textProperty().get();
    }

    ReadOnlyIntegerProperty numberOfLinesProperty() {
        return numberOfLinesProperty;
    }

    StringProperty languageProperty() {
        return languageProperty;
    }

    public void setLanguage(String language) {
        languageProperty().set(language);
    }

    public String getLanguage() {
        return languageProperty().get();
    }
}
