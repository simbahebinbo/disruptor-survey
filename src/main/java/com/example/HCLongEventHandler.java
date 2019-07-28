package com.example;

import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 *
 * @author: simba
 */
public class HCLongEventHandler implements EventHandler<LongEvent> {

  @Override
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("hc : " + Thread.currentThread().getName() + ":" + "\t消费者：" + event.getValue());
  }
}

