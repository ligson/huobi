package org.ligson.coincap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.ligson.huobi.service.KLineService;
import org.ligson.huobi.util.GzipUtil;
import org.ligson.huobi.util.JsonUtil;
import org.ligson.huobi.vo.KlineResponseVo;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/***
 * https://docs.coincap.io/#37dcec0b-1f7b-4d98-b152-0217a6798058
 */
@Slf4j
public class CoinCapWSClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    private KLineService kLineService = KLineService.getInstance();
    private PopupMenu popupMenu;


    public CoinCapWSClientHandler(WebSocketClientHandshaker handshaker, PopupMenu popupMenu) {
        this.handshaker = handshaker;
        this.popupMenu = popupMenu;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("WebSocket Client disconnected!");
    }

    public void send(Channel channel, Object msg) {
        String returnMsg = JsonUtil.toJson(msg);
        log.debug("返回消息:{}", returnMsg);
        TextWebSocketFrame binaryWebSocketFrame = new TextWebSocketFrame(returnMsg);
        channel.writeAndFlush(binaryWebSocketFrame);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        log.debug(msg.getClass().getName());
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            log.debug("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String json = textFrame.text();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            Iterator<String> iter = node.fieldNames();
            while (iter.hasNext()) {
                String name = iter.next();
                String value = node.get(name).asText();
                int count = popupMenu.getItemCount();
                for (int i = 0; i < count; i++) {
                    MenuItem item = popupMenu.getItem(i);
                    String label = item.getLabel();
                    String[] arr = label.split(":");
                    String lname = arr[0];
                    if (lname.equals(name)) {
                        item.setLabel(name + ":" + value);
                    }
                }
            }
            log.debug("WebSocket Client received message: " + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            log.debug("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            log.debug("WebSocket Client received closing");
            ch.close();
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            byte[] by = new byte[frame.content().readableBytes()];
            binaryWebSocketFrame.content().readBytes(by);
            String result = GzipUtil.decompressAsString(by);
            assert result != null;
            log.debug("接受的消息:{}", result);
            JsonNode node = JsonUtil.readNode(result);
            assert node != null;
            if (node.has("ping")) {
                Long ping = node.get("ping").asLong();
                Map<String, Object> returnTxt = new HashMap<>();
                returnTxt.put("pong", ping);
                send(ctx.channel(), returnTxt);
            } else if (node.has("ch")) {
                KlineResponseVo klineResponseVo = JsonUtil.readObject(result, KlineResponseVo.class);
                if (klineResponseVo != null) {
                    kLineService.receive(klineResponseVo);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
