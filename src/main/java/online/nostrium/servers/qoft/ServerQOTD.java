/*
 * Replies to Quote Of The Day requests
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.qoft;

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
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import online.nostrium.servers.ports.PortId;
import online.nostrium.servers.ports.PortType;
import online.nostrium.servers.ports.ServerPort;

import static online.nostrium.servers.qoft.QOTD.generateQuote;

/**
 * 
 * To test this locally (on production is port 17):
 *  
 *   telnet localhost 1700
 * 
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class ServerQOTD extends Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public String getId() {
        return "Server_QOTD";
    }
    
    @Override
    public void setupPorts() {
        ServerPort port = new ServerPort(PortId.QOTD.toString(),
                PortType.NONENCRYPTED,
                PortId.QOTD.getPortNumber(),
                PortId.QOTD_Debug.getPortNumber()
        );
        ports.add(port);
    }


    @Override
    protected void boot() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
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
                            new QuoteServerHandler() // Custom handler for finger requests
                        );
                    }
                });
            isRunning = true;

            // Bind and start to accept incoming connections.
            int port = ports.get(PortId.QOTD);
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    @Override
    protected void shutdown() {
        if (bossGroup != null && workerGroup != null) {
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
                isRunning = false;
                System.out.println("QOTD Server shut down successfully.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerQOTD server = new ServerQOTD();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.boot();
    }

    // Inner class to handle the quote requests without rate limiting
    private static class QuoteServerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
            String replyText = generateQuote();
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
