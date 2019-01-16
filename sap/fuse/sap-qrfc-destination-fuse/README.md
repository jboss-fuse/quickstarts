FUSE SAP Queued RFC Destination Endpoint Quick Start  
=======================================================================================================================
**Demonstrates the sap-qrfc-destination component running in a Fuse camel runtime.**  
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - Fuse Team  
Level: Intermediate  
Technologies: Fuse, SAP, Camel, Blueprint  
Summary: This quickstart demonstrates how to configure and use the sap-qrfc-destination component in a Fuse environment to invoke remote function modules and BAPI methods within SAP. This component invokes remote function modules and BAPI methods within SAP using the *Queued RFC* (qRFC) protocol.   
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Queued Remote Function Call Destination Camel component. This component extends the capabilities of the JBoss Fuse Transactional Remote Function Call Destination camel component by adding **IN-ORDER** delivery and processing guarantees to the delivery and processing of requests through its endpoints. This component and its endpoints should be used in cases where a series of requests depend on each other and must be delivered to and processed by the receiving SAP system in the same order that they were sent (**AT-MOST-ONCE** and **IN-ORDER**). The component accomplishes the AT-MOST-ONCE delivery guarantees using the same mechanisms as the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. The ordering guarantee is accomplished by serializing the requests in the order they are received by the SAP system to an *Inbound Queue*. Inbound queues are processed by the **QIN Scheduler** within SAP. When the inbound queue is *activated*, the QIN Scheduler which execute in order the queue's requests.   

This quick start contains a route with an initial timer endpoint which triggers and executes that route once. The route uses processor beans to build requests to the `CreateFromData` method of the `FlightCustomer` BAPI to create flight customer records in SAP. These requests are routed to `sap-qrfc-destination` endpoints which use the qRFC protocol to send these requests to SAP to invoke the BAPI method. The route logs to the console the serialized contents of the request messages it sends. These requests are serializes within SAP on the inbound queue `QUICKSTARTQUEUE`.  

**NOTE:** The qRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.    

In studying this quick start you will learn:

* How to configure the Fuse runtime environment in order to deploy the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component.
* How to define a Camel route containing the JBoss Fuse SAP Queued Remote Function Call Destination Camel component using the Blueprint XML syntax.
* How to use the JBoss Fuse SAP Queued Remote Function Call Destination Camel component to reliably update data in SAP. 
* How to configure connections used by the component.

For more information see:

* <https://access.redhat.com/documentation/en-us/red_hat_fuse/7.0/html-single/apache_camel_component_reference/#SAP> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/products/red-hat-fuse> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.1.1 or higher
* JDK 1.8
* A JBoss Fuse 7.0.0 container
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of your JBoss Fuse installation.  
2. In your JBoss Fuse installation, copy the `org.osgi.framework.system.packages.extra` property from the config properties file (`etc/config.properties`) to the custom properties file (`etc/custom.properties`) and append the following packages to the copied property:  

> org.osgi.framework.system.packages.extra = \  
>> ... \  
>> com.sap.conn.idoc, \  
>> com.sap.conn.idoc.jco, \   
>> com.sap.conn.jco, \   
>> com.sap.conn.jco.ext, \   
>> com.sap.conn.jco.monitor, \  
>> com.sap.conn.jco.rt, \   
>> com.sap.conn.jco.server  

3. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instance.  

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-qrfc-destination-fuse` directory.
* Run `mvn clean install` to build the quick start.
* In your JBoss Fuse installation directory run, `./bin/fuse` to start the JBoss Fuse runtime.
* In the JBoss Fuse console, run `install -s mvn:org.fusesource/camel-sap` to install the JBoss Fuse SAP Synchronous Remote Function Call Destination Camel component. Note the bundle number for the component bundle returned by this command.  
* In the JBoss Fuse console, run `install -s mvn:org.jboss.quickstarts.fuse/sap-qrfc-destination-fuse` to install the quick start. Note the bundle number for the quick start returned by this command.  
* In the JBoss Fuse console, run `log:tail` to monitor the JBoss Fuse log.
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
* Run `uninstall <quickstart-bundle-number>` providing the bundle number for the quick start bundle. 
* Run `uninstall <camel-sap-bundle-number>` providing the bundle number for the component bundle. 
* Run `shutdown -f` to shutdown the JBoss Fuse runtime.
