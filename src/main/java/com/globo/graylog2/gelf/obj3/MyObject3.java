package com.globo.graylog2.gelf.obj3;

import com.globo.graylog2.gelf.obj1.MyObject1;
import com.globo.graylog2.gelf.obj2.MyObject2;

/**
 * Created by lucas.castro on 10/7/16.
 */
public class MyObject3 {

    private MyObject2 obj;
    public MyObject3(MyObject2 obj) {
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
