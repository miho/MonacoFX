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

import netscape.javascript.JSObject;

import java.util.function.Function;

public class JFunction implements Function<JSObject, Object> {

    private Function<Object[],Object> actualCallable;

    public JFunction(Function<Object[],Object> callable) {
        this.actualCallable = callable;
    }

    @Override
    public Object apply(JSObject args) {

        Integer numArgs = 0;
        boolean isArray = false;

        if(args!=null) {
            try {
                numArgs = (Integer)args.getMember("length");
                isArray = true;
            } catch(NullPointerException ex) {
                // length not available
            }
        }

        if(isArray) {
            Object[] array = new Object[numArgs];

            for (int i = 0; i < numArgs; i++) {
                Object obj = args.getSlot(i);
                array[i] = obj;
            }

            return this.actualCallable.apply(array);
        } else {
            return this.actualCallable.apply(new Object[]{args});
        }

    }
}
