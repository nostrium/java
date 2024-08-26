package online.nostrium.servers.web;

import io.undertow.Undertow;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class UndertowWebSocketServer {

    public static void main(String[] args) {
//        Undertow server = Undertow.builder()
//                .addHttpListener(8080, "localhost")
//                .setHandler(exchange -> {
//                    exchange.upgradeChannel((WebSocketConnectionCallback) (WebSocketHttpExchange exchange1, WebSocketChannel channel) -> {
//                        channel.getReceiveSetter().set(new AbstractReceiveListener() {
//                            @Override
//                            protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
//                                WebSockets.sendText("Echo: " + message.getData(), channel, null);
//                            }
//                        });
//                        channel.resumeReceives();
//                    });
//                }).build();
//        server.start();
    }
}
