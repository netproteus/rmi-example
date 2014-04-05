// Copyright 2014 William Lewis
package com.netproteus.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote { 

    public String hello() throws RemoteException;
}
