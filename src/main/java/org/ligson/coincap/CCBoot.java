package org.ligson.coincap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ligson.coincap.setting.SettingWin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

@Slf4j
public class CCBoot extends JFrame {
    private SystemTray systemTray;
    private SettingWin settingWin = new SettingWin();
    private PopupMenu popupMenu = new PopupMenu();

    public static Image getImageByString(String path) {
        return Toolkit.getDefaultToolkit().getImage(CCBoot.class.getClassLoader().getResource(path));
    }

    public void init(List<String> coinList) throws Exception {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            Image image = getImageByString("btc.png"); // 定义托盘图标的图片
            TrayIcon ti = new TrayIcon(image, "btctool");
            ti.setImage(image);

            MenuItem settingItem = new MenuItem("设置");
            MenuItem exitItem = new MenuItem("退出");
            popupMenu.setLabel("ppp");
            popupMenu.add(new MenuItem("实时价格"));
            popupMenu.addSeparator();
            for (String coin : coinList) {
                popupMenu.add(new MenuItem(coin + ":-1"));
            }
            popupMenu.addSeparator();
            popupMenu.add(settingItem);
            popupMenu.add(exitItem);
            popupMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(settingItem.getActionCommand())) {
                        settingWin.setVisible(true);
                    } else if (e.getActionCommand().equals(exitItem.getActionCommand())) {
                        System.exit(0);
                    }
                }
            });
            ti.setPopupMenu(popupMenu);
            systemTray.add(ti);
            //throw new Exception("-----");
        } else {
            log.error("system not support tray");
            //System.exit(-1);
        }
    }

    public CCBoot() {
        this.setVisible(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static List<String> getCoinListInner() throws Exception {
        String home = System.getProperty("user.home");
        File configDir = new File(home, ".btctool");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        File configFile = new File(configDir, "config.properties");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(configFile));
        String coins = properties.getProperty("watch.coins");
        if (StringUtils.isBlank(coins)) {
            coins = "bitcoin,ethereum";
            properties.setProperty("watch.coins", coins);
        }
        properties.store(new FileWriter(configFile), "#btc配置");
        List<String> coinList = Arrays.asList(coins.split(","));
        if (coinList.isEmpty()) {
            coinList.add("bitcoin,ethereum");
        }
        return coinList;
    }

    private static List<String> getCoinList() {
        try {
            return getCoinListInner();
        } catch (Exception e) {
            e.printStackTrace();
            return Arrays.asList("bitcoin", "ethereum");
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> coinList = getCoinList();
        CCBoot ccBoot = new CCBoot();
        ccBoot.setVisible(false);
        ccBoot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            ccBoot.init(coinList);
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "程序错误:" + e.getMessage());
            //System.exit(-1);

            String[] options = {"退出"};

            int x = JOptionPane.showOptionDialog(null, "程序错误:" + e.getMessage(),
                    "程序出现错误",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            System.out.println(x);
        }
        //blog.csdn.net/bandaotixiruiqiang/article/details/43051779
        StringJoiner joiner = new StringJoiner(",");
        for (String coinId : coinList) {
            joiner.add(coinId);
        }
        CoinCapClient coinCapClient = new CoinCapClient("wss://ws.coincap.io/prices?assets=" + joiner.toString(), ccBoot.popupMenu);
        coinCapClient.init();
        coinCapClient.start();

    }
}
