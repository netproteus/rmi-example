// Copyright 2014 William Lewis
package com.netproteus.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

public abstract class AbstractRmiServer extends UnicastRemoteObject implements Remote {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(AbstractRmiServer.class);
    
    private final String rmiUrl;
    
    protected AbstractRmiServer(String rmiUrl) throws RemoteException {
        super();
        this.rmiUrl = rmiUrl;
    }

    /**
     * @throws AlreadyBoundException 
     * @throws NotBoundException  
     */
    public void bind() throws MalformedURLException, AlreadyBoundException, NotBoundException, RemoteException {
        log.info("Trying to bind to " + rmiUrl);
        
        try {
            try {
                Naming.bind(rmiUrl, this);
            }
            catch (AlreadyBoundException abe) {
                Naming.rebind(rmiUrl, this);
            }
        }
        catch (ConnectException e) {
            
            if (rmiUrl.startsWith("rmi://localhost/") || rmiUrl.startsWith("//localhost/")) {
                log.info("Can't find RMIRegistry creating local one");
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                // And try to bind in again.
                try {
                    Naming.bind(rmiUrl, this);
                }
                catch (AlreadyBoundException abe) {
                    Naming.rebind(rmiUrl, this);
                }
            }
            else {
                throw e;
            }
        }
        
        log.info("Bound to " + rmiUrl);
    }
    
    public void unbind() {
        
        try {
            Naming.unbind(rmiUrl);
        }
        catch (Exception e) {
            log.error("Unbinding Error: " + e.getMessage(), e);
        }
        
        try {
            // To prevent problems with hanging DGC references inside RMI, unexport the object
            unexportObject(this, true);
        }
        catch (NoSuchObjectException nsoe) {
        }
    }    
    
}