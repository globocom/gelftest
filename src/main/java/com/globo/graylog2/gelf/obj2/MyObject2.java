package com.globo.graylog2.gelf.obj2;

import com.globo.graylog2.gelf.obj1.MyObject1;

/**
 * Created by lucas.castro on 10/7/16.
 */
public class MyObject2 {

    private MyObject1 obj;
    public MyObject2(MyObject1 obj) {
        this.obj = obj;
    }


    public void execute(Exception e) {
        obj.process(e);
    }

    public void run(Exception e) {
        execute(e);
    }

    public void process(Exception e) {
        run(e);
    }
}
