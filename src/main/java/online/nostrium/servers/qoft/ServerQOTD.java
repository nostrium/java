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
import java.util.Random;
import static online.nostrium.servers.qoft.QOTD.generateQuote;

/**
 * 
 * To test, use this:
 *  
 *      finger brito@nostrium.online
 * 
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class ServerQOTD extends Server {

     
    @Override
    public String getId() {
        return "Server_QOTD";
    }

    @Override
    public int getPort() {
        if (core.config.debug) {
            return core.config.portQOTD_Debug;
        } else {
            return core.config.portQOTD     ;
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
                            new QuoteServerHandler() // Custom handler for finger requests
                        );
                    }
                });
            isRunning = true;

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


    public static void main(String[] args) {
        new ServerQOTD().boot();
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
