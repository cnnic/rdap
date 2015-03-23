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
package org.restfulwhois.rdap.port43.server;

/**
 * Proxy43 server.Use netty.
 * @author jiashuo
 * 
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server.
 */
public final class Server {
    /**
     * boss thread pool size.
     */
    private static final int THREAD_POOL_SIZE_BOSS = 1;
    /**
     * server port.
     */
    private int port;
    /**
     * bossGroup.
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(
            THREAD_POOL_SIZE_BOSS);
    /**
     * workerGroup.
     */
    private EventLoopGroup workerGroup;

    /**
     * server initializer.
     */
    private ChannelInitializer serverInitializer;

    /**
     * server channelFuture.
     */
    private ChannelFuture serverChannelFuture;

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * constructor.
     * 
     * @param port
     *            port.
     * @param serverInitializer
     *            serverInitializer.
     */
    public Server(int port, ChannelInitializer serverInitializer) {
        super();
        this.port = port;
        this.serverInitializer = serverInitializer;
        workerGroup = new NioEventLoopGroup();
    }

    /**
     * constructor.
     * 
     * @param port
     *            port.
     * @param serverInitializer
     *            serverInitializer.
     * @param workerGroupSize
     *            workerGroupSize.
     */
    public Server(int port, ChannelInitializer serverInitializer,
            int workerGroupSize) {
        super();
        this.port = port;
        this.serverInitializer = serverInitializer;
        workerGroup = new NioEventLoopGroup(workerGroupSize);
    }

    /**
     * run server.
     * @return ChannelFuture.
     * @throws Exception
     *             Exception.
     */
    public ChannelFuture start() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(this.serverInitializer);
        serverChannelFuture = bootstrap.bind(port);
        return serverChannelFuture;
    }

    /**
     * shutdown server.
     */
    public void shutdown() {
        if (null != serverChannelFuture) {
            if (null != serverChannelFuture.channel()) {
                serverChannelFuture.channel().close();
            }
        }
        if (null != bossGroup) {
            bossGroup.shutdownGracefully();
        }
        if (null != workerGroup) {
            workerGroup.shutdownGracefully();
            workerGroup.terminationFuture();
        }
    }

}
