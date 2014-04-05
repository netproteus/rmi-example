// $Id$
// Copyright 2014 William Lewis
package com.netproteus.data;

import java.io.Serializable;


public class Message implements RemoteMessage, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Hello World";
    }

}
