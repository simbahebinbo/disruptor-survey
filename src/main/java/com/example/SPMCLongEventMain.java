package com.example;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

//单生产者多消费者
public class SPMCLongEventMain {

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
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

        // Connect the handler
        disruptor.handleEventsWithWorkerPool(new LongEventWorker(), new LongEventWorker());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);

        Long bb;
        for (long l = 1; l < 10; l++) {
            bb = l;
            producer.onData(bb);
        }
    }
}
