package eu.mihosoft.monacofx;

import netscape.javascript.JSObject;

public class PasteAction extends AbstractEditorAction {

    public PasteAction() {
        setLabel("Paste");
        setName("Paste");
        setActionId("editor.action.clipboardPasteAction");
        setContextMenuOrder("3");
        setContextMenuGroupId("9_cutcopypaste");
        setVisibleOnReadonly(false);
        setKeyBindings("monaco.KeyMod.ShiftCmd & monaco.KeyCode.Insert");
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
