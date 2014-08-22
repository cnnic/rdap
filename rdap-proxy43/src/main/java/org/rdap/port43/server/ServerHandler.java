package org.rdap.port43.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.rdap.port43.service.ProxyService;

/**
 * 
 * @author jiashuo
 *
 */
/**
 * Handles a server-side channel.
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
//        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName()
//                + "!\r\n");
//        ctx.write("It is " + new Date() + " now.\r\n");
//        ctx.flush();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String request) {
        // Generate and write a response.
        String response;
        boolean close = false;
        if (request.isEmpty()) {
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '" + request + "'?\r\n";
            ProxyService proxyService = ProxyService.getInstance();
            String s = proxyService.execute(request);
            response = s;
        }

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the
        // conversion.
        ChannelFuture future = ctx.write(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        future.addListener(ChannelFutureListener.CLOSE);
        if (close) {
        }
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