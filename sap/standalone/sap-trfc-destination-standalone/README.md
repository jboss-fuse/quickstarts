Standalone SAP Transactional RFC Destination Endpoint Quick Start
=======================================================================================================================
**Demonstrates the sap-trfc-destination component running in a standalone camel runtime.**   
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-trfc-destination component in a standalone Camel environment to invoke remote function modules and BAPI methods in SAP. This component invokes remote function modules and BAPI methods within SAP using the *Transactional RFC* (tRFC) protocol.   
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. This component and its endpoints should be used in cases where requests must be delivered to the receiving SAP system **AT-MOST-ONCE**. To accomplish this the component generates a *transcation ID* (*tid*) which accompanies every request sent through the component in a route's exchange. The receiving SAP system records the tid accompanying a request before delivering the request; if the SAP system receives the request again with the same tid it will not deliver the request. Thus if a route encounters a communication error when sending a request through an endpoint of this component it can retry sending the request within the same exchange knowing it will be delivered and executed only once.   

This quick start contains a route with an initial timer endpoint which triggers and executes that route once. The route uses processor beans to build requests to the `CreateFromData` method of the `FlightCustomer` BAPI to create flight customer records in SAP. These requests are routed to `sap-trfc-destination` endpoints which use the tRFC protocol to send these requests to SAP to invoke the BAPI method. The route logs to the console the serialized contents of the request messages it sends.  

**NOTE:** The tRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.  

**NOTE:** This component does not guarantee that a series of requests sent through its endpoints are delivered and processed in the receiving SAP system in the same order that they were sent. The delivery and processing order of these requests may differ on the receiving SAP system due to communication errors and resends of a request. To guarantee the delivery and processing order of a series of requests please see the JBoss Fuse SAP Queued Remote Function Call Destination Camel component.     

In studying this quick start you will learn:

* How to configure the Camel runtime environment in order to deploy the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. 
* How to define a Camel route containing the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component using the Spring XML syntax.
* How to use the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component to reliably update data in SAP. 
* How to configure connections used by the component.

For more information see:

* <https://access.redhat.com/documentation/en-us/red_hat_fuse/7.0/html-single/apache_camel_component_reference/#SAP> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/products/red-hat-fuse> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.1.1 or higher
* JDK 1.8
* JBoss Fuse 7.0.0
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of the project.
2. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instance.  

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-trfc-destination-standalone` directory.
* Run `mvn clean install` to build the quick start.
* Run `mvn camel:run` to start the Camel runtime.
* In the console observe the contents of the requests processed by the route.
* Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
* Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping the Quickstart
-----------------------

To stop the camel run-time:

1. Enter Ctrl-c in the console.

