#!/bin/bash

mvn -U clean install dependency:build-classpath -Dmdep.outputFile=target/cp
