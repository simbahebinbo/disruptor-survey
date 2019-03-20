package com.example;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class MultiProducerMultiConsumerMain {
    private static final Translator TRANSLATOR = new Translator();

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
        disruptor.handleEventsWithWorkerPool(new LongEventWorker(), new LongEventWorker());


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

                ByteBuffer bb = ByteBuffer.allocate(8);
                for (long l = 0; l < 10; l++) {
                    bb.putLong(0, l);
                    ringBuffer.publishEvent(TRANSLATOR, bb);
                }

            });
        }

        es.shutdown();
        disruptor.shutdown();
    }
}
