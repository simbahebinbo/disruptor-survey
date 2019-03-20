package com.example;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

public class KLineEventProducer {
    private final RingBuffer<KLineEvent> ringBuffer;

    public KLineEventProducer(RingBuffer<KLineEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorTwoArg<KLineEvent, String, String> TRANSLATOR = new EventTranslatorTwoArg<KLineEvent, String, String>() {
        @Override
        public void translateTo(KLineEvent event, long sequence, String topic, String strdatas) {
            event.setTopic(topic);
            event.setDatas(strdatas);
        }
    };


    public void onData(String topic, String strdatas) {
        ringBuffer.publishEvent(TRANSLATOR, topic, strdatas);
    }
}

