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

        System.out.println("SCRiPT:" + registerScript);

        engine.executeScript(registerScript);
    }

    private void registerThemeJS(EditorTheme t) {
        System.out.println("registering theme");
//        window.setMember(("theme_" + t.name), t);
//
//        String registerScript = "require(['vs/editor/editor.main'], function() {\n"+
//        "        monaco.editor.defineTheme('"+t.name+"', theme_" + t.name+")\n"+
//        "})";
//
//        engine.executeScript(registerScript);

        window.call("monaco.editor.defineTheme",t.name, t);
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
