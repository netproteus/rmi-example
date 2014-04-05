// Copyright 2014 William Lewis
package com.netproteus.rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.netproteus.util.BasicHttpServer;

public class RmiClassServer {

    private static final Logger log = Logger.getLogger(RmiClassServer.class);

    private static final long initializationTime = System.currentTimeMillis();

    private BasicHttpServer server;

    private final String hostname;
    private final String hrefBase;
    
    public RmiClassServer() {
        this(getDefaultLocalHostName(),0);
    }

    public RmiClassServer(String hostname, int port) {
        this.hostname = hostname;
        this.hrefBase = "/" + initializationTime;
        this.server = new BasicHttpServer(hostname, port, hrefBase, new RmiClassServlet());
    }    
    
    private static String getDefaultLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void start() {
        
        log.debug("Starting");
        synchronized (this.server) {
            this.server.start();
            try {
                this.server.wait();
            }
            catch (InterruptedException e) {
                log.error("Interrupted ObjectHttpServer Startup", e);
            }
        }
        
        log.info("Setting java.rmi.server.codebase to " + getUrl());
        System.setProperty("java.rmi.server.codebase", getUrl());        
    }
    
    public void stop() {
        this.server.stopServer();
    }    
    
    protected String getUrl() {
        // n.b. the trailing slash is very important because otherwise it thinks you are serving a jar
        return "http://" + hostname + ":" + server.getPort() + hrefBase + "/";
    }
    
}
