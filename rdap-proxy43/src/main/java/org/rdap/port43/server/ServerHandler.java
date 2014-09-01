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

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.service.ConnectionControlService;
import org.rdap.port43.service.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server handler.
 * @author jiashuo
 *
 */
/**
 * Handles a server-side channel.
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * rate limit msg.
     */
    private static final String ERROR_MSG_RATE_LIMIT =
            "Exceed rate limit, please try some seconds later.";
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String request) {
        InetSocketAddress socketAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteAddr = socketAddress.getAddress().getHostAddress();
        LOGGER.info("clientAddress:{}", remoteAddr);
        String response = StringUtils.EMPTY;
        if (ConnectionControlService.exceedRateLimit(remoteAddr)) {
            LOGGER.debug("exceedRateLimit,return 429 error.");
            response = get429Response();
            writeResponseAndcloseConnection(ctx, response);
            return;
        }
        if (request.isEmpty()) {
            response = "command can't be empty.";
        } else {
            ProxyService proxyService = ProxyService.getInstance();
            try {
                response = proxyService.execute(request);
            } catch (Exception e) {
                LOGGER.error("internal server error:{}", e);
                response = "internal server error.";
            }
        }
        writeResponseAndcloseConnection(ctx, response);
    }

    /**
     * get 429 error response.
     * 
     * @return string.
     */
    private String get429Response() {
        return ERROR_MSG_RATE_LIMIT;
    }

    /**
     * close connection.
     * 
     * @param ctx
     *            ctx.
     * @param response
     *            response.
     */
    private void writeResponseAndcloseConnection(ChannelHandlerContext ctx,
            String response) {
        ChannelFuture future = ctx.write(response);
        // Close the connection.
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
