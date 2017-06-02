package com.si;

import java.util.HashMap;

/**
 * Created by medialog on 2017. 6. 1..
 */
public class Deal {

    private static String getDealKey(HashMap<String, Float> stat, HashMap<String, Float> deal) {

        String key = "";
        float statTotal = (float)0;
        float nowRatio = (float)0;


        statTotal = sum(stat);
        // 합계
        for( String keys : stat.keySet() ){  statTotal += stat.get(keys);
        }
        // 비율
        for( String keys : stat.keySet() ){
            nowRatio = stat.get(keys)/statTotal;

            if (nowRatio < deal.get(keys)) {
                key = keys;
                break;
            }
        }

        return key;
    }

    private static float sum(HashMap<String, Float> map) {
        float statTotal = (float)0;
        // 합계
        for( String keys : map.keySet() ){
            statTotal += map.get(keys);
        }

        return statTotal;
    }

    public static void main(String[] args) {

        HashMap<String, Float> stat = new HashMap<String, Float>();

        HashMap<String, Float> deal = new HashMap<String, Float>();

        stat.put("host1", (float)0);
        stat.put("host2", (float)0);
        stat.put("host3", (float)0);

        deal.put("host1", (float)10);
        deal.put("host2", (float)7);
        deal.put("host3", (float)3);

        Float dealTotal = sum(deal);

        // 비율
        for( String key : deal.keySet() ){
            deal.put(key,deal.get(key)/dealTotal);
        }
        // 출력
        for( String key : deal.keySet() ){
            System.out.println( "key="+key + " ratio=" +deal.get(key));
        }


        for (int i = 0; i < 1000; i++) {


        }
    }
}
