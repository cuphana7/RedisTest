package com.si;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by medialog on 2017. 5. 26..
 */
public class Cash {

    final static Logger logger = Logger.getLogger(com.si.Cash.class);

    public static void main(String[] args) throws InterruptedException {

        if (args != null && args.length == 5) {
            Jedis jedis = new Jedis("221.143.48.226", 6379);

            jedis.set(args[0]+"-Cash", args[1]);
            Float cash = Float.parseFloat(args[1]);
            int tcnt = Integer.parseInt(args[2]);
            Float unit = Float.parseFloat(args[3]);
            int downCnt = Integer.parseInt(args[4]);

            Float doneCash = cash - ( unit*downCnt*tcnt );

            logger.info(args[0]+"-Cash="+cash+ " DoneCash="+doneCash+" wait 10 Sec");

            Thread.sleep(10000);


            ExecutorService es = Executors.newFixedThreadPool(tcnt);
            for (int i = 0; i < tcnt; i++) {
                es.execute(new CashDown(args));
            }
            es.shutdown();



        } else {
            logger.info("useage : java -jar Sender.jar [user] [initCash] [threadCount] [unitCost] [downCount Of Thread]");
        }
    }
}


class CashDown implements Runnable {

    final static Logger logger = Logger.getLogger(com.si.CashDown.class);
    String[] args = null;

    public CashDown(String[] arg) {
        args = arg;
    }
    public void run() {
        Jedis jedis = new Jedis("221.143.48.226", 6379);

        Float cash = Float.parseFloat(args[1]);
        int tcnt = Integer.parseInt(args[2]);
        Float unit = Float.parseFloat(args[3]);
        int downCnt = Integer.parseInt(args[4]);
        String key = args[0]+"-Cash";

        for (int i = 0; i < downCnt; i++) {
            jedis.incrByFloat(key, unit*-1);
            logger.info(key+"="+jedis.get(key));
        }
        logger.info(Thread.currentThread().getName()+": Cash finish "+key+"="+jedis.get(key));
    }
}