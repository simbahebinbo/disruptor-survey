package com.example;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.sun.java.accessibility.util.Translator;

/**
 * 复杂的消费者依赖关系: 消费者1b消费时，必须保证消费者1a已经完成对该消息的消费；消费者2b消费时，必须保证消费者2a已经完成对该消息的消费；消费者c3消费时，必须保证消费者1b和2b已经完成对该消息的消费
 */
public class ComplexDisruptorWizardMain {

  private static final Translator TRANSLATOR = new Translator();

  public static void main(String[] args) throws InterruptedException {
    ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger index = new AtomicInteger(1);

      @Override
      public Thread newThread(Runnable r) {
        return new Thread((ThreadGroup) null, r, "consumer-thread-" + index.getAndIncrement());
      }
    };

    int bufferSize = 1024;

    EventHandler<LongEvent> h1a = new H1ALongEventHandler();
    EventHandler<LongEvent> h1b = new H1BLongEventHandler();

    EventHandler<LongEvent> h2a = new H2ALongEventHandler();
    EventHandler<LongEvent> h2b = new H2BLongEventHandler();

    EventHandler<LongEvent> hc = new HCLongEventHandler();

    Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());

    //先让h1a消费，然后才能让h1b消费;先让h2a消费，然后才能让h2b消费
    //先让h1b和h2b消费，然后才能让hc消费
    disruptor.handleEventsWith(h1a, h2a);
    disruptor.after(h1a).then(h1b);
    disruptor.after(h2a).then(h2b);
    disruptor.after(h1b, h2b).then(hc);

    disruptor.start();

    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
    LongEventProducer producer = new LongEventProducer(ringBuffer);

    for (long l = 0; l < 100; l++) {
      producer.onData(l);
      Thread.sleep(1000);
    }
  }
}

