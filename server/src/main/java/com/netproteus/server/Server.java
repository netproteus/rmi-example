// Copyright 2014 William Lewis
package com.netproteus.server;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.rmi.RemoteException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.netproteus.data.Message;
import com.netproteus.data.RemoteMessage;
import com.netproteus.rmi.AbstractRmiServer;
import com.netproteus.rmi.RemoteServer;
import com.netproteus.rmi.RmiClassServer;

public class Server extends AbstractRmiServer implements RemoteServer {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(Server.class);
    
    public Server() throws RemoteException {
        super("rmi://localhost/myserver");
    }

    @Override
    public RemoteMessage getMessage() {
        return new Message();
    }
    
    public static void main(String [] args) {
        updateDefaultCharset();
        
        BasicConfigurator.configure();
        
        // set java.security.policy if not defined
        if (System.getProperty("java.security.policy") == null) {
            System.setProperty("java.security.policy", "java.policy");
        }
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
        
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
    
    private static void updateDefaultCharset() {
        try {
            System.setProperty("file.encoding", "UTF-8");
            Class<Charset> c = Charset.class;
            Field defaultCharsetField = c.getDeclaredField("defaultCharset");
            defaultCharsetField.setAccessible(true);
            defaultCharsetField.set(null, Charset.forName("UTF-8"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }      
    
}
