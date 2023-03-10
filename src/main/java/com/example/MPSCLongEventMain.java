package com.example;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


// 多生产者单消费者
public class MPSCLongEventMain {

    private static final LongEventTranslator TRANSLATOR = new LongEventTranslator();


    public static void main(String[] args) throws Exception {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger index = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread((ThreadGroup) null, r, "consumer-thread-" + index.getAndIncrement());
            }
        };

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());

        // Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());


        // Start the Disruptor, starts all threads running
        disruptor.start();

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("producer-pool-%d").build();
        ExecutorService es = new ThreadPoolExecutor(3, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 3; i++) {
            es.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

                Long bb;
                for (long l = 0; l < 10; l++) {
                    bb = l;
                    ringBuffer.publishEvent(TRANSLATOR, bb);
                }

            });
        }
    }
}

