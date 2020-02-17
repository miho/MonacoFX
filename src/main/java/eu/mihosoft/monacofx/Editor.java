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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public final class Editor {

    private final WebEngine engine;
    private JSObject window;
    private JSObject editor;
    private final ViewController viewController;
    private final ObjectProperty<Document> documentProperty = new SimpleObjectProperty<>();
    private final ObservableList<LanguageSupport> languages = FXCollections.observableArrayList();
    private final ObservableList<EditorTheme> themes = FXCollections.observableArrayList();

    private final StringProperty currentThemeProperty = new SimpleStringProperty();
    private final StringProperty currentLanguageProperty = new SimpleStringProperty();

    Editor(WebEngine engine) {
        this.engine = engine;
        this.viewController = new ViewController(this);
        Document document = new Document();
        setDocument(document);
    }

    JSObject getJSEditor() {
        return editor;
    }

    JSObject getJSWindow() {
        return window;
    }

    WebEngine getEngine() {
        return engine;
    }

    private void registerLanguageJS(LanguageSupport l) {

        String registerScript = "require(['vs/editor/editor.main'], function() {\n";

        String registerLang = "monaco.languages.register({ id: '"+ l.getName() + "' })\n";

        registerScript+=registerLang;

        if(l.getMonarchSyntaxHighlighter()!=null) {
            String registerMonarch = "monaco.languages.setMonarchTokensProvider(\"" + l.getName() + "\", {\n"
                    + l.getMonarchSyntaxHighlighter().getRules()
                    + "})\n";
            registerScript+=registerMonarch;
        }

        if(l.getFoldingProvider()!=null) {
            window.setMember(("foldingProvider_" + l.getName()),
                    new JFunction((args) -> l.getFoldingProvider().computeFoldings(this))
            );


            String registerFoldingProvider = "monaco.languages.registerFoldingRangeProvider('mylang', {\n"
                    + "         provideFoldingRanges: function(model, context, token) {\n"
                    + "     return foldingProvider_" + l.getName() + ".apply([model,context,token]);\n"
                    + "}\n"
                    + "});\n";

            registerScript+=registerFoldingProvider;
        }

        registerScript+="\n})";

        engine.executeScript(registerScript);
    }

    private void registerThemeJS(EditorTheme t) {
        String script = "monaco.editor.defineTheme('"+t.name+"', " + t.toJS()+")";
        engine.executeScript(script);
    }

    void setEditor(JSObject window, JSObject editor) {
        this.editor = editor;
        this.window = window;

        // register custom languages
        languages.forEach(this::registerLanguageJS);
        languages.addListener((ListChangeListener<LanguageSupport>) c -> {
            while(c.next()) {
                c.getAddedSubList().stream().forEach(this::registerLanguageJS);
            }
        });

        // register custom themes
        themes.forEach(this::registerThemeJS);
        themes.addListener((ListChangeListener<EditorTheme>) c -> {
            while(c.next()) {
                c.getAddedSubList().stream().forEach(this::registerThemeJS);
            }
        });

        // initial theme
        if(getCurrentTheme()!=null) {
            engine.executeScript("monaco.editor.setTheme('"+getCurrentTheme()+"')");
        }

        // theme changes -> js
        currentThemeProperty().addListener((ov) -> {
            engine.executeScript("monaco.editor.setTheme('"+getCurrentTheme()+"')");
        });

        // initial lang
        if(getCurrentLanguage()!=null) {
            engine.executeScript("monaco.editor.setModelLanguage(editorView.getModel(),'"+getCurrentLanguage()+"')");
        }

        // lang changes -> js
        currentLanguageProperty().addListener((ov) -> {
            engine.executeScript("monaco.editor.setModelLanguage(editorView.getModel(),'"+getCurrentLanguage()+"')");
        });

        getDocument().setEditor(engine, window, editor);

        getViewController().setEditor(window, editor);
    }

    public StringProperty currentThemeProperty() {
        return this.currentThemeProperty;
    }

    public void setCurrentTheme(String theme) {
        currentThemeProperty().set(theme);
    }

    public String getCurrentTheme() {
        return currentThemeProperty().get();
    }

    public StringProperty currentLanguageProperty() {
        return this.currentLanguageProperty;
    }

    public void setCurrentLanguage(String theme) {
        currentLanguageProperty().set(theme);
    }

    public String getCurrentLanguage() {
        return currentLanguageProperty().get();
    }

    public ObjectProperty<Document> documentProperty() {
        return documentProperty;
    }

    public void setDocument(Document document) {
        documentProperty().set(document);
    }

    public Document getDocument() {
        return documentProperty().get();
    }

    public ViewController getViewController() {
        return viewController;
    }

//    List<LanguageSupport> getLanguages() {
//        return languages;
//    }

    public void registerLanguage(LanguageSupport language) {
        this.languages.add(language);
    }

    public void registerTheme(EditorTheme theme) {
        this.themes.add(theme);
    }


}
