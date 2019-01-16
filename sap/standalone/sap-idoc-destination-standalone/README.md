Standalone SAP IDoc Destination Endpoint Quick Start
===========================================
**Demonstrates the sap-idoc-destination component running in a standalone camel runtime.**   
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * * 
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-idoc-destination component in a standalone Camel environment to send Intermediate Documents (IDocs) to SAP. This component sends IDoc documents to SAP using the *Transactional RFC* (tRFC) protocol.  
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP IDoc Destination Camel component. This component and its endpoints should be used in cases where a camel route is required to send an Intermediate document (IDoc) to an SAP system.  

This quick start contains a route with an initial timer endpoint which triggers and executes that route once. The route uses processor beans to build `FLCUSTOMER_CREATEFROMDATA01` type IDoc documents to create Customer records in SAP. These documents are routed to `sap-idoc-destination` endpoints which use the tRFC protocol to send these documents to the ALE subsystem in SAP which creates the Customer records. The route logs to the console the serialized contents of each document it sends.  

**NOTE:** This component does not guarantee that a series of IDocs sent through its endpoints are delivered and processed in the receiving SAP system in the same order that they were sent. The delivery and processing order of these documents may differ on the receiving SAP system due to communication errors and resends of a document. To guarantee the delivery and processing order of a series of IDocs please see the JBoss Fuse SAP Queued IDoc Destination Camel component.     

In studying this quick start you will learn:

* How to configure the Camel runtime environment in order to deploy the JBoss Fuse SAP IDoc Destination Camel component. 
* How to define a Camel route containing the JBoss Fuse SAP IDoc Destination Camel component using the Spring XML syntax.
* How to use the JBoss Fuse SAP IDoc Destination Camel component to send IDocs to SAP. 
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

Configuring the ALE Subsystem
-----------------------------

To send IDocs from the quick start's route to your SAP system, you must first configure the Application Linking Enabling (ALE) subsystem in your SAP system:

1. Using the SAP GUI, run transaction `SALE`, the ALE Implementation Guide.
2. Ensure that Logical Systems for the quick start and your SAP client have been defined:  
    a. Run the `Define Logical System` step (Basic Systems > Logical Systems > Define Logical System).  
    b. Click `New Entries` and create and save the following logical systems:    
    
        | Log.System | System     |   
        | ---------- | ---------- |     
        | QUICKSTART | QUICKSTART |      
        | QUICKCLNT  | QUICKCLNT  |

	c. Return to the `SALE`  transaction main screen (Goto > Back).
3. Ensure the `QUICKCLNT` logical system has been assigned to you SAP client:  
    a. Run the `Assign Logical System to Client` step (Basic Settings > Logical Systems > Assign Logical System to Client).   
    b. Select the `QUICKCLNT` for your client's `Logical system` and save your changes.     
    c. Return to the `SALE`  transaction main screen (Goto > Back).  
4.  Ensure the destination `QUICKSTART` has been defined:   
    a. Run the `Create RFC Connections` step (Communication > Create RFC Connections).    
    b. Create a new destination (Edit > Create):  
		1. **RFC Destination** : `QUICKSTART`.    
        2. **Connection Type** : `T`.    
        3. **Technical Settings** :    
            i. **Activation Type** : `Registered Server Program`.    
            ii.**Program ID** : `QUICKSTART`.   
        4. **Unicode**:   
        	i. **Communication Type with Target System** : `Unicode`   
	c. Return to the `SALE` transaction main screen (Goto > Back).   
5. Ensure that a Model View for the message flow from the quick start to your SAP system has been defined:  
   a. Run the `Maintain Distribution Model and Distribute Views` step (Modelling and Implementing Business Processes > Maintain Distribution Model and Distribute Views).   
   b. Ensure a `QUICKSTART` model view has been created with the technical name `QUICKSTART` (Edit > Model view > Create).   
   c. Ensure the `QUICKSTART` model view has a BAPI call configured (Edit > Add BAPI):   
      1. **Sender/client** : `QUICKSTART`.  
      2. **Reciever/server** : `QUICKCLNT`.  
      3. **Obj.name/interface** : `FlightCustomer`.  
      4. **Method** : `CreateFromData`.   
   d. Ensure `Partner Profiles` have been generated for the quick start and your SAP client (Environment > Generate Partner Profiles).   
 
Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of the project.
2. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instance.  

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-idoc-destination-standalone` directory.
2. Run `mvn clean install` to build the quick start.
3. Run `mvn camel:run` to start the Camel runtime.
4. In the console observe the contents of the IDoc processed by the route.
5. Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
6. Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping the Quickstart
-----------------------

To stop the camel run-time:

1. Enter Ctrl-c in the console.

