package com.example;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


public class MPSCKLineEventMain {
    public static void main(String[] args) throws Exception {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger index = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread((ThreadGroup) null, r, "consumer-thread-" + index.getAndIncrement());
            }
        };

        KLineEventFactory factory = new KLineEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 8192;

        // Construct the Disruptor
        Disruptor<KLineEvent> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.MULTI,
                new YieldingWaitStrategy());


        // Connect the handler
        disruptor.handleEventsWith(new KLineEventConsumer());

        // Start the Disruptor, starts all threads running
        disruptor.start();


        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("producer-pool-%d").build();
        ExecutorService es = new ThreadPoolExecutor(3, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(8192), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 3; i++) {
            es.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                // 生产者要使用 Disruptor 的环形数组
                RingBuffer<KLineEvent> ringBuffer = disruptor.getRingBuffer();
                KLineEventProducer producer = new KLineEventProducer(ringBuffer);

                // 模拟消息发送
                for (int j = 0; j < 10; j++) {
                    String topic = "market.eosusdt.kline.1min" + j;

                    List<KlineVO> datas = new ArrayList<>();
                    KlineVO data = new KlineVO();
                    data.setId(1537325760L);
                    data.setOpen(5.1214);
                    data.setClose(5.1199);
                    data.setLow(5.117);
                    data.setHigh(5.1223);
                    data.setAmount(1947.6461);
                    data.setVol(9972.01690388);
                    data.setCount(18L);

                    IntStream.range(0, 1).forEach(k -> datas.add(data));

                    Gson gson = new Gson();
                    String strdata = gson.toJson(datas);

                    producer.onData(topic, strdata);
                }

            });
        }
    }
}

