// Copyright 2014 William Lewis
package com.netproteus.rmi;

import java.rmi.Naming;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class RmiClient {

    private static final Logger log = Logger.getLogger(RmiClient.class);
    
    public static void main(String[] args) {
        
        BasicConfigurator.configure();
        try {
            RemoteServer server = (RemoteServer) Naming.lookup("rmi://localhost/myserver");
            
            System.out.println(server.hello());
        }
        catch (Throwable t) {
            log.error("Failed", t);
        }
        
    }
    
}
