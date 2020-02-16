package eu.mihosoft.monacofx;

import netscape.javascript.JSObject;

import java.util.function.Function;

public class JCallable implements Function<JSObject, Object> {

    private Function<Object[],Object> actualCallable;

    public JCallable( Function<Object[],Object> callable) {
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
