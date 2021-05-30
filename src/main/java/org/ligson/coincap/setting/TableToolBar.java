package org.ligson.coincap.setting;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TableToolBar extends JToolBar {
    private JTableHeader jTableHeader;
    private java.util.List<String> showCols = new ArrayList<>();

    private void menuItemClick(ComponentEvent e) {
        System.out.println("-----------------");
        JMenuItem jMenuItem = (JMenuItem) e.getSource();
        String text = jMenuItem.getText();
        if (showCols.contains(text)) {
            showCols.remove(text);
            Enumeration<TableColumn> columnEnumeration = jTableHeader.getColumnModel().getColumns();
            List<TableColumn> columns = new ArrayList<>();
            List<TableColumn> removeColumns = new ArrayList<>();
            while (columnEnumeration.hasMoreElements()) {
                TableColumn col = columnEnumeration.nextElement();
                if (col.getHeaderValue() == text) {
                    removeColumns.add(col);
                }
                columns.add(col);
            }
            columns.removeAll(removeColumns);
            while (columnEnumeration.hasMoreElements()) {
                TableColumn col = columnEnumeration.nextElement();
                jTableHeader.getColumnModel().removeColumn(col);
            }
            for (TableColumn column : columns) {
                jTableHeader.getColumnModel().addColumn(column);
            }
        } else {
            showCols.add(text);
        }
    }

    public TableToolBar(JTableHeader jTableHeader) {
        add(new JTextField());


        java.util.List<String> data = new ArrayList<>();


        Enumeration<TableColumn> columnEnumeration = jTableHeader.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn col = columnEnumeration.nextElement();
            data.add(col.getHeaderValue() + "");
        }
        MultiComboBox multiComboBox = new MultiComboBox(data);

        multiComboBox.addItemActionListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                menuItemClick(e);
            }
        });
        add(multiComboBox);
    }
}
