package com.example;

import com.google.common.collect.EvictingQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lmax.disruptor.EventHandler;

import java.util.List;

/**
 * 消费者
 *
 * @author: simba
 */
public class KLineEventConsumer implements EventHandler<KLineEvent> {

    @Override
    public void onEvent(KLineEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName());
        Gson gson = new Gson();
        String topic = event.getTopic();
        String strdatas = event.getDatas();
        List<KlineVO> listdata = gson.fromJson(strdatas, new TypeToken<List<KlineVO>>() {
        }.getType());

        EvictingQueue<KlineVO> datas;
        if (KLine.testMap.containsKey(topic)) {
            System.out.println("更新" + listdata.size() + "个");
            datas = KLine.testMap.get(topic);

            listdata.forEach(data -> {
                datas.add(data);
            });

        } else {
            System.out.println("新增" + listdata.size() + "个");
            datas = EvictingQueue.create(2000);
            listdata.forEach(data -> {
                datas.add(data);
            });
            KLine.testMap.put(topic, datas);
        }

        KLine.testMap.forEach((k, v) -> {
            System.out.println("topic : " + k + "\tdatas count : " + v.size());
        });
    }
}

