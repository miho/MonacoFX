/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
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
