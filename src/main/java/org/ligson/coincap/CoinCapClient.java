package org.ligson.coincap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CoinCapClient {
    private Bootstrap connectionBootstrap = new Bootstrap();
    private static final EventLoopGroup group = new NioEventLoopGroup();

    private String wsAddress;
    private URI uri;
    private CoinCapWSClientHandler clientHandler;
    private ChannelFuture channelFuture;
    private PopupMenu popupMenu;

    public CoinCapClient(String wsAddress, PopupMenu popupMenu) throws Exception {
        this.wsAddress = wsAddress;
        uri = new URI(wsAddress);
        this.popupMenu = popupMenu;
    }

    public void start() throws Exception {
        channelFuture = connectionBootstrap.connect(uri.getHost(), 443).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    log.debug("连接：{}成功", wsAddress);
                } else {
                    log.debug("连接：{}失败", wsAddress);
                }
            }
        });
    }

    public void init() throws Exception {
        WebSocketClientHandshaker ss = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, HttpHeaders.EMPTY_HEADERS, 100 * 1024 * 1024);
        clientHandler = new CoinCapWSClientHandler(ss, popupMenu);
        CoinCapChannelInitializer myChannelInitializer = new CoinCapChannelInitializer(clientHandler);
        connectionBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        connectionBootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(myChannelInitializer);
    }

    public void subscribe(String[] symbols) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Channel channel = channelFuture.channel();
        // 订阅深度
        // 谨慎选择合并的深度，ws每次推送全量的深度数据，若未能及时处理容易引起消息堆积并且引发行情延时
        // 订阅K线
        for (String symbol : symbols) {
            Map<String, Object> subMap = new HashMap<>();
            subMap.put("id", symbol);
            subMap.put("sub", "market." + symbol + ".kline.1min");
            clientHandler.send(channel, subMap);
        }
    }
}
