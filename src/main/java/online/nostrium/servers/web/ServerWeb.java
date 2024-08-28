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
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.main.Folder;
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.screens.Screen;

public class ServerWeb extends Server {

    @Override
    public String getId() {
        return "Server_Web";
    }

    public static void main(String[] args) throws Exception {
        ServerWeb server = new ServerWeb();
        server.start();
    }

    @Override
    public int getPort() {
        if (core.config.debug) {
            return core.config.portWeb_Debug;
        } else {
            return core.config.portWeb;
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void boot() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpObjectAggregator(65536));
                            p.addLast(new ChunkedWriteHandler());
                            p.addLast(new HttpRequestHandler("/ws"));  // Handles HTTP requests and routes WebSocket requests
                            p.addLast(new WebSocketServerProtocolHandler("/ws"));
                            p.addLast(new WebSocketFrameHandler()); // Handles WebSocket frames
                        }
                    });

            int PORT = this.getPort();

            try {
                Channel ch = b.bind(PORT).sync().channel();
                isRunning = true;
                ch.closeFuture().sync();

            } catch (Exception ex) {
                System.out.println(getId() + " failed to open port " + PORT);
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
            String uniqueId = getId(ctx);

            // Print the command to the server console
            System.out.println(
                    "["
                    + uniqueId
                    + "] "
                    + "requested: " + uri);

            // Default to serving index.html if the root is requested
            if ("/".equals(uri)) {
                uri = "/index.html";
            }

            // Resolve the full file path
            String basePath = Folder.getFolderWWW().getCanonicalPath();
            String fullPath = basePath + uri;
            //System.out.println("Full file path: " + fullPath);

            // Create a File object for the requested file
            File file = new File(fullPath);

            // Check if the file exists and is readable
            if (file.isHidden() || !file.exists() || !file.isFile()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }

            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                long fileLength = raf.length();

                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                HttpUtil.setContentLength(response, fileLength);

                // Determine the content type
                String contentType = "text/html; charset=UTF-8";
                if (uri.endsWith(".js")) {
                    contentType = "application/javascript";
                } else if (uri.endsWith(".css")) {
                    contentType = "text/css";
                } else if (uri.endsWith(".txt")) {
                    contentType = "text/plain";
                } else if (uri.endsWith(".json")) {
                    contentType = "application/json";
                } else if (uri.endsWith(".png")) {
                    contentType = "image/png";
                } else if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (uri.endsWith(".gif")) {
                    contentType = "image/gif";
                }

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

                // Write the initial line and the header.
                ctx.write(response);

                // Write the content.
                ctx.write(new ChunkedFile(raf, 0, fileLength, 8192), ctx.newProgressivePromise());

                // Write the end marker.
                ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

                // Close the connection once the whole content is written out.
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }

        private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, status,
                    ctx.alloc().buffer(0));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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
        private final Map<ChannelHandlerContext, ContextSession> ctxSessions = new HashMap<>();

        @Override
        @SuppressWarnings("null")
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
            if (frame instanceof TextWebSocketFrame textWebSocketFrame) {
                // Get the received text
                String request = textWebSocketFrame.text();

                // Accumulate the new characters in the buffer
                StringBuilder buffer = buffers.get(ctx);

                // does it exist already, or not?
                if (buffer == null) {
                    // create a new one
                    buffer = new StringBuilder();
                    buffers.put(ctx, buffer);
                }

                // add the text to the buffer
                buffer.append(request);

                // shall the text be ignored?
                String textCurrent = buffer.toString().trim();

                if (textCurrent.startsWith(">") == false) {
                    // Echo each character back to the browser right away
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(request));
                }

                // Check if the original frame contained a newline character to indicate end of a message
                if (textWebSocketFrame.text().contains("\n") == false
                        && textWebSocketFrame.text().contains("\r") == false) {
                    return;
                }

                /////////// start the command processing /////////////
                processCommand(ctx, textCurrent);

                /////////// close the command processing /////////////
                // remove the context from the cache
                buffers.remove(ctx);

            } else {
                throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
            }
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            buffers.remove(ctx);  // Clean up buffer when the handler is removed (client disconnects)
            super.handlerRemoved(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        /**
         * Handle the commands provided by the user
         *
         * @param ctx
         * @param textCurrent
         */
        private void processCommand(ChannelHandlerContext ctx, String textCurrent) {
            // get the context
            ContextSession ctxSession = ctxSessions.get(ctx);

            // session is null, create one
            if (ctxSession == null) {
                // get the unique Id
                String uniqueId = getId(ctx);
                // create the screen
                Screen screen = new ScreenWeb(ctx);
                // start with the basic app
                User user = UserUtils.createUserAnonymous();
                TerminalApp app = new TerminalBasic(screen, user);
                ctxSession
                        = new ContextSession(screen, user, app, uniqueId);
                ctxSessions.put(ctx, ctxSession);
            }
            
            // ping that this account is still alive
            ctxSession.ping();

            // Handle the command request
            CommandResponse response
                    = ctxSession.app.handleCommand(
                            TerminalType.ANSI, textCurrent);

            // Ignore null responses
            if (response == null) {
                // Output the next prompt
                //ctxSession.screen.writeln("");
                ctxSession.screen.writeUserPrompt(ctxSession.app);
                return;
            }

            if (textCurrent.startsWith(">")) {
                textCurrent = textCurrent.substring(1);
                switch (textCurrent) {
                    case ("showLogo"):
                        ctxSession.screen.writeln(ctxSession.app.getIntro());
                        ctxSession.screen.writeUserPrompt(ctxSession.app);
                }
                return;
            }

//                // Is it time to leave?
//                if (response.getCode() == TerminalCode.EXIT_CLIENT) {
//                    out.println(response.getText());
//                    break;
//                }
//                    // Forced session break
//                    if (session.isTimeToStop()) {
//                        break;
//                    }
            // Is it time to go down one app?
            if (response.getCode() == TerminalCode.EXIT_APP
                    && ctxSession.app.appParent != null) {
                ctxSession.screen.writeln("");
                ctxSession.app = ctxSession.app.appParent;
            }

            // Is it time to change apps?
            if (response.getCode() == TerminalCode.CHANGE_APP) {
                ctxSession.app = response.getApp();
                ctxSession.session.setApp(ctxSession.app);
                ctxSession.screen.writeln("");
                if (ctxSession.app.appParent != null) {
                    ctxSession.screen.writeln(
                            ctxSession.app.getIntro()
                    );
                }
            }

            // Output the reply
            if (response.getText().length() > 0) {
                // Output the message
                ctxSession.screen.writeln("\n"+ response.getText());
            }

            // Output the next prompt
            ctxSession.screen.writeUserPrompt(ctxSession.app);

//                // Print the command to the server console
//                System.out.println(
//                        "["
//                        + ctxSession.uniqueId
//                        + "] "
//                        + "Running: "
//                        + textCurrent
//                );
        }
    }
}
