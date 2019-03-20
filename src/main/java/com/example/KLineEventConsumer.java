package com.example;

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
        List<KlineVO> datas = gson.fromJson(strdatas, new TypeToken<List<KlineVO>>() {
        }.getType());
        KLine.testMap.put(topic, datas);
        System.out.println("数据：\t" + KLine.testMap);
    }
}

