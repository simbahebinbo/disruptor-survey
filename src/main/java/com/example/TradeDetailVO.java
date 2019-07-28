package com.example;

import lombok.Data;

import java.util.List;

@Data
public class TradeDetailVO {
  private Long id;
  private Long ts;
  private List<DetailInnerDataVO> data;
}
