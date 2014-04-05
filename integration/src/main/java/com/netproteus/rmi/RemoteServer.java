// Copyright 2014 William Lewis
package com.netproteus.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.netproteus.data.RemoteMessage;

public interface RemoteServer extends Remote { 

    public RemoteMessage getMessage() throws RemoteException;
}
