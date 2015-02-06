sap-trfc-destination-standalone: Demonstrates the sap-trfc-destination component running in a standalone camel runtime.
=======================================================================================================================
* * *
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-trfc-destination component. This component invokes remote function modules and BAPI methods within SAP using the *Transactional RFC* (tRFC) wire protocol.   
Target Product: Fuse  
Source: <http://github.com/punkhorn/sap-quickstarts/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component.   

This quick start uses XML files containing serialized SAP requests to create Customer records in the Flight Data Application within SAP. These files are consumed by the quickstart's route and their contents are then converted to string message bodies. These messages are then routed to an `sap-srfc-destination` endpoint which converts and sends them to SAP as `BAPI_FLCUST_CREATEFROMDATA` requests to create Customer records.  

In studying this quick start you will learn:

* How to define a Camel route containing the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component using the Spring XML syntax.
* How to use the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component to reliably update data in SAP. 
* How to configure connections used by the component.

For more information see:

* <https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.2/html/Apache_Camel_Component_Reference/SAP.html> for more information about the JBoss Fuse SAP Synchronous Remote Function Call Camel components 
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
* Edit the project's Spring file (`src/main/resources/spring/camel-context.xml`) and modify the `quickstartDestinationData` bean to match the connection configuration for your SAP instance. 
* Edit the project's IDoc files (`src/data/idoc?.xml`) and enter the SID of your SAP in the location indicated.

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-trfc-destination-standalone` directory.
* Run `mvn clean install` to build the quick start.
* Run `mvn camel:run` to start the Camel runtime.
* In the console observe the contents of the IDoc processed by the route.
* Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
* Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping the Quickstart
-----------------------

To stop the camel run-time:

1. Enter Ctrl-c in the console.

