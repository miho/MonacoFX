package eu.mihosoft.monacofx;

public final class EditorTheme {
    public final String name;
    public final String base;
    public final boolean inherit;
    public final Rule[] rules;

    public EditorTheme(String name, String base, boolean inherit, Rule... rules) {
        this.name = name;
        this.base = base;
        this.inherit = inherit;
        this.rules = rules;
    }


}
