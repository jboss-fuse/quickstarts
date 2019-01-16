Spring Boot SAP IDoc List Server Endpoint Quick Start   
====================================================   
**Demonstrates the sap-idoclist-server component running in a spring boot camel runtime.**   
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - Fuse Team  
Level: Beginner  
Technologies: SAP, Camel, Spring  
Summary: This quickstart demonstrates how to configure and use the sap-idoclist-server component in a Spring Boot Camel environment to handle Intermediate Document (IDoc) lists sent from SAP. This component handles document lists from SAP using the *Transactional RFC* (tRFC) protocol.       
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

* * *  

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP IDoc List Server Camel component. This component and its endpoints should be used in cases where a sending SAP system requires delivery of Intermediate Document lists to a Camel route. This component uses the tRFC protocol to communicate with SAP as described in the `sap-trfc-server-standalone` quick start.

This quick start handles lists of `FLCUSTOMER_CREATEFROMDATA01` type IDoc documents from SAP to create Customer records in the Flight Data Application. The route of this quick start simply mocks the processing of these documents by logging the documents it receives. The `sap-idoclist-server` endpoint at the beginning of the route consumes a list of IDoc documents from SAP and its contents is placed into the message body of the exchange's message. The document list is then logged to the console.

**NOTE:** This component does not guarantee that a series of IDoc lists sent through its endpoints are handled and processed in the receiving Camel route in the same order that they were sent. The delivery and processing order of these lists may differ on the receiving Camel route due to communication errors and resends of a document list. This component only guarantees that each idoc list is processed **AT-MOST-ONCE**. To guarantee the delivery **and** processing order of a series of IDoc lists from SAP it is incumbent upon the sending SAP system to serialize its document lists to an **outbound queue** when sending them to this component to achieve **IN-ORDER** delivery and processing guarantees.  

In studying this quick start you will learn:

* How to configure the Camel runtime environment in order to deploy the JBoss Fuse SAP IDoc List Server Camel component. 
* How to define a Camel route containing the JBoss Fuse SAP IDoc List Server Camel component using the Spring XML syntax.
* How to use the JBoss Fuse SAP IDoc List Server Camel component to receive IDoc lists from SAP. 
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

To send IDocs from your SAP system to the quick start's route, you must first configure the Application Linking Enabling (ALE) subsystem in your SAP system:

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
5. Ensure that a Model View for the message flow from your SAP system to the quick start has been defined:  
   a. Run the `Maintain Distribution Model and Distribute Views` step (Modelling and Implementing Business Processes > Maintain Distribution Model and Distribute Views).   
   b. Ensure a `QUICKSTART` model view has been created with the technical name `QUICKSTART` (Edit > Model view > Create).   
   c. Ensure the `QUICKSTART` model view has a BAPI call configured (Edit > Add BAPI):   
      1. **Sender/client** : `QUICKCLNT`.  
      2. **Reciever/server** : `QUICKSTART`.  
      3. **Obj.name/interface** : `FlightCustomer`.  
      4. **Method** : `CreateFromData`.   
   d. Ensure `Partner Profiles` have been generated for the quick start and your SAP client (Environment > Generate Partner Profiles).   
 
Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of the project.
2. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instance.  
* Ensure the following `ZFLCUSTOMER_CREATEFROMDATA01` ABAP program is installed and activated in your SAP client:  

		*&---------------------------------------------------------------------*
		*& Report  ZFLCUSTOMER_CREATEFROMDATA01
		*&
		*&---------------------------------------------------------------------*
		*&
		*&
		*&---------------------------------------------------------------------*
		
		REPORT  ZFLCUSTOMER_CREATEFROMDATA01.
		
		DATA: WA_IDOC_CONTROL TYPE EDIDC,
		      IT_IDOC_COMM_CONTROL TYPE STANDARD TABLE OF EDIDC,
		      IT_IDOC_DATA TYPE STANDARD TABLE OF EDIDD,
		      WA_IDOC_DATA TYPE EDIDD.
		
		DATA: WA_E1SCU_CRE TYPE E1SCU_CRE,
		      WA_E1BPSCUNEW TYPE E1BPSCUNEW.
		
		CLEAR WA_IDOC_CONTROL.
		WA_IDOC_CONTROL-MESTYP = 'FLCUSTOMER_CREATEFROMDATA'.
		WA_IDOC_CONTROL-IDOCTP = 'FLCUSTOMER_CREATEFROMDATA01'.
		WA_IDOC_CONTROL-RCVPRN = 'QUICKSTART'.
		WA_IDOC_CONTROL-RCVPRT = 'LS'.
		WA_IDOC_CONTROL-SNDPRN = 'QUICKCLNT'.
		WA_IDOC_CONTROL-SNDPRT = 'LS'.
		
		CLEAR WA_E1SCU_CRE.
		WA_E1SCU_CRE-TEST_RUN = SPACE.
		
		CLEAR WA_IDOC_DATA.
		WA_IDOC_DATA-SEGNAM = 'E1SCU_CRE'.
		WA_IDOC_DATA-SDATA = WA_E1SCU_CRE.
		APPEND WA_IDOC_DATA TO IT_IDOC_DATA.
		
		CLEAR WA_E1BPSCUNEW.
		WA_E1BPSCUNEW-CUSTNAME = 'Fred Flintstone'.
		WA_E1BPSCUNEW-FORM = 'Mr.'.
		WA_E1BPSCUNEW-STREET = '123 Rubble Lane'.
		WA_E1BPSCUNEW-POSTCODE = '01234'.
		WA_E1BPSCUNEW-CITY = 'Bedrock'.
		WA_E1BPSCUNEW-COUNTR = 'US'.
		WA_E1BPSCUNEW-PHONE = '800-555-1212'.
		WA_E1BPSCUNEW-EMAIL = 'fred@bedrock.com'.
		WA_E1BPSCUNEW-CUSTTYPE = 'P'.
		WA_E1BPSCUNEW-DISCOUNT = '005'.
		WA_E1BPSCUNEW-LANGU = 'E'.
		
		CLEAR WA_IDOC_DATA.
		WA_IDOC_DATA-SEGNAM = 'E1BPSCUNEW'.
		WA_IDOC_DATA-SDATA = WA_E1BPSCUNEW.
		APPEND WA_IDOC_DATA TO IT_IDOC_DATA.
		
		CALL FUNCTION 'MASTER_IDOC_DISTRIBUTE'
		  EXPORTING
		    MASTER_IDOC_CONTROL = WA_IDOC_CONTROL
		  TABLES
		    COMMUNICATION_IDOC_CONTROL = IT_IDOC_COMM_CONTROL
		    MASTER_IDOC_DATA = IT_IDOC_DATA
		  EXCEPTIONS
		    ERROR_IN_IDOC_CONTROL = 01
		    ERROR_WRITING_IDOC_STATUS = 02
		    ERROR_IN_IDOC_DATA = 03
		    SENDING_LOGICAL_SYSTEM_UNKNOWN = 04.
		
		IF SY-SUBRC <> 0.
		  MESSAGE ID SY-MSGID TYPE SY-MSGTY NUMBER SY-MSGNO
		    WITH SY-MSGV1 SY-MSGV2 SY-MSGV3 SY-MSGV4.
		ENDIF.
		
		COMMIT WORK.    
		
Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the `sap-idoclist-server-spring-boot` directory.
* Run `mvn clean install` to build the quick start.
* Run `mvn spring-boot:run` to start the Camel runtime.
* Invoke the camel route from SAP:  
  a.Run the `ZFLCUSTOMER_CREATEFROMDATA01` program.  
  b.Using the SAP GUI, run transaction `SM58`, the Transactional RFC Monitor:   
    i. 		Display the outstanding transactions (Program > Execute).  
    ii.		Select the transaction destined for the `QUICKSTART`.  
    iii.	Execute the transaction to send the requests (Edit > Execute LUW).  
* In the console observe the IDoc received by the endpoint.  

Stopping the Quickstart
-----------------------

To stop the camel run-time:

1. Enter Ctrl-c in the console.

