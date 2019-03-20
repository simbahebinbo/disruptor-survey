package com.example;

import com.lmax.disruptor.EventTranslatorOneArg;

class LongEventTranslator implements EventTranslatorOneArg<LongEvent, Long> {
    @Override
    public void translateTo(LongEvent event, long sequence, Long data) {
        event.setValue(data);
    }
}
