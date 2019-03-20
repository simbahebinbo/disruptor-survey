package com.example;

import com.lmax.disruptor.EventTranslatorTwoArg;

class KLineEventTranslator implements EventTranslatorTwoArg<KLineEvent, String, String> {
    @Override
    public void translateTo(KLineEvent event, long sequence, String topic, String strdatas) {

        event.setTopic(topic);
        event.setDatas(strdatas);
    }
}
