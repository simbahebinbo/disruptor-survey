package com.example;

import java.math.BigInteger;

import org.apache.lucene.util.RamUsageEstimator;

//测试内存消耗
public class SurveyMain {


  public static void main(String[] args) {
    KlineVO kl = new KlineVO();
    kl.setId(1537325760L);
    kl.setOpen(5.1214);
    kl.setClose(5.1199);
    kl.setLow(5.117);
    kl.setHigh(5.1223);
    kl.setAmount(1947.6461);
    kl.setVol(9972.01690388);
    kl.setCount(18L);

    System.out.printf("sizeOf = %s \n", RamUsageEstimator.sizeOf(kl));
    System.out.printf("sizeOf = %s M\n", (RamUsageEstimator.sizeOf(kl) * 400 * 10 * 2000) / (1024.0 * 1024));

    DetailInnerDataVO did = new DetailInnerDataVO();
    did.setAmount(1947.6461);
    did.setDirection("buy");
    did.setId(BigInteger.valueOf(1537325760));
    did.setPrice(5.11);
    did.setTs(1000000L);
    System.out.printf("sizeOf = %s \n", RamUsageEstimator.sizeOf(did));
    System.out.printf("sizeOf = %s M\n", (RamUsageEstimator.sizeOf(did) * 400 * 2000) / (1024.0 * 1024));

    DepthVO d = new DepthVO();
    d.setAsk(1947.6461);
    d.setBid(1947.6461);
    System.out.printf("sizeOf = %s \n", RamUsageEstimator.sizeOf(d));
    System.out.printf("sizeOf = %s M\n", (RamUsageEstimator.sizeOf(d) * 150 * 2) / (1024.0 * 1024));

  }
}


