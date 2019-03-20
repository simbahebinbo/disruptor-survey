package com.example;

import com.lmax.disruptor.WorkHandler;


public class LongEventWorker implements WorkHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event) throws Exception {
        System.out.println(Thread.currentThread().getName() + ":" + event.getValue());
    }
}
