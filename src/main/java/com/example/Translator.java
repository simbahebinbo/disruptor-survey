package com.example;

import com.lmax.disruptor.EventTranslatorOneArg;

import java.nio.ByteBuffer;

class Translator implements EventTranslatorOneArg<LongEvent, ByteBuffer> {
    @Override
    public void translateTo(LongEvent event, long sequence, ByteBuffer data) {
        event.setValue(data.getLong(0));
    }
}
