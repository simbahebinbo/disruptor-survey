package com.example;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DetailInnerDataVO {

  private BigInteger id;
  private Long ts;
  private Double amount;
  private Double price;
  private String direction;
}
