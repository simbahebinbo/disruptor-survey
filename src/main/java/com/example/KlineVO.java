package com.example;

import lombok.Data;

@Data
public class KlineVO {
    private Long id;
    private Double open;
    private Double close;
    private Double low;
    private Double high;
    private Double amount;
    private Double vol;
    private Long count;
}
