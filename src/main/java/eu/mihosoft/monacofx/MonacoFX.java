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

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MonacoFX extends Region {

    private final WebView view;
    private final WebEngine engine;

    private final static String EDITOR_HTML_RESOURCE_LOCATION = "/eu/mihosoft/monacofx/monaco-editor-0.20.0/index.html";

    private final Editor editor;
    private final SystemClipboardWrapper systemClipboardWrapper;
    private boolean readOnly;

    public MonacoFX() {
        view = new WebView();
        getChildren().add(view);
        engine = view.getEngine();
        String url = getClass().getResource(EDITOR_HTML_RESOURCE_LOCATION).toExternalForm();

        engine.load(url);

        editor = new Editor(engine);

        systemClipboardWrapper = new SystemClipboardWrapper();
        ClipboardBridge clipboardBridge = new ClipboardBridge(getEditor().getDocument(), systemClipboardWrapper);
        engine.getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.SUCCEEDED) {

                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("clipboardBridge", clipboardBridge);

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

        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            Object obj = engine.executeScript("editorView.getModel().getValueInRange(editorView.getSelection())");
            systemClipboardWrapper.handleCopyCutKeyEvent(event, obj);
        });

    }

    protected void postConstruct() {
        addPasteAction();
    }

    private void addPasteAction() {
        final PasteAction pasteAction = new PasteAction();
        addContextMenuAction(pasteAction);
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

    public Editor getEditor() {
        return editor;
    }

    @Deprecated
    public WebEngine getWebEngine() {
        return engine;
    }

    /**
     * The call back implementation which is added for a custom action will be included in context menu of the editor.
     *
     * @param action {@link eu.mihosoft.monacofx.AbstractEditorAction} call back object as abstract action.
     */
    public void addContextMenuAction(AbstractEditorAction action) {
        getWebEngine().getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.SUCCEEDED) {
                String precondition = "null";
                if(!action.isVisibleOnReadonly() && readOnly) {
                    precondition = "\"false\"";
                }
                JSObject window = (JSObject) getWebEngine().executeScript("window");
                String actionName = action.getName();
                window.setMember(actionName, action);
                String contextMenuOrder = "";
                if (action.getContextMenuOrder() != null && !action.getContextMenuOrder().isEmpty()) {
                    contextMenuOrder = "contextMenuOrder: " + action.getContextMenuOrder() + ",\n";
                }
                getWebEngine().executeScript(
                    "editorView.addAction({\n" +
                        "id: \"" + action.getActionId() + actionName + "\",\n" +
                        "label: \"" + action.getLabel() + "\",\n" +
                        "contextMenuGroupId: \""+ action.getContextMenuGroupId() +"\",\n" +
                        "precondition: " + precondition + ",\n" +
                        contextMenuOrder +
                        "run: (editor) => {" +
                            actionName + ".action();\n" +
                            action.getRunScript() +
                        "}\n" +
                    "});"
                );
            }
        });
    }


    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadonly(boolean readOnly) {
        this.readOnly = readOnly;
        setOption("readOnly", readOnly);
    }

    public void setOption(String optionName, Object value) {
        String script = String.format("editorView.updateOptions({ " + optionName + ": %s })", value);
        ReadOnlyObjectProperty<Worker.State> stateProperty = getWebEngine().getLoadWorker().stateProperty();
        if (Worker.State.SUCCEEDED == stateProperty.getValue()) {
            getWebEngine().executeScript(script);
        } else {
            getWebEngine().getLoadWorker().stateProperty().addListener((o, old, state) -> {
                if (Worker.State.SUCCEEDED == state) {
                    getWebEngine().executeScript(script);
                }
            });
        }
    }
}
