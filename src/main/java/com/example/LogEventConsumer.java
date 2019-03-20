package com.example;

import com.lmax.disruptor.EventHandler;

public class LogEventConsumer implements EventHandler<LogEvent> {

    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName() + " | Event : " + event);
    }
}
