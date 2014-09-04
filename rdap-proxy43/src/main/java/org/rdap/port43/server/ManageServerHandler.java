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

/**
 * manage server handler.
 * @author jiashuo
 * 
 */
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server.
 */
@Sharable
public final class ManageServerHandler extends
        SimpleChannelInboundHandler<String> {
    /**
     * shutdown command.
     */
    public static final String CMD_SHUTDOWN = "shutdown";
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ManageServerHandler.class);

    /**
     * constructor.
     * 
     */
    public ManageServerHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg)
            throws Exception {
        LOGGER.info("receive manage cmd:{}", msg);
        if (StringUtils.isBlank(msg) || !StringUtils.equals(CMD_SHUTDOWN, msg)) {
            ChannelFuture future = ctx.writeAndFlush("wrong command:" + msg);
            future.addListener(ChannelFutureListener.CLOSE).channel().close();
            return;
        }
        ChannelFuture future = ctx.writeAndFlush("shutdown server successful.");
        ctx.writeAndFlush(ManageServerInitializer.LINE_DELIMITER);
        LOGGER.info("do shutdown server ...");
        Main.doShutdown();
        LOGGER.info("do shutdown server end.");
        future.addListener(ChannelFutureListener.CLOSE).channel().close();
        LOGGER.info("shutdown manage server end.");
    }

}
