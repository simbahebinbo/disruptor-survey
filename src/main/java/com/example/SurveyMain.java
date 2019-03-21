package com.example;

import com.google.common.collect.EvictingQueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class SurveyMain {

    public static void main(String[] args) {
        ConcurrentHashMap<String, EvictingQueue<KlineVO>> testMap = new ConcurrentHashMap<>();

        long start = 0;
        long end = 0;
        // 先垃圾回收
        System.gc();
        start = Runtime.getRuntime().freeMemory();
        IntStream.range(0, 4000).forEach(i -> {
            String topic = "market.eosusdt.kline.1min" + i;
            EvictingQueue<KlineVO> datas = EvictingQueue.create(2000);
            KlineVO data = new KlineVO();
            data.setId(1537325760L);
            data.setOpen(5.1214);
            data.setClose(5.1199);
            data.setLow(5.117);
            data.setHigh(5.1223);
            data.setAmount(1947.6461);
            data.setVol(9972.01690388);
            data.setCount(18L);

            IntStream.range(0, 2000).forEach(j -> datas.add(data));

            testMap.put(topic, datas);
        });

        // 快要计算的时,再清理一次
        System.gc();
        end = Runtime.getRuntime().freeMemory();

        System.out.println("占内存:" + (start - end) / 1000.0 + "M");
    }
}


