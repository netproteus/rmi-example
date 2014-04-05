// Copyright 2014 William Lewis
package com.netproteus.client;

import java.rmi.Naming;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.netproteus.rmi.RemoteServer;

public class Client {

    private static final Logger log = Logger.getLogger(Client.class);
    
    public static void main(String[] args) {
        
        BasicConfigurator.configure();
        
        // set java.security.policy if not defined
        if (System.getProperty("java.security.policy") == null) {
            System.setProperty("java.security.policy", "java.policy");
        }
        
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
        
        try {
            // set security manager
            System.setSecurityManager(new SecurityManager());            
            
            RemoteServer server = (RemoteServer) Naming.lookup("rmi://localhost/myserver");
            
            System.out.println(server.getMessage().getMessage());
        }
        catch (Throwable t) {
            log.error("Failed", t);
        }
        
    }
    
}
