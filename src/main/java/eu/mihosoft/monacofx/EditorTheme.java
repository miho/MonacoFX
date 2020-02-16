package eu.mihosoft.monacofx;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public String toJS() {
        String result = "{\n"
               +"base: '"+base+"',\n"
               +"inherit: "+inherit+",\n"
               +"rules: [\n"
               + String.join(",", Arrays.asList(rules).
                 stream().map(r->r.toJS()).collect(Collectors.toList()))
               +"]\n"
               +"}";
        return result;
    }


}
