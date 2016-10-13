package com.globo.graylog2.gelf;

import com.globo.graylog2.gelf.obj1.MyObject1;
import com.globo.graylog2.gelf.obj2.MyObject2;
import com.globo.graylog2.gelf.obj3.MyObject3;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {

    static final int PRINT_THREAD_COUNT_MSG = 10000;

    public static final org.apache.log4j.Logger log4j1Logger = org.apache.log4j.Logger.getLogger(Main.class);
    public static final org.apache.logging.log4j.Logger log4j2Logger = org.apache.logging.log4j.LogManager.getLogger(Main.class);

    public static void logError(int log4jVersion, String msg) {
        if (log4jVersion == 1) {
            log4j1Logger.error(msg);
        } else {
            log4j2Logger.error(msg);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = new Date().getTime();

        int max = 2500;
        if (args[0] != null){
            max = Integer.parseInt(args[0]);
        }
        int threads = 1;
        if (args[1] != null) {
            threads = Integer.parseInt(args[1]);
        }

        boolean bigstack = false;
        if (args[2] != null) {
            bigstack = Boolean.valueOf(args[2]);
        }

        int log4jVersion = 1;
        if (args[3] != null) {
            log4jVersion = Integer.valueOf(args[3]);
        }

        List<MyTask> tasks = new ArrayList<>();
        List<Thread> ths = new ArrayList<>();
        for (int t=0; t < threads; t++) {

            try {
                MyTask task = new MyTask(t, max, bigstack, log4jVersion);
                tasks.add(task);
                Thread thred = new Thread(task);
                ths.add(thred);
                thred.start();
            } catch (Exception e) {
                logError(1, "error executing thread");
            }
        }

        while( !isAllThreadsTerminateds(ths) ) {
            Thread.sleep(300);
        }

        long finalDuration = new Date().getTime() - start;
        for ( MyTask task : tasks) {
            System.out.println("duration: " + (task.duration / 1000.0));
        }


        printReport(max, threads, bigstack, finalDuration);
    }

    private static void printReport(int max, int threads, boolean bigstack, long finalDuration) {
        long totalMsg = threads * max;
        System.out.println("final duration: " + (finalDuration / 1000.0));
        System.out.println("total messages: " + threads * max);


        double msgduration = totalMsg/(double)finalDuration;

        String tag = System.getProperty("ta");
        String protocol = System.getProperty("type");

        String size= "165";
        if (bigstack) {
            size = "2500";
        }

        String middle = ",";
        String startS = "=SPLIT(\"";
        String end = "\";\",\")";
        double finalTime =finalDuration/1000.0;
        DecimalFormat df = new DecimalFormat("#.00");
        String result = startS +
                protocol + middle +
                size + middle +
                tag + middle +
                threads + middle +
                totalMsg + middle +
                df.format(finalTime) + middle +
                df.format(msgduration) + end;


        StringSelection selection = new StringSelection(result);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        System.out.println(result);
        System.out.println("finished");
    }

    private static boolean isAllThreadsTerminateds(List<Thread> ths) {

        for ( Thread th : ths){
            if (th.getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }


    protected static class MyTask implements Runnable {

//        public static final Logger logger = LogManager.getLogger(MyTask.class);

        int max;
        long duration = 0;
        int id;
        boolean bigstack;
        int log4jVersion;

        public MyTask(int id, int max, boolean bigstack, int log4jVersion){
            this.id = id;
            this.max = max;
            this.bigstack = bigstack;
            this.log4jVersion = log4jVersion;
        }

        @Override
        public void run() {
            Exception e;

            if (bigstack) {
                e = createMyTerribleException();
            } else {
                e = new RuntimeException("short stacktrace");
            }

//            logger.debug("Start: " + max);
            long time = new Date().getTime();
            for (int i = 0; i < max; i++) {
                logError(this.log4jVersion, "error testing log asdfgasdfgasdfgasdf");
                int m = i % PRINT_THREAD_COUNT_MSG;
                if ( m == 0  ){
                    System.out.println("t: " + id + ", " + i);
                }

            }
            long endtime = new Date().getTime();

            duration = endtime - time;
        }
    }


    public static Exception createMyTerribleException() {
        try {
            MyObject3 obj = new MyObject3(new MyObject2(new MyObject1()));
            obj.process(null);
        }catch (Exception e) {
            try {
                MyObject3 obj2 = new MyObject3(new MyObject2(new MyObject1()));
                obj2.process(e);
            }catch (Exception ex) {
                try {
                    MyObject3 obj2 = new MyObject3(new MyObject2(new MyObject1()));
                    obj2.process(e);
                }catch (Exception ex1) {
                    MyObject3 obj2 = new MyObject3(new MyObject2(new MyObject1()));
                    try {
                        obj2.process(ex1);
                    } catch (Exception ef) {
                        return ef;
                    }
                }
            }
        }
        return null;
    }

}