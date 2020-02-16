package eu.mihosoft.monacofx;

import javafx.beans.property.*;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.util.function.Function;

public class Document {

    private WebEngine engine;
    private JSObject editor;
    private JSObject editorGlobal;
    private JSObject window;

    private final StringProperty textProperty = new SimpleStringProperty();
    private final StringProperty languageProperty = new SimpleStringProperty();
    private final IntegerProperty numberOfLinesProperty = new SimpleIntegerProperty();
    private boolean textChanging;

    void setEditor(WebEngine engine, JSObject window, JSObject editor) {
        this.engine = engine;
        this.editor = editor;
        this.window = window;

        // initial text
        editor.call("setValue", getText());

        // text changes -> js
        textProperty.addListener((ov) -> {
            if(!textChanging) {
                editor.call("setValue", getText());
            }
        });

        // text changes <- js
        window.setMember("contentChangeListener", new JFunction( args -> {

            textChanging = true;

            String text = (String) editor.call("getValue");

            if(text!=null) {
                setText(text);
                numberOfLinesProperty.setValue(text.split("\\R").length);
            }

            textChanging = false;

            return new Object();
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
