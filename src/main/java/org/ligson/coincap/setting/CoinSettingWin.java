package org.ligson.coincap.setting;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class CoinSettingWin extends JPanel {
    private JTable table;

    public CoinSettingWin() {
        super(new BorderLayout());



        // 创建一个表格，指定 所有行数据 和 表头
        CoinInfoTableModel coinInfoTableModel = new CoinInfoTableModel();
        table = new JTable(coinInfoTableModel);
        RowSorter sorter = new TableRowSorter(coinInfoTableModel);
        table.setRowSorter(sorter);
        table.getTableHeader().setBackground(Color.GRAY);
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 16);
        table.getTableHeader().setFont(font);

        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
        //add(table.getTableHeader(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        // 把 表格内容 添加到容器中心
        add(scrollPane, BorderLayout.CENTER);


        TableToolBar tableToolBar = new TableToolBar(table.getTableHeader());
        add(tableToolBar, BorderLayout.NORTH);


    }


}
