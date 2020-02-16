package eu.mihosoft.monacofx;

public final class Rule {
    public final String token;
    public final String foreground;
    public final String background;
    public final String font;
    public final String fontStyle;
    public final String fontSize;

    public Rule(String token, String foreground, String background, String font, String fontStyle, String fontSize) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;
    }

    public Rule(String token, String foreground, String background) {
        this.token = token;
        this.foreground = foreground;
        this.background = background;
        this.font = null;
        this.fontStyle = null;
        this.fontSize = null;
    }

    public Rule(String token, String foreground) {
        this.token = token;
        this.foreground = foreground;
        this.background = null;
        this.font = null;
        this.fontStyle = null;
        this.fontSize = null;
    }
}