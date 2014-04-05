// Copyright 2014 William Lewis
package com.netproteus;

import java.rmi.RemoteException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.netproteus.rmi.AbstractRmiServer;
import com.netproteus.rmi.RemoteServer;
import com.netproteus.rmi.RmiClassServer;

public class Server extends AbstractRmiServer implements RemoteServer {

    private static final Logger log = Logger.getLogger(Server.class);
    
    public Server() throws RemoteException {
        super("rmi://localhost/myserver");
    }

    @Override
    public String hello() {
        return "World";
    }
    
    public static void main(String [] args) {
        
        BasicConfigurator.configure();
        
        // set java.security.policy if not defined
        if (System.getProperty("java.security.policy") == null) {
            System.setProperty("java.security.policy", "java.policy");
        }
        
        // set file encoding
        System.setProperty("file.encoding", "UTF-8");
        
        try {
            // set security manager
            System.setSecurityManager(new SecurityManager());
            
            RmiClassServer rmiClassServer = new RmiClassServer();
            rmiClassServer.start();
            
            Server server = new Server();
            server.bind();
        }
        catch (Throwable t) {
            log.error("Failed to start server", t);
        }
    
    }
    
}
