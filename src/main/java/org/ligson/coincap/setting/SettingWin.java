package org.ligson.coincap.setting;

import javax.swing.*;
import java.awt.*;

public class SettingWin extends JFrame {
    private static JComponent createTextPanel(String text) {
        // 创建面板, 使用一个 1 行 1 列的网格布局（为了让标签的宽高自动撑满面板）
        JPanel panel = new JPanel(new GridLayout(1, 1));

        // 创建标签
        JLabel label = new JLabel(text);
        label.setFont(new Font(null, Font.PLAIN, 50));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // 添加标签到面板
        panel.add(label);

        return panel;
    }

    public SettingWin() {
        setTitle("退出");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        //tabbedPane.setPreferredSize(new Dimension(300, 200));

        tabbedPane.addTab("币种设置", new CoinSettingWin());
        tabbedPane.addTab("关于", createTextPanel("22222"));
        // tabbedPane.setEnabledAt(1, false);

        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        setContentPane(tabbedPane);
        setVisible(false);
    }
}
