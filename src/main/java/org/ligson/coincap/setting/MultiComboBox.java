package org.ligson.coincap.setting;

import javax.swing.*;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MultiComboBox extends JPanel {
    private List<String> data;
    public List<MouseListener> itemActionListeners = new ArrayList<>();

    public MultiComboBox(List<String> data) {
        JLabel jLabel = new JLabel("选中值:");
        JButton button = new BasicArrowButton(BasicArrowButton.SOUTH);

        add(jLabel);
        add(button);
        JPopupMenu jPopupMenu = new JPopupMenu();
        for (String coinInfo : data) {
            JMenuItem jMenuItem = new JCheckBoxMenuItem();
            jMenuItem.setText(coinInfo);
            for (MouseListener itemActionListener : itemActionListeners) {
                jMenuItem.addMouseListener(itemActionListener);
            }
            jPopupMenu.add(jMenuItem);
        }

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jPopupMenu.isVisible()) {
                    jPopupMenu.show(jLabel, jLabel.getX(), jLabel.getY());
                }
                //jPopupMenu.setVisible(!jPopupMenu.isVisible());
            }
        });

    }

    public void addItemActionListener(MouseListener actionListener) {
        itemActionListeners.add(actionListener);
    }
}
