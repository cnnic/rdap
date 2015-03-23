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
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.port43.service.ClearRateLimitMapTimer;
import org.restfulwhois.rdap.port43.util.RdapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server.
 */
public final class Main {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    /**
     * service port.
     */
    private static final int SERVICE_PORT = RdapProperties.getServicePort();
    /**
     * manage port.
     */
    private static final int MANAGE_PORT = RdapProperties.getManagePort();

    /**
     * all servers.
     */
    public static List<Server> allServer = new ArrayList<Server>();

    /**
     * main method.
     * 
     * @param args
     *            args.
     * @throws Exception
     *             Exception.
     */
    public static void main(String[] args) throws Exception {
        if (null == args || args.length == 0) {
            LOGGER.error("wrong parameter! valid parameter:start shutdown");
            return;
        }
        String command = args[0];
        Server server = null;
        LOGGER.info("command:{}", command);
        if (ServiceHandler.CMD_START.equals(command)) {
            server = startServer();
        } else if (ManageServerHandler.CMD_SHUTDOWN.equals(command)) {
            shutdownServer(server);
        } else {
            LOGGER.error("wrong parameter:{}! valid parameter:start, shutdown",
                    command);
        }
    }

    /**
     * shutdown server.
     * 
     * @param server
     *            server.
     * @throws InterruptedException
     *             InterruptedException.
     */
    private static void shutdownServer(Server server)
            throws InterruptedException {
        LOGGER.info("shutdownServer");
        ShutdownServer.shutdown();
    }

    /**
     * start server.
     * 
     * @return server server.
     * @throws Exception
     *             Exception.
     */
    private static Server startServer() throws Exception {
        ClearRateLimitMapTimer.schedule();
        Server serviceServer =
                new Server(SERVICE_PORT, new ServiceServerInitializer());
        serviceServer.start();
        allServer.add(serviceServer);
        Server manageServer =
                new Server(MANAGE_PORT, new ManageServerInitializer(), 1);
        manageServer.start();
        allServer.add(manageServer);
        return serviceServer;
    }

    /**
     * do shutdown.
     */
    public static void doShutdown() {
        LOGGER.info("doShutdown begin...");
        for (Server server : allServer) {
            LOGGER.info("shutdown server {}", server);
            server.shutdown();
        }
        LOGGER.info("shutdown rate limit IP map clear timer...");
        ClearRateLimitMapTimer.stop();
        LOGGER.info("doShutdown end.");
    }

}
