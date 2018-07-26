package org.ligson.huobi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;


public class MyChannelInitializer extends ChannelInitializer<Channel> {
    private WebSocketClientHandler clientHandler;

    public MyChannelInitializer(WebSocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
        sslEngine.setUseClientMode(true);
        pipeline.addLast(new SslHandler(sslEngine));
        pipeline.addLast(new HttpContentDecompressor());
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(clientHandler);
    }
}
