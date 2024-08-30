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

     private static final String[] PART_ONE = {
        "Decentralize your systems,", 
        "Encrypt your communications,", 
        "Store data on-chain,", 
        "Adopt zero-knowledge proofs,", 
        "Use end-to-end encryption,", 
        "Build with open-source tools,", 
        "Audit your smart contracts,", 
        "Use decentralized identity,", 
        "Control your private keys,", 
        "Implement decentralized governance,", 
        "Utilize multi-signature wallets,", 
        "Opt for peer-to-peer transactions,", 
        "Ensure data sovereignty,", 
        "Embrace distributed ledgers,", 
        "Prioritize user anonymity,", 
        "Adopt privacy-preserving technologies,", 
        "Invest in cryptographic research,", 
        "Trust the code, not intermediaries,", 
        "Enable permissionless innovation,", 
        "Focus on security-first development,"
    };

    private static final String[] PART_TWO = {
        "eliminating centralized points of failure.", 
        "protecting against surveillance.", 
        "ensuring data immutability.", 
        "providing privacy without trust.", 
        "securing your digital life.", 
        "for transparency and control.", 
        "preventing vulnerabilities.", 
        "for self-sovereign identities.", 
        "never share them.", 
        "to make decisions collectively.", 
        "for secure asset management.", 
        "cutting out the middlemen.", 
        "to retain control over your data.", 
        "for resilient record-keeping.", 
        "in every digital interaction.", 
        "to protect personal information.", 
        "for future-proof privacy.", 
        "not the gatekeepers.", 
        "empowering creators globally.", 
        "to safeguard against cyber threats."
    };

    private static final String[] PART_THREE = {
        "Decentralization isn't optional; it's necessary.", 
        "Your privacy is your freedom, guard it.", 
        "Control your data, or someone else will.", 
        "Cryptography is your shield in the digital age.", 
        "Centralization is a weakness, not a strength.", 
        "Trustlessness is the path to security.", 
        "Privacy is not a privilege; it's a right.", 
        "Decentralized systems are resilient by design.", 
        "Ownership is power; never give it away.", 
        "Without privacy, there is no freedom.", 
        "Security is not a product, it's a process.", 
        "Autonomy begins with decentralized tools.", 
        "Surveillance is a threat; encryption is the answer.", 
        "Digital rights are human rights.", 
        "In the digital world, privacy equals power.", 
        "A decentralized future is a secure future.", 
        "Take control of your digital destiny.", 
        "Empower yourself with technology, not corporations.", 
        "Decentralize or be compromised.", 
        "The future is encrypted, or it isn't free."
    };

    public static String generateQuote() {
        Random random = new Random();

        String part1 = PART_ONE[random.nextInt(PART_ONE.length)];
        String part2 = PART_TWO[random.nextInt(PART_TWO.length)];
        String part3 = PART_THREE[random.nextInt(PART_THREE.length)];

        // Generate a random number for the day
        int numberOfTheDay = random.nextInt(100) + 1; // Random number between 1 and 100

        return String.format("%s %s %s Your number for the day is: %d.", part1, part2, part3, numberOfTheDay);
    }
    
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
