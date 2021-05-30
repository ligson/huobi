package org.ligson.coincap.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ligson.coincap.vo.AssetsData;
import org.ligson.coincap.vo.CoinInfo;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CoinInfoTableModel extends AbstractTableModel {
    private Field[] fields;
    private List<CoinInfo> coinInfos;

    public List<CoinInfo> getData() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://api.coincap.io/v2/assets?limit=2000");
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        HttpEntity entity = response.getEntity();
        String json = null;
        try {
            json = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        ObjectMapper mapper = new ObjectMapper();
        List<CoinInfo> coinList = new ArrayList<>();
        try {
            AssetsData data = mapper.readValue(json, AssetsData.class);
            coinList = data.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return coinList;
    }

    public CoinInfoTableModel() {
        fields = CoinInfo.class.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        coinInfos = getData();
    }

    @Override
    public int getRowCount() {
        return coinInfos.size();
    }

    @Override
    public int getColumnCount() {
        return fields.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return FieldUtils.readDeclaredField(coinInfos.get(rowIndex), fields[columnIndex].getName(), true);
        } catch (IllegalAccessException e) {
            return "无效";
        }
    }

    @Override
    public String getColumnName(int column) {
        return fields[column].getName();
    }


}
