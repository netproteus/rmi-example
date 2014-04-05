// Copyright 2014 William Lewis
package com.netproteus.client;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.rmi.Naming;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.netproteus.rmi.RemoteServer;

public class Client {

    private static final Logger log = Logger.getLogger(Client.class);
    
    public static void main(String[] args) {
        updateDefaultCharset();
        
        BasicConfigurator.configure();
        
        // set java.security.policy if not defined
        if (System.getProperty("java.security.policy") == null) {
            System.setProperty("java.security.policy", "java.policy");
        }
        
        // http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/enhancements-7.html
        // Oracle call this an enhancement, it's completely stupid if you ask me
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
