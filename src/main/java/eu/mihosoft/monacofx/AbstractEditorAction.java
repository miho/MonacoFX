package eu.mihosoft.monacofx;

/**
 * This is a Call Back class where the action method can be implemented for a custom usage.
 * Using @see {@link eu.mihosoft.monacofx.MonacoFX#addContextMenuAction} the implementation of this class can be added to
 * the context menu actions of the editor.
 */
public abstract class AbstractEditorAction {

    private String actionId;
    /**
     * The label of the action shown in context menu has to be unique.
     */
    private String label;
    private String contextMenuOrder;

    private String runScript;
    /**
     * if this flag is set to true the action is visible in context menu.
     */
    private boolean visibleOnReadonly;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContextMenuOrder() {
        return contextMenuOrder;
    }

    public void setContextMenuOrder(String contextMenuOrder) {
        this.contextMenuOrder = contextMenuOrder;
    }

    public boolean isVisibleOnReadonly() {
        return visibleOnReadonly;
    }

    public void setVisibleOnReadonly(boolean visibleOnReadonly) {
        this.visibleOnReadonly = visibleOnReadonly;
    }

    /**
     * This method is called whenever the action in javascript is activated.
     *
     */
    abstract public void action();

    public String getRunScript() {
        return runScript;
    }

    public void setRunScript(String runScript) {
        this.runScript = runScript;
    }

}
