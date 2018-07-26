package org.ligson.huobi;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.ligson.huobi.service.KLineService;

public class Bootstrap extends ApplicationFrame {
    public static DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public static void main(String[] args) throws Exception {
        HuoBiClient huoBiClient = new HuoBiClient("wss://www.hbg.com/-/s/pro/ws");
        huoBiClient.init();
        huoBiClient.start();
        //, "btcusdt"
        String[] symbols = new String[]{"htusdt"};
        huoBiClient.subscribe(symbols);
        KLineService.low = 3.51;
        Bootstrap chart = new Bootstrap(
                "HT-USDT",
                "HT LOW VALUE");

        chart.pack();
        //RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }


    public Bootstrap(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "时间", "最低交易值",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }
}
