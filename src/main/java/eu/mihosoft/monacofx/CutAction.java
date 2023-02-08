package eu.mihosoft.monacofx;

public class CutAction extends AbstractEditorAction {

    public CutAction() {
        setLabel("Cut");
        setName("Cut");
        setActionId("editor.action.clipboardCutAction");
        setContextMenuOrder("3");
        setContextMenuGroupId("9_cutcopypaste");
        setVisibleOnReadonly(false);
        setKeyBindings("monaco.KeyMod.Shift | monaco.KeyCode.Delete",
                "monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyX");
        setRunScript("clipboardBridge.copy(editorView.getSelection());\n"
                + "document.execCommand('cut');\n"
        );
    }

    @Override
    public void action() {
        // empty
    }
}
