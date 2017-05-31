package com.si;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by medialog on 2017. 5. 22..
 */
public class Sender {

    final static Logger logger = Logger.getLogger(Sender.class);

    public static void main(String[] args) {


        if (args != null && args.length == 4) {
            int th = Integer.parseInt(args[0]);
            ExecutorService es = Executors.newFixedThreadPool(th);
            for (int i = 0; i < th; i++) {
                es.execute(new SenderThread(args));
            }
            es.shutdown();

        } else {
            logger.info("useage : java -jar Sender.jar [threadCount] [byte] [count] [queueCount]");
        }




    }


}
class SenderThread implements Runnable {

    final static Logger logger = Logger.getLogger(com.si.SenderThread.class);
    String[] args = null;

    public SenderThread(String[] arg) {
        args = arg;
    }
    public void run() {
        Jedis jedis = new Jedis("221.143.48.226", 6379);

        int bt = Integer.parseInt(args[1]);
        int cnt = Integer.parseInt(args[2]);
        int qcnt = Integer.parseInt(args[3]);


        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < bt; j++) {
            buf.append("0");
        }
        logger.info(Thread.currentThread().getName()+": Send packSize="+buf.length()+" count="+Integer.toString(cnt));
        Random ran = new Random();
        int qno = 0;
        for (int i = 0; i < cnt; i++) {
                qno = ran.nextInt(qcnt);
                jedis.lpush("queue"+Integer.toString(qno), buf.toString());
                logger.info(Thread.currentThread().getName()+": lpushed "+"queue"+Integer.toString(qno)+" no="+(i+1));
        }

        logger.info(Thread.currentThread().getName()+": Send finish");
    }
}