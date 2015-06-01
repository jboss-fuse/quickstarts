Standalone SAP Queued RFC Destination Endpoint Quick Start
==========================================================
**Demonstrates the sap-qrfc-destination component running in a standalone camel runtime.**   
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")
   
* * *
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-qrfc-destination component. This component invokes remote function modules and BAPI methods within SAP using the *Queued RFC* (qRFC) protocol.   
Target Product: Fuse  
Source: <http://github.com/punkhorn/sap-quickstarts/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Queued Remote Function Call Destination Camel component. This component extends the capabilities of the JBoss Fuse Transactional Remote Function Call Destination camel component by adding **IN-ORDER** delivery guarantees to the delivery of requests through its endpoints. This component and its endpoints should be used in cases where a series of requests depend on each other and must be delivered to the receiving SAP system **AT-MOST-ONCE** and **IN-ORDER**. The component accomplishes the AT-MOST-ONCE delivery guarantees using the same mechanisms as the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. The ordering guarantee is accomplished by serializing the requests in the order they are received by the SAP system to an *Inbound Queue*. Inbound queues are processed by the **QIN Scheduler** within SAP. When the inbound queue is *activated*, the QIN Scheduler which execute in order the queue's requests.   

**NOTE:** The qRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.    

This quick start uses XML files containing serialized SAP requests to create Customer records in the Flight Data Application within SAP. These files are consumed by the quickstart's route and their contents are then converted to string message bodies. These messages are then routed to an `sap-qrfc-destination` endpoint which converts and sends them to SAP as `BAPI_FLCUST_CREATEFROMDATA` requests to create Customer records. This endpoint uses the qRFC protocol to send these requests. These requests are serializes within SAP on the inbound queue `QUICKSTARTQUEUE`.  

In studying this quick start you will learn:

* How to define a Camel route containing the JBoss Fuse SAP Queued Remote Function Call Destination Camel component using the Spring XML syntax.
* How to use the JBoss Fuse SAP Queued Remote Function Call Destination Camel component to reliably update data in SAP. 
* How requests sent through the component are processed by SAP.
* How to configure connections used by the component.

For more information see:

* <https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.2/html/Apache_Camel_Component_Reference/SAP.html> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/site/documentation/JBoss_Fuse/> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.0.4 or higher
* JDK 1.7 or 1.8
* JBoss Fuse 6.2
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of the project.
* Edit the project's Spring file (`src/main/resources/META-INF/spring/camel-context.xml`) and modify the `quickstartDestinationData` bean to match the connection configuration for your SAP instance. 
* Edit the project's request files (`src/data/request?.xml`) and enter the SID of your SAP in the location indicated.

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-qrfc-destination-standalone` directory.
* Run `mvn clean install` to build the quick start.
* Run `mvn camel:run` to start the Camel runtime.
* In the console observe the contents of the IDoc processed by the route.
* Execute the queued requests waiting in the inbound queue `QUICKSTARTQUEUE`. Using the SAP GUI, run transaction `SMQ2`, the Inbound Queue qRFC Monitor:  
    a. Select the `QUICKSTARTQUEUE` queue.  
    b. Display the queue contents (Edit > Display Selection).  
    c. Select the entry for your Client connection and activate the queue (Edit > Activate).  
* Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
* Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping the Quickstart
-----------------------

To stop the camel run-time:

1. Enter Ctrl-c in the console.

