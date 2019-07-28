package com.example;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class LogEventProducer {

  private final RingBuffer<LogEvent> ringBuffer;

  public LogEventProducer(RingBuffer<LogEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  private static final EventTranslatorOneArg<LogEvent, String> TRANSLATOR = (event, sequence, bb) -> {
    event.setMsg(bb);
  };

  public void onData(String msg) {
    ringBuffer.publishEvent(TRANSLATOR, msg);
  }
}

