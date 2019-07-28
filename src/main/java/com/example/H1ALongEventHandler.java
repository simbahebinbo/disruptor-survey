package com.example;

import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 *
 * @author: simba
 */
public class H1ALongEventHandler implements EventHandler<LongEvent> {

  @Override
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("h1a : " + Thread.currentThread().getName() + ":" + "\t消费者：" + event.getValue());
  }
}

