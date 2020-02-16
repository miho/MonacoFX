package eu.mihosoft.monacofx;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MonacoFX extends Region {

    private final WebView view;
    private final WebEngine engine;

    private final static String EDITOR_HTML_RESOURCE_LOCATION = "/eu/mihosoft/monacofx/monaco-editor-0.20.0/index.html";

    private final Editor editor;

    public MonacoFX() {

        view = new WebView();
        getChildren().add(view);
        engine = view.getEngine();
        String url = getClass().getResource(EDITOR_HTML_RESOURCE_LOCATION).toExternalForm();

        engine.load(url);

        editor = new Editor(engine);

        engine.getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.SUCCEEDED) {

                JSObject window = (JSObject) engine.executeScript("window");

                AtomicBoolean jsDone = new AtomicBoolean(false);
                AtomicInteger attempts = new AtomicInteger();

                Thread thread = new Thread(() -> {
                    while (!jsDone.get()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // check if JS execution is done.
                        Platform.runLater(() -> {
                            Object jsEditorObj = window.call("getEditorView");
                            if (jsEditorObj instanceof JSObject) {
                                editor.setEditor(window, (JSObject) jsEditorObj);
                                jsDone.set(true);
                            }
                        });

                        if(attempts.getAndIncrement()> 10) {
                            throw new RuntimeException(
                                "Cannot initialize editor (JS execution not complete). Max number of attempts reached."
                            );
                        }
                    }
                });
                thread.start();

            }
        });
    }

    @Override protected double computePrefWidth(double height) {
        return view.prefWidth(height);
    }

    @Override protected double computePrefHeight(double width) {
        return view.prefHeight(width);
    }

    @Override public void requestLayout() {
        super.requestLayout();
    }

    @Override protected void layoutChildren() {
        super.layoutChildren();

        layoutInArea(view,0,0,getWidth(), getHeight(),
                0, HPos.CENTER, VPos.CENTER
        );
    }

    Editor getEditor() {
        return editor;
    }

}
