#!/bin/bash

mvn clean install

cd server

mvn dependency:build-classpath -Dmdep.outputFile=target/cp
java -cp $(cat target/cp):target/classes/ com.netproteus.Server


