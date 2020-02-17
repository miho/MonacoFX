/*
 * MIT License
 *
 * Copyright (c) 2020 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
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