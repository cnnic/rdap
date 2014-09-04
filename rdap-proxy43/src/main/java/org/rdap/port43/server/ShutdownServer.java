/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.rdap.port43.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.rdap.port43.util.RdapProperties;

/**
 * shutdown server handler.
 * 
 * @author jiashuo
 */
public final class ShutdownServer {
    /**
     * host.
     */
    private static final String HOST = System.getProperty("host", "127.0.0.1");
    /**
     * port.
     */
    private static final int MANAGE_PORT = RdapProperties.getManagePort();

    /**
     * event group.
     */
    private static final EventLoopGroup EVENT_GROUP = new NioEventLoopGroup();

    /**
     * channelFuture.
     */
    private static ChannelFuture channelFuture = null;

    /**
     * shutdown.
     * 
     * @throws InterruptedException
     *             InterruptedException.
     */
    public static void shutdown() throws InterruptedException {
        Bootstrap boot = new Bootstrap();
        boot.group(EVENT_GROUP).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(ManageServerInitializer.DELIMITER_BASED_FRAME_DECODER);
                        p.addLast(ManageServerInitializer.DECODER);
                        p.addLast(ManageServerInitializer.ENCODER);
                        p.addLast(new ShutdownClientHandler());
                    }
                });
        channelFuture = boot.connect(HOST, MANAGE_PORT).sync();
    }

    /**
     * shutdown me.
     */
    public static void shutdownMe() {
        if (null != EVENT_GROUP) {
            EVENT_GROUP.shutdownGracefully();
        }
        if (null != channelFuture) {
            if (null != channelFuture.channel()) {
                channelFuture.channel().closeFuture();
            }
        }
    }

}
