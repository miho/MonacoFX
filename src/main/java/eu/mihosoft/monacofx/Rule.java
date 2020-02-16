package eu.mihosoft.monacofx;

public final class Rule {
    public final String token;
    public final String foreground;
    public final String background;
    public final String font;
    public final String fontStyle;
    public final String fontSize;

    public Rule(String token, String foreground, String background, String font, String fontSize, String fontStyle) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
    }

    public Rule(String token, String foreground, String background, String font, String fontSize) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.fontSize = fontSize;
        this.fontStyle = null;
    }

    public Rule(String token, String foreground, String background, String font) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.fontSize = null;
        this.fontStyle = null;
    }

    public Rule(String token, String foreground, String background) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = null;
        this.fontSize = null;
        this.fontStyle = null;
    }

    public Rule(String token, String foreground) {
        this.token = token;
        this.foreground = foreground;
        this.background = null;
        this.font = null;
        this.fontSize = null;
        this.fontStyle = null;
    }

    public String toJS() {
        String result = "{";

        result+="token: '"+token+"',\n"
            +(foreground==null?"":("foreground: '"+foreground+"',\n"))
            +(background==null?"":("background: '"+background+"',\n"))
            +(font==null?"":("font: '"+font+"',\n"))
            +(fontStyle==null?"":("fontStyle: '"+fontStyle+"',\n"))
            +(fontSize==null?"":("fontSize: '"+fontSize+"',\n"))
            +"}";

        return result;
    }
}