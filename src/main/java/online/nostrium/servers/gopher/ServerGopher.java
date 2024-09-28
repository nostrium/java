/*
 * Server for the gopher network
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.gopher;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import online.nostrium.main.core;
import online.nostrium.servers.Server;

import java.io.File;
import online.nostrium.folder.FolderUtils;
import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;
import java.nio.charset.Charset;

/**
 * @author Brito
 * @date: 2024-09-27
 * @location: Germany
 */
public class ServerGopher extends Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public String getId() {
        return "Server_Gopher";
    }

    @Override
    public int getPort() {
        return core.config.debug ? core.config.portGopher_Debug : core.config.portGopher;
    }

    @Override
    public int getPortSecure() {
        return -1;  // No HTTPS for Gopher
    }

    @Override
    protected void boot() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            isRunning = true;
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecoder(Charset.forName("UTF-8")));  // Decode incoming bytes into strings
                        p.addLast(new StringEncoder(Charset.forName("UTF-8")));  // Encode outgoing messages into strings
                        p.addLast(new GopherRequestHandler());  // Custom handler for Gopher requests
                    }
                });

            int port = getPort();
            Channel ch = b.bind(port).sync().channel();
            System.out.println("Gopher server started on port " + port);

            // Wait for the server to close
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            isRunning = true;
        } finally {
            isRunning = true;
            shutdown();
        }
    }

    @Override
    protected void shutdown() {
        if (bossGroup != null && workerGroup != null) {
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
                System.out.println("Gopher server shut down successfully.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GopherRequestHandler extends SimpleChannelInboundHandler<String> {

        public int getPort() {
            return core.config.debug ? core.config.portGopher_Debug : core.config.portGopher;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String request) {
            // Basic Gopher request handling, expecting a simple path
            if (request.trim().isEmpty()) {
                sendMenu(ctx);  // Send default menu
            } else {
                handleRequest(ctx, request.trim());
            }
        }

        private void handleRequest(ChannelHandlerContext ctx, String path) {
            Log.write("GOPHER", TerminalCode.VISIT, "New request", path);
            File file = new File(FolderUtils.getFolderWWW(), path);
            if (file.exists()) {
                if (file.isDirectory()) {
                    sendMenu(ctx, file);  // Serve directory as a Gopher menu
                } else {
                    sendFile(ctx, file);  // Serve file content
                }
            } else {
                sendError(ctx);  // Handle error
            }
        }

        private void sendMenu(ChannelHandlerContext ctx) {
            // Simple hardcoded menu for the Gopher server
            StringBuilder menu = new StringBuilder();
            menu.append("1Welcome to the Nostrium Gopher Server!\t/\t").append(core.config.getDomain()).append("\t").append(core.config.portGopher).append("\r\n");
            menu.append("0Read the first file\tfile1.txt\t").append(core.config.getDomain()).append("\t").append(core.config.portGopher).append("\r\n");
            menu.append("0Read the second file\tfile2.txt\t").append(core.config.getDomain()).append("\t").append(core.config.portGopher).append("\r\n");
            menu.append(".\r\n");  // End of Gopher menu
            ctx.writeAndFlush(menu.toString()).addListener(ChannelFutureListener.CLOSE);
        }

        private void sendMenu(ChannelHandlerContext ctx, File directory) {
            // Dynamically list the contents of a directory as a Gopher menu
            StringBuilder menu = new StringBuilder();
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    menu.append("1").append(file.getName()).append("\t")
                        .append(file.getName()).append("\t")
                        .append(core.config.getDomain()).append("\t")
                            .append(core.config.portGopher).append("\r\n");
                } else {
                    menu.append("0").append(file.getName())
                        .append("\t").append(file.getName())
                        .append("\t").append(core.config.getDomain())
                        .append("\t").append(getPort())
                        .append("\r\n");
                }
            }
            menu.append(".\r\n");  // End of Gopher menu
            ctx.writeAndFlush(menu.toString()).addListener(ChannelFutureListener.CLOSE);
        }

        private void sendFile(ChannelHandlerContext ctx, File file) {
            try {
                StringBuilder content = new StringBuilder();
                java.nio.file.Files.lines(file.toPath()).forEach(line -> content.append(line).append("\r\n"));
                ctx.writeAndFlush(content.toString()).addListener(ChannelFutureListener.CLOSE);
            } catch (Exception e) {
                sendError(ctx);
            }
        }

        private void sendError(ChannelHandlerContext ctx) {
            ctx.writeAndFlush("3Error: File or directory not found.\r\n").addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
    
    public static void main(String[] args) {
        try {
            core.startConfig();
            ServerGopher server = new ServerGopher();
            server.start();  // Start the Gopher server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
