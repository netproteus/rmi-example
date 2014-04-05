#!/bin/bash

cd client

if [ ! -f target/cp ]     
then
    echo "Run build.sh first"
    exit 1 
fi

java -cp $(cat target/cp):target/classes/ com.netproteus.client.Client
