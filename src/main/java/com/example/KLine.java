package com.example;

import com.google.common.collect.EvictingQueue;

import java.util.concurrent.ConcurrentHashMap;

public class KLine {
    public static ConcurrentHashMap<String, EvictingQueue<KlineVO>> testMap = new ConcurrentHashMap<>();
}
