package com.example;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;


public class LongEventProducerWithTranslator {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, Long> TRANSLATOR = (event, sequence, bb) -> event.setValue(bb);

    public void onData(Long bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
