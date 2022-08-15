/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
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

    private boolean updatingText;

    private final StringProperty textProperty = new SimpleStringProperty();
    private final StringProperty languageProperty = new SimpleStringProperty();
    private final IntegerProperty numberOfLinesProperty = new SimpleIntegerProperty();

    private JFunction jsfListener;

    void setEditor(WebEngine engine, JSObject window, JSObject editor) {
        this.engine = engine;
        this.editor = editor;
        this.window = window;

        // initial text
        editor.call("setValue", getText());

        // text changes -> js
        textProperty.addListener((ov) -> {
            if(!updatingText) editor.call("setValue", getText());
        });

        // keep a global reference because it's garbage collected otherwise
        jsfListener = new JFunction( args -> {
            String text = (String) editor.call("getValue");
            if(text!=null) {
                try {
                    updatingText = true;
                    textProperty().set(text);
                }finally {
                    updatingText=false;
                }
                numberOfLinesProperty.setValue(text.split("\\R").length);
            }
            return null;
        });

        // text changes <- js
        window.setMember("contentChangeListener", jsfListener);

    }

    public StringProperty textProperty() {
        return textProperty;
    }

    public void setText(String text) {
        if(editor==null) {
            textProperty.set(text);
        } else {
            try {
                updatingText = true;
                textProperty().set(text);
            }finally {
                updatingText=false;
            }
            editor.call("setValue", text);
        }
    }

    public String getText() {
        return textProperty().get();
    }

    public ReadOnlyIntegerProperty numberOfLinesProperty() {
        return numberOfLinesProperty;
    }

    public StringProperty languageProperty() {
        return languageProperty;
    }

    public void setLanguage(String language) {
        languageProperty().set(language);
    }

    public String getLanguage() {
        return languageProperty().get();
    }

    /**
     * used to update the text in the editor without losing the document history
     * @param text the text in editor is replaced byt this text
     */
    public void updateText(String text) {
        window.call("updateText", text);
    }
}
