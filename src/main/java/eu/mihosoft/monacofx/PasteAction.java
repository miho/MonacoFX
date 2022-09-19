package eu.mihosoft.monacofx;

import netscape.javascript.JSObject;

public class PasteAction extends AbstractEditorAction {

    public PasteAction() {
        setLabel("Paste");
        setActionId("editor.action.clipboardPasteAction");
        setContextMenuOrder("3");
        setVisibleOnReadonly(false);
        setRunScript("let position = editor.getPosition();\n"
                + "let newPosition = clipboardBridge.paste(editor.getSelection(), position);\n"
                + "editor.setPosition(newPosition);\n"
                + "editor.focus();");
    }

    @Override
    public void action() {
        // empty
    }
}
