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

//import org.apache.log4j.Logger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Main {

    static final int PRINT_THREAD_COUNT_MSG = 10000;
//    public static final Logger logger = Logger.getLogger(Main.class);

    public static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws InterruptedException {
//        BasicConfigurator.configure();
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


        List<MyTask> tasks = new ArrayList<>();
        List<Thread> ths = new ArrayList<>();
        for ( int t=0; t < threads; t++) {

            try {
                MyTask task = new MyTask(t, max, bigstack);
                tasks.add(task);
                Thread thred = new Thread(task);
                ths.add(thred);
                thred.start();
            } catch (Exception e) {
                logger.error("error executing thread", e);
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
//        logger.debug("Messages: " + max);
//        logger.debug("Threads: " + threads);
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

//        public static final Logger logger = Logger.getLogger(MyTask.class);
        public static final Logger logger = LogManager.getLogger(MyTask.class);

        int max;
        long duration = 0;
        int id;
        boolean bigstack;

        public MyTask(int id, int max, boolean bigstack){
            this.id = id;
            this.max = max;
            this.bigstack = bigstack;
        }

        @Override
        public void run() {
            Exception e;
            e = createMyTerribleException();

//            if (bigstack) {
//                e = createMyTerribleException();
//            }else {
//                e = new RuntimeException("short stacktrace");
//            }

            logger.debug("Start: " + max);
            long time = new Date().getTime();
            for (int i = 0; i < max; i++) {
                logger.error("error testing log asaaaaaaaaaaaaaaaaaadfgasdfgasdfgasdf", e);
//                logger2.error("error testing log asdfgasdfgasdfgasdf", e);
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