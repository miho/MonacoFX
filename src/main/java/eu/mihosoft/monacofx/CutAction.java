package eu.mihosoft.monacofx;

public class CutAction extends AbstractEditorAction {

    public CutAction() {
        setLabel("Paste");
        setName("Paste");
        setActionId("editor.action.clipboardPasteAction");
        setContextMenuOrder("3");
        setContextMenuGroupId("9_cutcopypaste");
        setVisibleOnReadonly(false);
        setKeyBindings("monaco.KeyMod.ShiftCmd & monaco.KeyCode.Insert");
        setRunScript("let position = editor.getPosition();\n"
                + "console.log('paste ' + position);"
                + "let newPosition = clipboardBridge.paste(editor.getSelection(), position);\n"
                + "editor.setPosition(newPosition);\n"
                + "editor.focus();");
    }

    @Override
    public void action() {
        // empty
    }
}
