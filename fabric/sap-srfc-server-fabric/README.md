FABRIC SAP Synchronous RFC Server Endpoint Quick Start
===========================================================
**Demonstrates the sap-srfc-server component running in a Fabric camel runtime.**  
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - JBoss Fuse Team  
Level: Advanced  
Technologies: Fabric, Camel, SAP  
Summary: This quick start demonstrates how to configure and use the sap-srfc-server component in a JBoss Fuse Fabric environment to handle a remote function call from SAP. This component handles a remote function call from SAP using the *Synchronous RFC* (sRFC) protocol.  
Target Product: Fuse  
Source: <https://github.com/jboss-fuse/fuse/tree/master/quickstarts/camel-sap>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component. This component and its endpoints should be used in cases where a Camel route is required to synchronously handle requests from and responses to an SAP system.  

This quick start handles requests from SAP for the `GetList` method of the `FlightCustomer` BAPI to query for Customer records in the Flight Data Application. The route of this quick start simply mocks the behavior of this method by returning a fixed response of Customer records. The `sap-srfc-server` endpoint at the beginning of the route consumes a request from SAP and its contents is placed into the message body of the exchange's message. The request is then logged to the console. A processor bean then builds a response message for the BAPI method which contains a fixed set of customer records. It then replaces the message body of the exchange's message with this response. The response is then logged to the console. The `sap-srfc-server`  endpoint then sends the response message back to the caller in SAP.  

**NOTE** The sRFC protocol used by this component delivers requests and responses from and to an SAP system **BEST-EFFORT**. When an SAP system experiences a communication error when sending a request to or receiving a response from this component, it will be *in doubt* whether the processing of a remote function call in Camel was successful. For the guaranteed delivery and processing of requests in Camel please see the JBoss Fuse SAP Transactional Remote Function Call Server Camel component.     

In studying this quick start you will learn:

* How to define, build and deploy a JBoss Fuse Fabric profile that configures a Fabric container to use the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component
* How to define a Camel route containing the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component using the Blueprint XML syntax.
* How to use the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component to handle requests from SAP. 
* How to configure connections used by the component.

For more information see:

* <https://access.redhat.com/documentation/en/red-hat-jboss-fuse/6.3/paged/apache-camel-component-reference/chapter-138-sap-component> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/documentation/en/red-hat-jboss-fuse/> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.0.4 or higher
* JDK 1.7 or 1.8
* A JBoss Fuse 6.3.0 container running within a Fabric
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

3. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instance.  
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
5. Ensure the following `ZBAPI_FLCUST_GETLIST` ABAP program is installed and activated in your SAP client:  

			*&---------------------------------------------------------------------*
			*& Report  ZBAPI_FLCUST_GETLIST
			*&
			*&---------------------------------------------------------------------*
			*&
			*&
			*&---------------------------------------------------------------------*
			
			REPORT  ZBAPI_FLCUST_GETLIST.
			
			
			DATA: RFCDEST LIKE RFCDES-RFCDEST VALUE 'NONE'.
			
			
			DATA: RFC_MESS(128).
			
			
			DATA: CUSTOMER_DATA LIKE BAPISCUDAT,
			      IT_CUSTOMER_LIST TYPE STANDARD TABLE OF BAPISCUDAT.
			
			DATA: IT_RETURN TYPE STANDARD TABLE OF BAPIRET2,
			      RETURN TYPE BAPIRET2.
			
			RFCDEST = 'QUICKSTART'.
			
			CALL FUNCTION 'BAPI_FLCUST_GETLIST'
			  DESTINATION RFCDEST
			  TABLES
			    CUSTOMER_LIST = IT_CUSTOMER_LIST
			    RETURN = IT_RETURN.
			
			IF SY-SUBRC NE 0.
			
			WRITE: / 'Call ZBAPI_FLCUST_GETLIST SY-SUBRC = ', SY-SUBRC.
			WRITE: / RFC_MESS.
			
			ELSE.
			
			WRITE: / 'CUSTOMER_LIST:'.
			ULINE.
			
			WRITE: /5 'CUSTOMERID', 16 'FORM', 30 'CUSTNAME', 55 'STREET', 85 'POSTCODE', 95 'CITY', 120 'COUNTR', 127 'PHONE', 157 'EMAIL'.
			
			LOOP AT IT_CUSTOMER_LIST INTO CUSTOMER_DATA.
			
			WRITE: /5 CUSTOMER_DATA-CUSTOMERID, 16 CUSTOMER_DATA-FORM, 30 CUSTOMER_DATA-CUSTNAME, 55 CUSTOMER_DATA-STREET, 85 CUSTOMER_DATA-POSTCODE, 95 CUSTOMER_DATA-CITY, 120 CUSTOMER_DATA-COUNTR, 127 CUSTOMER_DATA-PHONE, 157 CUSTOMER_DATA-EMAIL.
			
			ENDLOOP.
			
			WRITE: / 'RETURN:'.
			ULINE.
			
			WRITE: /5 'TYPE', 20 'ID', 40 'MESSAGE'.
			
			LOOP AT IT_RETURN INTO RETURN.
			
			WRITE: /5 RETURN-TYPE, 20 RETURN-ID, 40 RETURN-MESSAGE.
			
			ENDLOOP.
			ENDIF.


Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the quick start directory `sap-srfc-server-fabric`.
* Run `mvn clean install` to build the quick start.
* In your JBoss Fuse installation directory run, `./bin/fuse` to start the JBoss Fuse runtime.
* It is assumed that you have already created a fabric and logged into a container called `root` from the JBoss Fuse console.
* In the quick start directory run `mvn fabric8:deploy` to upload the quick start's profile (`sap-srfc-server-fabric`) to the fabric container.
* Create a new child container and deploy the `sap-srfc-server-fabric` profile in a single step, by entering the
 following command in the JBoss Fuse console:

        fabric:container-create-child --profile sap-srfc-server-fabric root mychild

7. Wait for the new child container, `mychild`, to start up. Use the `fabric:container-list` command to check the status of the `mychild` container and wait until the `[provision status]` is shown as `success`.

	**Note:** You may need to restart the `mychild` container to successfully provision it.  

8. Log into the `mychild` container using the `fabric:container-connect` command, as follows:

		fabric:container-connect mychild
				
9. In the `mychild` container's JBoss Fuse console, run `log:tail` to monitor the container's log.
10. Invoke the camel route from SAP by running the `ZBAPI_FLCUST_GETLIST` program.
11. In the container's log observe the request received and the response returned by the endpoint.
12. Compare the log's response with the received data displayed by the ABAP program.   

Stopping and Uninstalling the Quickstart
----------------------------------------

To uninstall the quick start and stop the JBoss Fuse run-time perform the following in the JBoss Fuse console:

1. Disconnect from the child container by typing `Ctrl-D` at the console prompt.
2. Stop and delete the child container by entering the following command at the JBoss Fuse console:

		fabric:container-stop mychild
		fabric:container-delete mychild

3. Delete the quick start's profile by entering the following command at the JBoss Fuse console: 

		fabric:profile-delete sap-srfc-server-fabric

4. Run `osgi:shutdown -f` to shutdown the JBoss Fuse runtime.

