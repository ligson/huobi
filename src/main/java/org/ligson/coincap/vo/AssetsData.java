package org.ligson.coincap.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssetsData {
    private List<CoinInfo> data = new ArrayList<>();
    private Date timestamp = new Date();

    public List<CoinInfo> getData() {
        return data;
    }

    public void setData(List<CoinInfo> data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
