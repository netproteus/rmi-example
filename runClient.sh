#!/bin/bash

cd client

mvn dependency:build-classpath -Dmdep.outputFile=target/cp
java -cp $(cat target/cp):target/classes/ com.netproteus.rmi.RmiClient
