FABRIC SAP Transactional RFC Server Endpoint Quick Start
===========================================================
**Demonstrates the sap-trfc-server component running in a Fabric camel runtime.**  
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - JBoss Fuse Team  
Level: Advanced  
Technologies: Fabric, Camel, SAP  
Summary: This quick start demonstrates how to configure and use the sap-trfc-server component in a JBoss Fuse Fabric environment to handle a remote function call from SAP. This component handles a remote function call from SAP using the *Transactional RFC* (tRFC) protocol.    
Target Product: Fuse  
Source: <https://github.com/jboss-fuse/fuse/tree/master/quickstarts/camel-sap>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Transactional Remote Function Call Server Camel component. This component and its endpoints should be used in cases where the sending SAP system requires **AT-MOST-ONCE** delivery of its requests to a Camel route. To accomplish this the sending SAP system generates a *transcation ID* (*tid*) which accompanies every request it sends to the component's endpoints. The sending SAP system will first check with the component whether a given tid has been received by it before sending a series of requests associated with the tid. The component will check a list of received tids it maintains, record the sent tid if it is not in that list and then respond to the sending SAP system with whether the tid had already been recorded. The sending SAP system will only then send the series of requests if the tid has not been previously recorded. This enables a sending SAP system to reliably send a series of requests once to a camel route. 

This quick start handles requests from SAP for the `BAPI_FLCUST_CREATEFROMDATA` BAPI method to create Customer records in the Flight Data Application. The route of this quick start simply mocks the behavior of this method by logging the requests it receives. The `sap-trfc-server` endpoint at the beginning of the route consumes requests from SAP and their contents are then converted to string message bodies and logged to the console. 

**NOTE:** The tRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.  

**NOTE:** The sending SAP system will send a request using the tRFC protocol to the compponent's endpoint only when invoking the endpoint with the **IN BACKGROUND TASK** option within SAP. These calls are stored in the SAP database and are not sent until the local transaction within SAP is committed.   

**NOTE:** If the endpoints of this component receive a request from SAP using the sRFC protocol, the request will be processed as in the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component however a response will not be sent back to the calling SAP system.    

**NOTE:** This component does not guarantee in and of itself the order of a series of requests through its endpoints in a transaction. It is incumbent upon the sending SAP system to serialize the requests it sends to this component; the component simply delivers the requests in the order it receives them. To achieve **Queued RFC** (qRFC) delivery guarantees, the sending SAP system should use an output queue when sending its requests.     

In studying this quick start you will learn:

* How to define, build and deploy a JBoss Fuse Fabric profile that configures a Fabric container to use the JBoss Fuse SAP Transactional Remote Function Call Server Camel component
* How to define a Camel route containing the JBoss Fuse SAP Transactional Remote Function Call Server Camel component using the Blueprint XML syntax.
* How to use the JBoss Fuse SAP Transactional Remote Function Call Server Camel component to handle requests from SAP. 
* How to configure connections used by the component.
* How to configure the Fuse runtime environment in order to deploy the JBoss Fuse SAP Transactional Remote Function Call Server Camel component.

For more information see:

* <https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.2/html/Apache_Camel_Component_Reference/SAP.html> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/site/documentation/JBoss_Fuse/> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.0.4 or higher
* JDK 1.7 or 1.8
* A JBoss Fuse 6.2.1 container running within a Fabric
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the QuickStart for your environment
-----------------------------------------------

To configure the quick start for your environment:

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to a network accessible location in your environment.
2. Edit the profile configuration file (`src/main/fabric8/io.fabric8.agent.properties`) in the quick start and set the network locations of the SAP libraries in your environment:

>lib.sapjco3.jar=http://host/path/to/library/sapjco3.jar  
>lib.sapidoc3.jar=http://host/path/to/library/sapidoc3.jar  
>lib.sapjco3.nativelib=http://host/path/to/library/\<native-lib\>  

3. Edit the camel context file (`src/main/fabric8/camel.xml`) and modify the `quickstartDestinationData` and `quickstartServerData` beans to match the connection configuration for your SAP instance.
4. Ensure the destination `QUICKSTART` has been defined in your SAP instance:   
	a. Using the SAP GUI, run transaction `SM59` (RFC Destinations).    
    b. Create a new destination (Edit > Create):  
		1. **RFC Destination** : `QUICKSTART`.    
        2. **Connection Type** : `T`.    
        3. **Technical Settings** :    
            i. **Activation Type** : `Registered Server Program`.    
            ii.**Program ID** : `QUICKSTART`.   
        4. **Unicode**:   
        	i. **Communication Type with Target System** : `Unicode`   
5. Ensure the following `ZBAPI_FLCUST_CREATEFROMDATA` ABAP program is installed and activated in your SAP client:  

		*&---------------------------------------------------------------------*
		*& Report  ZBAPI_FLCUST_CREATEFROMDATA
		*&
		*&---------------------------------------------------------------------*
		*&
		*&
		*&---------------------------------------------------------------------*
		
		REPORT  ZBAPI_FLCUST_CREATEFROMDATA.
		
		
		DATA: RFCDEST LIKE RFCDES-RFCDEST VALUE 'NONE'.
		
		
		DATA: RFC_MESS(128).
		
		
		DATA: CUSTOMER_DATA LIKE BAPISCUNEW.
		
		RFCDEST = 'QUICKSTART'.
		
		CUSTOMER_DATA-CUSTNAME = 'Fred Flintstone'.
		CUSTOMER_DATA-FORM = 'Mr.'.
		CUSTOMER_DATA-STREET = '123 Rubble Lane'.
		CUSTOMER_DATA-POSTCODE = '01234'.
		CUSTOMER_DATA-CITY = 'Bedrock'.
		CUSTOMER_DATA-COUNTR = 'US'.
		CUSTOMER_DATA-PHONE = '800-555-1212'.
		CUSTOMER_DATA-EMAIL = 'fred@bedrock.com'.
		CUSTOMER_DATA-CUSTTYPE = 'P'.
		CUSTOMER_DATA-DISCOUNT = '005'.
		CUSTOMER_DATA-LANGU = 'E'.
		
		CALL FUNCTION 'BAPI_FLCUST_CREATEFROMDATA'
		  IN BACKGROUND TASK
		  DESTINATION RFCDEST
		  EXPORTING
		    CUSTOMER_DATA = CUSTOMER_DATA.
		
		CUSTOMER_DATA-CUSTNAME = 'Barney Rubble'.
		CUSTOMER_DATA-FORM = 'Mr.'.
		CUSTOMER_DATA-STREET = '123 Pebble Road'.
		CUSTOMER_DATA-POSTCODE = '98765'.
		CUSTOMER_DATA-CITY = 'Flagstone'.
		CUSTOMER_DATA-COUNTR = 'US'.
		CUSTOMER_DATA-PHONE = '800-555-1313'.
		CUSTOMER_DATA-EMAIL = 'barney@flagstone.com'.
		CUSTOMER_DATA-CUSTTYPE = 'P'.
		CUSTOMER_DATA-DISCOUNT = '005'.
		CUSTOMER_DATA-LANGU = 'E'.
		
		CALL FUNCTION 'BAPI_FLCUST_CREATEFROMDATA'
		  IN BACKGROUND TASK
		  DESTINATION RFCDEST
		  EXPORTING
		    CUSTOMER_DATA = CUSTOMER_DATA.
		
		IF SY-SUBRC NE 0.
		
		WRITE: / 'Call ZBAPI_FLCUST_CREATEFROMDATA SY-SUBRC = ', SY-SUBRC.
		WRITE: / RFC_MESS.
		
		ENDIF.
		
		COMMIT WORK.

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the quick start directory `sap-trfc-server-fabric`.
* Run `mvn clean install` to build the quick start.
* In your JBoss Fuse installation directory run, `./bin/fuse` to start the JBoss Fuse runtime.
* It is assumed that you have already created a fabric and logged into a container called `root` from the JBoss Fuse console.
* In the quick start directory run `mvn fabric8:deploy` to upload the quick start's profile (`sap-trfc-server-fabric`) to the fabric container.
* Create a new child container and deploy the `sap-trfc-server-fabric` profile in a single step, by entering the
 following command in the JBoss Fuse console:

        fabric:container-create-child --profile sap-trfc-server-fabric root mychild

7. Wait for the new child container, `mychild`, to start up. Use the `fabric:container-list` command to check the status of the `mychild` container and wait until the `[provision status]` is shown as `success`.

	**Note:** You may need to restart the `mychild` container to successfully provision it.  

8. Log into the `mychild` container using the `fabric:container-connect` command, as follows:

		fabric:container-connect mychild
				
9. In the `mychild` container's JBoss Fuse console, run `log:tail` to monitor the container's log.
11. Invoke the camel route from SAP:  
  a.Run the `ZBAPI_FLCUST_CREATEFROMDATA` program.  
  b.Using the SAP GUI, run transaction `SM58`, the Transactional RFC Monitor:   
    i. 		Display the outstanding transactions (Program > Execute).  
    ii.		Select the transaction destined for the `QUICKSTART`.  
    iii.	Execute the transaction to send the requests (Edit > Execute LUW).  
12. In the container's log observe the request received by the endpoint. 

Stopping and Uninstalling the Quickstart
----------------------------------------

To uninstall the quick start and stop the JBoss Fuse run-time perform the following in the JBoss Fuse console:

1. Disconnect from the child container by typing `Ctrl-D` at the console prompt.
2. Stop and delete the child container by entering the following command at the JBoss Fuse console:

		fabric:container-stop mychild
		fabric:container-delete mychild

3. Delete the quick start's profile by entering the following command at the JBoss Fuse console: 

		fabric:profile-delete sap-trfc-server-fabric

4. Run `osgi:shutdown -f` to shutdown the JBoss Fuse runtime.

