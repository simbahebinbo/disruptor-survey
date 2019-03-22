package com.example;

import com.google.common.collect.EvictingQueue;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.concurrent.ConcurrentHashMap;

//测试内存消耗
public class SurveyMain {


    public static void main(String[] args) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, EvictingQueue<KlineVO>>> testMap = new ConcurrentHashMap<>();

        KlineVO kl = new KlineVO();
        kl.setId(1537325760L);
        kl.setOpen(5.1214);
        kl.setClose(5.1199);
        kl.setLow(5.117);
        kl.setHigh(5.1223);
        kl.setAmount(1947.6461);
        kl.setVol(9972.01690388);
        kl.setCount(18L);

        System.out.printf("sizeOf = %s M\n", (RamUsageEstimator.sizeOf(kl) * 400 * 10 * 2000) / (1024.0 * 1024));


    }
}


