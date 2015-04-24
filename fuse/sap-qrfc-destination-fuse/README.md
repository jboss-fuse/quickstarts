FUSE SAP Queued RFC Destination Endpoint Quick Start  
=======================================================================================================================
**Demonstrates the sap-qrfc-destination component running in a Fuse camel runtime.**  
![Waldo](../../waldo.png "Waldo")

* * *
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-qrfc-destination component in a Fuse environment to invoke remote function modules and BAPI methods within SAP. This component invokes remote function modules and BAPI methods within SAP using the *Queued RFC* (tRFC) protocol.   
Target Product: Fuse  
Source: <http://github.com/punkhorn/sap-quickstarts/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Queued Remote Function Call Destination Camel component. This component extends the capabilities of the JBoss Fuse Transactional Remote Function Call Destination camel component by adding **IN-ORDER** delivery guarantees to the delivery of requests through its endpoints. This component and its endpoints should be used in cases where a series of requests depend on each other and must be delivered to the receiving SAP system **AT-MOST-ONCE** and **IN-ORDER**. The component accomplishes the AT-MOST-ONCE delivery guarantees using the same mechanisms as the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. The ordering guarantee is accomplished by serializing the requests in the order they are received by the SAP system to an *Inbound Queue*. Inbound queues are processed by the **QIN Scheduler** within SAP. When the inbound queue is *activated*, the QIN Scheduler which execute in order the queue's requests.   

**NOTE:** The qRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.    

This quick start uses XML files containing serialized SAP requests to create Customer records in the Flight Data Application within SAP. These files are consumed by the quickstart's route and their contents are then converted to string message bodies. These messages are then routed to an `sap-qrfc-destination` endpoint which converts and sends them to SAP as `BAPI_FLCUST_CREATEFROMDATA` requests to create Customer records. This endpoint uses the qRFC protocol to send these requests. These requests are serializes within SAP on the inbound queue `QUICKSTARTQUEUE`.  

In studying this quick start you will learn:

* How to define a Camel route containing the JBoss Fuse SAP Queued Remote Function Call Destination Camel component using the Blueprint XML syntax.
* How to use the JBoss Fuse SAP Queued Remote Function Call Destination Camel component to reliably update data in SAP. 
* How to configure connections used by the component.
* How to configure the Fuse runtime environment in order to deploy the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component.

For more information see:

* <https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.2/html/Apache_Camel_Component_Reference/SAP.html> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/site/documentation/JBoss_Fuse/> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.0.4 or higher
* JDK 1.7 or 1.8
* A JBoss Fuse 6.2 container not running with a Fabric
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of your JBoss Fuse installation.  
2. Edit the custom properties file (`etc/custom.properties`) of your JBoss Fuse installation and add the following packages to the `org.osgi.framework.system.packages.extra` property:  

> org.osgi.framework.system.packages.extra = \  
>...  
>> com.sap.conn.idoc, \  
>> com.sap.conn.idoc.jco, \   
>> com.sap.conn.jco, \   
>> com.sap.conn.jco.ext, \   
>> com.sap.conn.jco.monitor, \  
>> com.sap.conn.jco.rt, \   
>> com.sap.conn.jco.server  

3. Edit the project's Blueprint file (`src/main/resources/OSGI-INF/blueprint/camel-context.xml`) and modify the `quickstartDestinationData` bean to match the connection configuration for your SAP instance.  
4. Edit the project's idoc files (`src/data/request?.xml`) and enter the SID of your SAP in the location indicated.

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-qrfc-destination-fuse` directory.
* Run `mvn clean install` to build the quick start.
* In your JBoss Fuse installation directory run, `./bin/fuse` to start the JBoss Fuse runtime.
* In the JBoss Fuse console, run `osgi:install -s mvn:org.fusesource/camel-sap` to install the JBoss Fuse SAP Synchronous Remote Function Call Destination Camel component. Note the bundle number for the component bundle returned by this command.  
* In the JBoss Fuse console, run `osgi:install -s mvn:org.jboss.quickstarts.fuse/sap-qrfc-destination-fuse` to install the quick start. Note the bundle number for the quick start returned by this command.  
* In the JBoss Fuse console, run `log:tail` to monitor the JBoss Fuse log.
* Copy the idoc files (`src/data/request?.xml`) in the project to the input directory(`work/sap-qrfc-destination-fuse/input`) of the quick start route.
* In the JBoss Fuse console observe the request sent by the endpoint.
* Execute the queued requests waiting in the inbound queue `QUICKSTARTQUEUE`. Using the SAP GUI, run transaction `SMQ2`, the Inbound Queue qRFC Monitor:  
    a. Select the `QUICKSTARTQUEUE` queue.  
    b. Display the queue contents (Edit > Display Selection).  
    c. Select the entry for your Client connection and activate the queue (Edit > Activate).  
* Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
* Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping and Uninstalling the Quickstart
----------------------------------------

To uninstall the quick start and stop the JBoss Fuse run-time perform the following in the JBoss Fuse console:

1. Enter Ctrl-c to stop monitoring the JBoss Fuse log.
* Run `osgi:uninstall <quickstart-bundle-number>` providing the bundle number for the quick start bundle. 
* Run `osgi:uninstall <camel-sap-bundle-number>` providing the bundle number for the component bundle. 
* Run `osgi:shutdown -f` to shutdown the JBoss Fuse runtime.
