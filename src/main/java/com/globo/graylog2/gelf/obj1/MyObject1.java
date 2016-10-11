package com.globo.graylog2.gelf.obj1;

/**
 * Created by lucas.castro on 10/7/16.
 */
public class MyObject1 {



    public void execute(Exception e) {
        String m = "";
        if (e != null ) {
            m = e.getMessage();
        }
        throw new RuntimeException("myobject throws exception: " + m, e);
    }

    public void run(Exception e) {
        execute(e);
    }

    public void process(Exception e) {
        run(e);
    }
}

