/*
 * Web server access
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.user.UserUtils;
import online.nostrium.folder.FolderUtils;
import online.nostrium.logs.Log;
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import online.nostrium.servers.ports.PortId;
import online.nostrium.servers.ports.PortType;
import online.nostrium.servers.ports.ServerPort;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.session.SessionUtils;
import online.nostrium.session.maps.AutoComplete;
import online.nostrium.session.maps.MapBox;
import online.nostrium.utils.screens.Screen;

/**
 * Web server implementation with HTTPS support.
 *
 * @Author: Brito
 * @Date: 2024-08-29
 * @Location: Germany
 */
public class ServerWeb extends Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public String getId() {
        return "Server_Web";
    }

    public static void main(String[] args) throws Exception {
        ServerWeb server = new ServerWeb();
        server.start();
    }

    @Override
    public void setupPorts() {
        // add HTTP
        ServerPort portHTTP = new ServerPort(PortId.HTTP.toString(),
                PortType.NONENCRYPTED,
                PortId.HTTP.getPortNumber(),
                PortId.HTTP_Debug.getPortNumber()
        );
        ports.add(portHTTP);

        // add HTTPS
        ServerPort portHTTPS = new ServerPort(PortId.HTTPS.toString(),
                PortType.ENCRYPTED,
                PortId.HTTPS.getPortNumber(),
                PortId.HTTPS_Debug.getPortNumber()
        );
        ports.add(portHTTPS);
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void boot() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        int HTTP_PORT = ports.get(PortId.HTTP);
        int HTTPS_PORT = ports.get(PortId.HTTPS);

        try {
            // Load the SSL context
            SslContext sslCtx = createSslContext();

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();


                            if (sslCtx != null && ch.localAddress().getPort() == HTTPS_PORT) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }

                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpObjectAggregator(65536));
                            p.addLast(new ChunkedWriteHandler());
                            p.addLast(new HttpRequestHandler("/ws"));
                            p.addLast(new WebSocketServerProtocolHandler("/ws"));
                            p.addLast(new WebSocketFrameHandler());
                        }
                    });

            Channel httpChannel = null;
            Channel httpsChannel = null;

            try {
                isRunning = true;
                httpChannel = b.bind(HTTP_PORT).sync().channel();
                //System.out.println("HTTP server started on port " + HTTP_PORT);
            } catch (Exception ex) {
                System.err.println("Failed to start HTTP server on port " + HTTP_PORT + ": " + ex.getMessage());
                ex.printStackTrace();
                isRunning = false;
            }

            if (sslCtx != null) {
                try {
                    httpsChannel = b.bind(HTTPS_PORT).sync().channel();
                    //System.out.println("HTTPS server started on port " + HTTPS_PORT);
                } catch (Exception ex) {
                    System.err.println("Failed to start HTTPS server on port " + HTTPS_PORT + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                System.out.println("SSL context is not available, HTTPS server will not start.");
            }

            // Wait for the server channel(s) to close.
            try {
                if (httpChannel != null) {
                    httpChannel.closeFuture().sync();
                }
                if (httpsChannel != null) {
                    httpsChannel.closeFuture().sync();
                }
            } catch (InterruptedException e) {
                System.err.println("Server interrupted: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Unexpected error during server boot: " + e.getMessage());
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status,
                ctx.alloc().buffer(0));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @SuppressWarnings("UseSpecificCatch")
    private SslContext createSslContext() {
        try {
            File folder = FolderUtils.getFolderCerts();
            File domain = new File(folder, "domain.crt");
            File keyFile = new File(folder, "domain.key"); // Private key

            if (!keyFile.exists()) {
                System.out.println("SSL certificates not found, starting without HTTPS.");
                return null;
            }

            // Specify TLS versions explicitly
            return SslContextBuilder.forServer(domain, keyFile)
                    .protocols("TLSv1.2", "TLSv1.3") // Use TLS 1.2 or TLS 1.3
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create SSL context, starting without HTTPS.");
            return null;
        }
    }

    @Override
    protected void shutdown() {
        if (bossGroup != null && workerGroup != null) {
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
                isRunning = false;
                System.out.println("Web Server shut down successfully.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Handles HTTP requests and routes WebSocket requests
    private static class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        private final String websocketPath;

        public HttpRequestHandler(String websocketPath) {
            this.websocketPath = websocketPath;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
            // Check if this is a WebSocket upgrade request
            if (websocketPath.equalsIgnoreCase(req.uri())) {
                ctx.fireChannelRead(req.retain());
            } else {
                handleHttpRequest(ctx, req);
            }
        }

        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
            // Handle only GET methods
            if (!req.method().equals(HttpMethod.GET)) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                return;
            }

            // Determine the requested URI
            String uri = req.uri();
            String sessionId = getId(ctx);

            // Print the command to the server console
            System.out.println("[" + sessionId + "] requested: " + uri);

            boolean isRootRequest = false;

            // Default to serving index.html if the root is requested
            if ("/".equals(uri)) {
                uri = "/index.html";
                isRootRequest = true;
            }

            // Resolve the full file path in the central archive
            String basePath = FolderUtils.getFolderWWW().getCanonicalPath();
            String fullPath = basePath + uri;
            File file = new File(fullPath);

            if (file.exists() || file.isFile()) {
                // send the file from the public service
                FilesWeb.sendFile(file, ctx);
                return;
            } else if (isRootRequest == false) {
                // file from a specific user
                FilesWeb.sendFileFromUser(req.uri(), ctx);
                return;
            }

            // whatever was asked, we didn't found it
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public static String getId(ChannelHandlerContext ctx) {
        ChannelId channelId = ctx.channel().id();
        return channelId.asLongText();
    }

    // Handles WebSocket frames with character echo and line-based command execution
    private static class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

        // Store a buffer for each connected channel to accumulate text until a full line is received
        private final Map<ChannelHandlerContext, StringBuilder> buffers = new HashMap<>();

        @Override
        @SuppressWarnings("null")
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
                // Get the received text
                String request = textWebSocketFrame.text();

                // Accumulate the new characters in the buffer
                StringBuilder buffer = buffers.get(ctx);

                if (buffer == null) {
                    buffer = new StringBuilder();
                    buffers.put(ctx, buffer);
                }

                // handle the backspace situation
                if (request.equals("\b")) {
                    // reduce the buffer lenght when bigger than zero
                    if (buffer.length() > 0) {
                        buffer.setLength(buffer.length() - 1);
                    }
                    return;
                }

                // handle the backspace situation
                if (request.endsWith("\t")) {
                    Log.write(TerminalCode.OK, "Handling tab autocomplete: " + request);
                    Session session = getSession(ctx);
                    autoComplete(session, request);
                    return;
                }

                // shall we append the new letter?
                buffer.append(request);

                String textCurrent = buffer.toString().trim();

                if (!textCurrent.startsWith(">")) {
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(request));
                }

                if (!textWebSocketFrame.text().contains("\n") && !textWebSocketFrame.text().contains("\r")) {
                    return;
                }

                processCommand(ctx, textCurrent);

                buffers.remove(ctx);

            } else {
                throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
            }
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            buffers.remove(ctx);
            super.handlerRemoved(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        private void autoComplete(Session session, String text) {
            MapBox map = (MapBox) session.getMap();
            Screen screen = session.getScreen();
            // clean up the request, remove the \t character at the end
            text = text.substring(0, text.length() - 1);
            String suggestion = AutoComplete.autoComplete(text, map);
            Log.write(TerminalCode.OK, "Suggestion: " + suggestion);

            if (suggestion.isEmpty()) {
                return;
            }

            // multiple choice
            if (suggestion.contains(" | ")) {
                // try to see if it works
                screen.deleteCurrentLine();
                screen.writeln(suggestion);
                screen.writeUserPrompt(session);
                screen.write(text);
            } else {
                screen.write("\r\n");
                screen.writeUserPrompt(session);
                String output = suggestion.substring(text.length());
                output = "AUTO_COMPLETE:" + output;
                screen.write(suggestion);
                screen.write(output);
            }
        }

        /**
         * Gets the previous session or creates a new one based on the ctx
         * provided
         *
         * @param ctx
         * @return
         */
        private Session getSession(ChannelHandlerContext ctx) {
            String sessionId = getId(ctx);
            Screen screen = new ScreenWeb(ctx);
            Session session;
            if (core.sessions.has(ChannelType.WEB, sessionId)) {
                session = SessionUtils
                        .getOrCreateSession(ChannelType.WEB, sessionId, screen);
            } else {
                // create a new one
                session = SessionUtils
                        .getOrCreateSession(ChannelType.WEB, sessionId, screen);
                session.setScreen(screen);
                UserUtils.checkFirstTimeSetup(session.getUser(), screen);
            }
            return session;
        }

        private void processCommand(ChannelHandlerContext ctx, String textCurrent) {
            Session session = getSession(ctx);
            session.getScreen().writeln("");

            CommandResponse response = session.getApp()
                    .handleCommand(TerminalType.ANSI, textCurrent);

            if (response == null) {
                session.getScreen().writeUserPrompt(session);
                return;
            }

            if (textCurrent.startsWith(">")) {
                textCurrent = textCurrent.substring(1);
                if ("showLogo".equals(textCurrent)) {
                    session.getScreen().writeln(session.getApp().getIntro());
                    session.getScreen().writeUserPrompt(session);
                }
                return;
            }

            if (response.getCode() == TerminalCode.NOT_FOUND && response.getText().isEmpty()) {
                session.getScreen().writeln("Not found");
            }

            if (response.getCode() == TerminalCode.EXIT_APP && session.getApp().appParent != null) {
                session.setApp(session.getApp().appParent);
            }

            if (response.getCode() == TerminalCode.CHANGE_APP) {
                //session.setApp(response.getApp());
                if (session.getApp().appParent != null) {
                    session.getScreen().writeln(session.getApp().getIntro());
                }
            }

            if (response.getText().length() > 0) {
                session.getScreen().writeln(response.getText());
            }

            session.getScreen().writeUserPrompt(session);
        }
    }
}
