// Copyright 2014 William Lewis
package com.netproteus.util;

import javax.servlet.Servlet;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Basic embedded invocation of a jetty webserver that takes a single servlet to use to handle all requests
 * 
 * @author <a href="mailto:william@netproteus.net">william</a>
 */
public class BasicHttpServer extends Thread {

    private static final Logger log = Logger.getLogger(BasicHttpServer.class);
    private static final String ROLE = "role";

    private final Servlet servlet;
    private final String username;
    private final String pass;
    private final String hostname;

    private int port;
    private boolean daemonize = true;
    private Server server;
    private final String base;
    
    public BasicHttpServer(String bindAddress, int port, String username, String pass, String base, Servlet servlet) {
        this.port = port;
        this.hostname = bindAddress;
        this.username = username;
        this.pass = pass;
        this.base = base;
        this.servlet = servlet;
    }
    
    public BasicHttpServer(String bindAddress, int port, String base, Servlet servlet) {
        this(bindAddress, port, null, null, base, servlet);
    }    

    @Override
    public synchronized void start() {
        this.setDaemon(daemonize);
        super.start();
    }
    
    public void stopServer() {
        if (server != null) {
            try {
                server.stop();
            }
            catch (Throwable t) {
                log.error("Failed to Stop Webserver", t);
            }
        }
    }    
    
    @Override
    public void run() {
        try {
            synchronized (this) {
                
                server = new Server();
                
                Connector connector=new SocketConnector();
                connector.setPort(port);
                connector.setHost(hostname == null ? "127.0.0.1" : hostname);
                server.setConnectors(new Connector[]{connector});            
                
                Context root = new Context(server,(base == null || base.isEmpty() ? "/" : base) ,Context.SESSIONS);
                root.addServlet(new ServletHolder(servlet), "/*");
                
                if (username != null && !username.isEmpty()) {
                    Constraint constraint = new Constraint();
                    constraint.setName(Constraint.__BASIC_AUTH);;
                    constraint.setRoles(new String[]{ROLE});
                    constraint.setAuthenticate(true);
        
                    ConstraintMapping cm = new ConstraintMapping();
                    cm.setConstraint(constraint);
                    cm.setPathSpec("/*");            
                    
                    SecurityHandler sh = new SecurityHandler();
                    HashUserRealm hashUserRealm = new HashUserRealm();
                    hashUserRealm.put(username, pass == null ? "" : pass);
                    hashUserRealm.addUserToRole(username, ROLE);
                    sh.setUserRealm(hashUserRealm);
                    sh.setConstraintMappings(new ConstraintMapping[]{cm});            
                    
                    root.addHandler(sh);
                }
                
                server.start();
                this.port = server.getConnectors()[0].getLocalPort();
                
                this.notify();
            }
            server.join();
        }
        catch (Throwable t) {
            log.error("Failed to start Object Http Server", t);
        }
    }

    public void setDaemonize(boolean daemonize) {
        this.daemonize = daemonize;
    }

    public int getPort() {
        return port;
    }

}