/*
 * Replies to Finger requests
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.finger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import online.nostrium.utils.cybersec.TextSanitation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * To test in debug mode, use this:
 *  
 *      echo "user1" | nc localhost 7900
 * 
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class ServerFinger extends Server {

    // Map to track the last request time for each IP address
    private static final Map<String, Long> requestTimes = new ConcurrentHashMap<>();

    @Override
    public String getId() {
        return "Server_Finger";
    }

    @Override
    public int getPort() {
        if (core.config.debug) {
            return core.config.portFinger_Debug;
        } else {
            return core.config.portFinger;
        }
    }

    @Override
    protected void boot() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                //.handler(new LoggingHandler(LogLevel.ERROR))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                            new StringDecoder(),
                            new StringEncoder(),
                            new ChunkedWriteHandler(),
                            new FingerServerHandler() // Custom handler for finger requests
                        );
                    }
                });
            isRunning = true;

            // Start the scheduled cleanup task
            startCleanupTask();

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(getPort()).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void startCleanupTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            long expirationTime = TimeUnit.MINUTES.toMillis(5); // 5 minutes expiration
            requestTimes.entrySet().removeIf(entry -> currentTime - entry.getValue() > expirationTime);
        }, 5, 5, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        new ServerFinger().boot();
    }

    // Inner class to handle the finger requests with rate limiting
    private static class FingerServerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
            String clientIp = ctx.channel().remoteAddress().toString();
            long currentTime = System.currentTimeMillis();

            // Check if the IP has made a request in the last 5 seconds
            if (requestTimes.containsKey(clientIp)) {
                long lastRequestTime = requestTimes.get(clientIp);
                if (currentTime - lastRequestTime < TimeUnit.SECONDS.toMillis(5)) {
                    ctx.writeAndFlush("Too many requests. Please wait before making another request.\n");
                    return;
                }
            }

            // Update the last request time for this IP
            requestTimes.put(clientIp, currentTime);

            String response;
            String notFound = "No such user";
            request = request.trim();

            // invalid request format
            if (TextSanitation.checkRequest(request) == false) {
                response = notFound + "\n";
                ctx.writeAndFlush(response);
                return;
            }

            // try to find the user
            User user = UserUtils.getUserByUsername(request);
            if (user == null) {
                response = notFound + "\n";
                ctx.writeAndFlush(response);
                return;
            }

            FingerReply fingerReply = FingerReply.convertUser(user);
            String replyText = fingerReply.toString();

            ctx.writeAndFlush(replyText);
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
