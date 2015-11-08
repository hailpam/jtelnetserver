JTelnetServer
=============

Portable Telnet server. It implements a subset of functionalities normally
available on OS shells. For example, the command already implemented are:
- ls
- mkdir
- cd
- rm
- more
- touch
- status
- history
- help
- quit
- pwd


How to deploy/start the server
==============================

From the project directory, simply peform the following entry:

 > mvn exec:java

The server will be binded on localhost:20000 if no issues are encountered in
opening the server configuration file; otherwise, a default binding on 
localhost:10000 will be deployed.


Test using Telnet
=================

Run the sever as above explained. On the command prompt, digit

 > telnet localhost 20000[10000]

the port is alterative and it depends from the bind (read above).


How to rebuild from scratch
===========================

Implement the following steps:
1. clean the current build

 > mvn clean

2. compile the project

 > mvn compile

3. test the release

 > mvn test

4. install the jar in the repo

 > mvn install

Of course, the above steps can be collapsed in only one.


Alternative way to deploy/start the server
==========================================

Once the above rebuilding has been accomplished, it is possible to perform the
following entry on the command prompt:
 
 > java -jar target/jtelnetserver-1.0.jar target/classes server.properties

this will start the connector thread as done by maven exec. Alternatively,

 > java -jar target/jtelnetserver-1.1.jar

the server will be started using the default configuration. 


NOTE on Configuration (be careful)
==================================

In order to let the server work properly, the server.properties should be
modified accordingly. For example, the server.home it's different between
a Linux machine and a Windows one, so it's needed to change di property
accordingly (an example is already present in the configuration file).
Otherwise, by inhibiting the arguments in Maven exec plugin, the configuration
manager will resolve the server.home as the system user.home.

Anyway, in order to change the settings, before to build build the server
the following setting should be done (according to the environment).

 # Linux version
 #server.home = /home/hailpam
 # Windows version
 server.home = D:\\data

The file is stored under

 jtelnetserver/src/main/resources


Example
=======

 > mvn exec:java

```
 Starting the Connector Thread...
 Nov 08, 2015 9:43:01 AM it.pm.jtelnetserver.thread.ConnectorThread run
 INFO: 
 +--Server Info
     OS :: Linux, architecture :: amd64, version:: 3.10.0-229.14.1.el7.x86_64
     home :: /home/hailpam
     sessions # ::      0
 +--
 Nov 08, 2015 9:43:01 AM it.pm.jtelnetserver.thread.ConnectorThread run
 INFO: Server is Running
```

 > telnet localhost 20000
 prompt> ls

```
 total 13
 d   r w e 0        13 Jul   settings                                                              
 d   r w e 0        12 Jul   environments                                                          
 d   r w e 4        12 Oct   tools                                                                 
 d   r w e 0        26 Feb   libs                                                                  
 d   r w e 0        26 Feb   archive                                                               
 d   r w e 0        26 Feb   deliverables                                                          
 d   r w e 0        26 Feb   data                                                                  
 d   r w e 4        02 Nov   scripts                                                               
 d   r w e 0        13 Jul   servers                                                               
 d   r w e 0        26 Feb   brokers                                                               
 f   r w - 1        04 Jun   cli-usage                                                             
 d   r w e 4        10 Oct   workspaces                                                            
 d   r w e 0        13 Jul   runtimes
 ```

 prompt> status

```
 session status: 
  is active :: true
  server command(s) #:: 12
  service failure(s) #:: 1
  uptime :: 329517ms
```

Versions
========

- 1.0 - first relesae
- 1.1 - second release: fixed some issues with Windows display (line 
        separator incompatibility)




