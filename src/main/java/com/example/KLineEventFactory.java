package com.example;

import com.lmax.disruptor.EventFactory;

public class KLineEventFactory implements EventFactory<KLineEvent> {
    @Override
    public KLineEvent newInstance() {
        return new KLineEvent();
    }
}
