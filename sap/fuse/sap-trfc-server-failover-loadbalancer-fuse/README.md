Fuse SAP Transactional RFC Server Endpoint with Failover and Loadbalancing Quick Start
=======================================================================================================================
**Demonstrates the sap-trfc-server component running a load balancer in a HA/failover Fuse camel runtime.**   
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

***
Author: William Collins - Fuse Team  
Level: Intermediate  
Technologies: Fuse, HA, SAP, Camel, Blueprint  
Summary: This quickstart demonstrates how to configure and use the sap-trfc-server component in a Fuse environment to handle and load balance a remote function call from SAP. This component handles a remote function call from SAP using the *Transactional RFC* (tRFC) protocol. In addition this quickstart demonstrates how to provide High Availability (HA) to the endpoint with a master/slave failover topology of multiple Fuse containers.       
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

***

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Transactional Remote Function Call Server Camel component. This component and its endpoints should be used in cases where the sending SAP system requires **AT-MOST-ONCE** delivery of its requests to a Camel route. To accomplish this the sending SAP system generates a *transcation ID* (*tid*) which accompanies every request it sends to the component's endpoints. The sending SAP system will first check with the component whether a given tid has been received by it before sending a series of requests associated with the tid. The component will check a list of received tids it maintains, record the sent tid if it is not in that list and then respond to the sending SAP system with whether the tid had already been recorded. The sending SAP system will only then send the series of requests if the tid has not been previously recorded. This enables a sending SAP system to reliably send a series of requests once to a camel route.  

This quick start also shows how to provide high availablity to an SAP server endpoint. The SAP gateway which manages connections to systems external to SAP, such as JBoss Fuse, identifies each connection with a `PROG_ID` which must be unique for each connection. This restricts each SAP server endpoint to a single connection to an SAP system. To provide HA to the route processing the requests received by an SAP server endpoint, the route and endpoint can be deployed to a master/slave pair of Fuse containers where the SAP connection will failover to the slave container in the event of the master container's failure. This quick start leverages Fuse's container locking system to implement this master/slave failover topology.     

 Since requests from SAP to a destination are dispatched over a unique connection, all requests to an SAP destination implemented by JBoss Fuse must be handled by a unique SAP server endpoint. To share the processing load of these requests, this quickstart demonstrates the use of a load balancing EIP to distribute the load of processing these requests.     

This quick start handles requests from SAP for the `CreateFromData` method of the `FlightCustomer` BAPI to create flight customer records in the Flight Data Application. The `sap-trfc-server` endpoint at the beginning of the receiving route consumes a request from SAP and its contents is placed into the message body of the exchange's message. It then logs each request and dispatches each request, in a `round-robin` fashion, to one sub-route, either `sap-trfc-server-failover-loadbalancer-service-a-fuse-route` or `sap-trfc-server-failover-loadbalancer-service-b-fuse-route`, for processing. Each sub-route simply mocks the behavior of the `CreateFromData` method by logging the requests it receives.  

**NOTE:** The tRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.  

**NOTE:** The sending SAP system will send a request using the tRFC protocol to the compponent's endpoint only when invoking the endpoint with the **IN BACKGROUND TASK** option within SAP. These calls are stored in the SAP database and are not sent until the local transaction within SAP is committed.   

**NOTE:** If the endpoints of this component receive a request from SAP using the sRFC protocol, the request will be processed as in the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component however a response will not be sent back to the calling SAP system.    

**NOTE:** This component does not guarantee that a series of requests sent through its endpoints are handled and processed in the receiving Camel route in the same order that they were sent. The delivery and processing order of these requests may differ on the receiving Camel route due to communication errors and resends of a document list. This component only guarantees that each request is processed **AT-MOST-ONCE**. To guarantee the delivery **and** processing order of a series of requests from SAP it is incumbent upon the sending SAP system to serialize its requests to an **outbound queue** when sending them to this component to achieve **IN-ORDER** delivery and processing guarantees.  

**NOTE:** This quickstart demonstrates the failover of a master/slave pair of Fuse containers using a simple lock file mechanism for simplicity. This restricts the master/slave pair of Fuse containers to reside on the same host. For a failover scenario where the master/slave pair of Fuse containers are deployed on different hosts, this quickstart can be adapted by using a JDBC locking mechanism instead. Consult the Fuse documentation for further information.    


In studying this quick start you will learn:

* How to define a Camel route containing the JBoss Fuse SAP Transactional Remote Function Call Server Camel component using the Blueprint XML syntax.  
* How to use the JBoss Fuse SAP Transactional Remote Function Call Server Camel component to handle requests from SAP. 
* How to use the load balancing EIP to distribute the processing load of SAP requests. 
* How to configure connections used by the component.  
* How to configure the Fuse runtime environment in order to deploy the JBoss Fuse SAP Transactional Remote Function Call Server Camel component.  
* How to configure the Fuse runtime environment to support HA/failover topology.  

For more information see:

* <https://access.redhat.com/documentation/en-us/red_hat_fuse/7.0/html-single/apache_camel_component_reference/#SAP> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/products/red-hat-fuse> for more information about using JBoss Fuse

System requirements
-------------------

Before building and running this quick start you will need:

* Maven 3.1.1 or higher
* JDK 1.8
* Two JBoss Fuse 7.0.0 installations.
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.

Configuring the Quickstart for your environment
-----------------------------------------------

To configure the quick start for your environment: 

1. Deploy the JCo3 library jar and native library (for your platform) and IDoc3 library jar to the `lib` folder of your JBoss Fuse installations.  
2. In each JBoss Fuse installation, copy the `org.osgi.framework.system.packages.extra` property from the config properties file (`etc/config.properties`) to the custom properties file (`etc/custom.properties`) and append the following packages to the copied property:  

> org.osgi.framework.system.packages.extra = \  
>> ... \  
>> com.sap.conn.idoc, \  
>> com.sap.conn.idoc.jco, \   
>> com.sap.conn.jco, \   
>> com.sap.conn.jco.ext, \   
>> com.sap.conn.jco.monitor, \  
>> com.sap.conn.jco.rt, \   
>> com.sap.conn.jco.server  

3. In each JBoss Fuse installation, add the following properies to the system properties file (`etc/system.properties`):  

> karaf.lock=true  
> karaf.lock.class=org.apache.karaf.main.lock.SimpleFileLock  
> karaf.lock.dir=/path/to/lock/file/directory.  
> karaf.lock.delay=10000   
  
3. Ensure that the **SAP Instance Configuration Configuration Parameters** in the parent pom.xml file (`../../.pom.xml`) of quick starts project has been set to match the connection configuration for your SAP instances.  
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
6. Ensure the following `ZBAPI_FLCUST_CREATEFROMDATA` ABAP program is installed and activated in your SAP client:  

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

1. Change your working directory to the `sap-trfc-server-fuse` directory.
* Run `mvn clean install` to build the quick start.
* In one JBoss Fuse installation directory run, `./bin/fuse` to start that JBoss Fuse runtime.
* In the JBoss Fuse console, run `install -s mvn:org.fusesource/camel-sap` to install the JBoss Fuse SAP Synchronous Remote Function Call Server Camel component. Note the bundle number for the component bundle returned by this command.  
* In the JBoss Fuse console, run `install -s mvn:org.jboss.quickstarts.fuse/sap-trfc-server-fuse` to install the quick start. Note the bundle number for the quick start returned by this command. 
* In the JBoss Fuse console, run `shutdown -f` to shutdown the JBoss Fuse runtime.
* In the other JBoss Fuse installation repeat steps 3 through 5. This installation will act as the master.
* In the previous JBoss Fuse installation directory run, `./bin/fuse` to start that JBoss Fuse runtime which pauses its startup waiting for the lock. This installation will act as the slave.
* In the JBoss Fuse console of the master, run `log:tail` to monitor the JBoss Fuse log.
* Invoke the camel route from SAP by running the `ZBAPI_FLCUST_CREATEFROMDATA` program.
* In the console of the master observe each request is received by the endpoint and dispatched to the sub-routes in a round-robin fashion.  
* In the JBoss Fuse console, run `shutdown -f` to shutdown the master JBoss Fuse runtime. Note that the slave after a delay obtains the lock and completes its startup. This installation will now act as the master. 
* In the JBoss Fuse console of the new master, run `log:tail` to monitor the JBoss Fuse log.
* Invoke the camel route from SAP by running the `ZBAPI_FLCUST_CREATEFROMDATA` program.
* In the console of the new master observe each request is received by the endpoint and dispatched to the sub-routes in a round-robin fashion as was done by the previous master.  

Stopping and Uninstalling the Quickstart
----------------------------------------

To uninstall the quick start and stop the JBoss Fuse run-times perform the following in the JBoss Fuse console:

1. Enter Ctrl-c to stop monitoring the JBoss Fuse log.
* Run `uninstall <quickstart-bundle-number>` providing the bundle number for the quick start bundle. 
* Run `uninstall <camel-sap-bundle-number>` providing the bundle number for the component bundle. 
* Run `shutdown -f` to shutdown the JBoss Fuse runtime.
* In the other JBoss Fuse installation directory run, `./bin/fuse` to start that JBoss Fuse runtime and repeat steps 2 through 4.  

