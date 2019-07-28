package com.example;

import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 *
 * @author: simba
 */
public class H1BLongEventHandler implements EventHandler<LongEvent> {

  @Override
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("h1b : " + Thread.currentThread().getName() + ":" + "\t消费者：" + event.getValue());
  }
}

