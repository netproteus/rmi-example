RMI Example
===========

Requirements
------------

* maven Download [here](http://maven.apache.org/download.cgi)


Running an RMIRegistry
----------------------

If you are binding onto localhost then the Server will start an RMIRegistry automatically and you don't need to do anything. If you want to run the RMIRegistry on a different host or you want to run it yourself you should invoke it with the following option.

* rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false

See [here](http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/enhancements-7.html) for an explanation

Building
--------

* ./build.sh


Running Server
--------------

* ./runServer.sh


Running Client
--------------

* ./runClient.sh


Development
-----------

* mvn eclipse:eclipse
* import as existing project
