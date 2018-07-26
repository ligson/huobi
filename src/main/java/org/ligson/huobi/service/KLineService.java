package org.ligson.huobi.service;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.ligson.huobi.Bootstrap;
import org.ligson.huobi.vo.KlineResponseVo;

import javax.swing.*;

public class KLineService {
    private static KLineService instance;
    public static double low;


    private KLineService() {
    }

    public void receive(KlineResponseVo responseVo) {
        if (responseVo.getCh().contains("htusdt")) {
            String key = DateFormatUtils.format(responseVo.getTs(), "HH:mm");
            Bootstrap.dataset.addValue(responseVo.getTick().getLow(), "low", key);
            Bootstrap.dataset.addValue(responseVo.getTick().getHigh(), "high", key);
            Bootstrap.dataset.addValue(responseVo.getTick().getClose(), "close", key);
            Bootstrap.dataset.addValue(responseVo.getTick().getOpen(), "open", key);
            if (responseVo.getTick().getLow() <= low) {
                JOptionPane.showConfirmDialog(null, "速度----" + responseVo.getTick());
            }
        }
    }

    public static KLineService getInstance() {
        if (instance == null) {
            instance = new KLineService();
        }
        return instance;
    }
}
