package org.ligson.huobi.vo;

import lombok.Data;

import java.util.Date;

@Data
public class KlineResponseVo {
    private String ch;
    private Date ts;
    private TickVo tick;
}
