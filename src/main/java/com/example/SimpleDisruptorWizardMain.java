package com.example;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 简单的消费者依赖关系: 消费者3消费时，必须保证消费者1和消费者2已经完成对该消息的消费。
 */
public class SimpleDisruptorWizardMain {

  public static void main(String[] args) throws InterruptedException {
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger index = new AtomicInteger(1);

      @Override
      public Thread newThread( Runnable r) {
        return new Thread((ThreadGroup) null, r, "consumer-thread-" + index.getAndIncrement());
      }
    };

    int bufferSize = 1024;

    EventHandler<LongEvent> handler1 = new FirstLongEventHandler();
    EventHandler<LongEvent> handler2 = new SecondLongEventHandler();
    EventHandler<LongEvent> handler3 = new ThirdLongEventHandler();

    Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());

    //先让1和2消费，然后才能让3消费。
    disruptor.handleEventsWith(handler1, handler2).then(handler3);

    disruptor.start();

    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
    LongEventProducer producer = new LongEventProducer(ringBuffer);

    for (long l = 0; l < 100; l++) {
      producer.onData(l);
      Thread.sleep(1000);
    }
  }
}
