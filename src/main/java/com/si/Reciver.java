package com.si;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by medialog on 2017. 5. 22..
 */
public class Reciver {
    final static Logger logger = Logger.getLogger(Reciver.class);
    public static void main(String[] args) throws InterruptedException, ExecutionException {


        if (args != null && args.length == 3) {
            int tcnt = Integer.parseInt(args[0]);
            int to = Integer.parseInt(args[1]);
            int qcnt = Integer.parseInt(args[2]);
            ExecutorService es = Executors.newFixedThreadPool(tcnt*qcnt);
            for (int q = 0; q < qcnt; q++) {
                for (int i = 0; i < tcnt; i++) {
                    es.execute(new ReciverThread("queue"+Integer.toString(q) , to));
                }
            }


            es.shutdown();

        } else {
            logger.info("useage : java -jar Reciver.jar [threadcount] [timeout] [queueCount]");
        }

    }
}

class ReciverThread implements Runnable {
    final static Logger logger = Logger.getLogger(ReciverThread.class);
    int to = 0;
    String qname = "";

    public ReciverThread(String qname, int timeout) {
        this.qname = qname;
        to = timeout;
    }
    public void run() {
        Jedis jedis = new Jedis("221.143.48.226");
        List<String> messages = null;
        int i = 1;
        while(true){
            messages = jedis.brpop(to, qname);
            if (messages != null && messages.size() > 0) {
                logger.info(Thread.currentThread().getName()+": "+qname +" brpop " +i++ );
            } else {
                logger.info(Thread.currentThread().getName()+": "+qname +" wait timeout");
            }

        }
    }
}
