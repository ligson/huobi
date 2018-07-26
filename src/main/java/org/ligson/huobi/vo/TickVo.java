package org.ligson.huobi.vo;

import lombok.Data;

@Data
public class TickVo {
    private long id;
    private double open;
    private double close;
    private double low;
    private double high;
    private double amount;
    private double vol;
    private int count;
}
