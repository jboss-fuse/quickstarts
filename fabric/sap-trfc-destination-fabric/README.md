FABRIC SAP Transactional RFC Destination Endpoint Quick Start
===========================================================
**Demonstrates the sap-trfc-destination component running in a Fabric camel runtime.**  
![SAP Tool Suite](../../sap_tool_suite.png "SAP Tool Suite")

* * *
Author: William Collins - JBoss Fuse Team  
Level: Advanced  
Technologies: Fabric, Camel, SAP  
Summary: This quick start demonstrates how to configure and use the sap-trfc-destination component in a JBoss Fuse Fabric environment to invoke remote function modules and BAPI methods within SAP. This component invokes remote function modules and BAPI methods within SAP using the *Transactional RFC* (tRFC) protocol.  
Target Product: Fuse  
Source: <https://github.com/jboss-fuse/fuse/tree/master/quickstarts/camel-sap>  

* * *

What is it?  
-----------  

This quick start shows how to integrate Apache Camel with SAP using the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component. This component and its endpoints should be used in cases where requests must be delivered to the receiving SAP system **AT-MOST-ONCE**. To accomplish this the component generates a *transcation ID* (*tid*) which accompanies every request sent through the component in a route's exchange. The receiving SAP system records the tid accompanying a request before delivering the request; if the SAP system receives the request again with the same tid it will not deliver the request. Thus if a route encounters a communication error when sending a request through an endpoint of this component it can retry sending the request within the same exchange knowing it will be delivered and executed only once.   

This quick start uses XML files containing serialized SAP requests to create Customer records in the Flight Data Application within SAP. These files are consumed by the quickstart's route and their contents are then converted to string message bodies. These messages are then routed to an `sap-trfc-destination` endpoint which converts and sends them to SAP as `BAPI_FLCUST_CREATEFROMDATA` requests to create Customer records. This endpoint uses the tRFC protocol to send these requests.  

**NOTE:** The tRFC protocol used by this component is asynchronous and does not return a response and thus the endpoints of this component do not return a response message.  

**NOTE:** This component does not guarantee that a series of requests sent through its endpoints are delivered and processed in the receiving SAP system in the same order that they were sent. The delivery and processing order of these requests may differ on the receiving SAP system due to communication errors and resends of a request. To guarantee the delivery and processing order of a series of requests please see the JBoss Fuse SAP Queued Remote Function Call Destination Camel component.     

In studying this quick start you will learn:

* How to define, build and deploy a JBoss Fuse Fabric profile that configures a Fabric container to use the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component
* How to define a Camel route containing the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component using the Blueprint XML syntax.
* How to use the JBoss Fuse SAP Transactional Remote Function Call Destination Camel component to reliably update data in SAP. 
* How to configure connections used by the component.

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

3. Edit the camel context file (`src/main/fabric8/camel.xml`) and modify the `quickstartDestinationData` bean to match the connection configuration for your SAP instance.
4. Edit the project's request files (`src/data/request?.xml`) and enter the SID of your SAP in the location indicated.

Build and Run the Quickstart
----------------------------

To build and run the quick start:

1. Change your working directory to the quick start directory `sap-trfc-destination-fabric`.
2. Run `mvn clean install` to build the quick start.
3. In your JBoss Fuse installation directory run, `./bin/fuse` to start the JBoss Fuse runtime.
4. It is assumed that you have already created a fabric and logged into a container called `root` from the JBoss Fuse console.
5. In the quick start directory run `mvn fabric8:deploy` to upload the quick start's profile (`sap-trfc-destination-fabric`) to the fabric container.
6. Create a new child container and deploy the `sap-trfc-destination-fabric` profile in a single step, by entering the
 following command in the JBoss Fuse console:

        fabric:container-create-child --profile sap-trfc-destination-fabric root mychild

7. Wait for the new child container, `mychild`, to start up. Use the `fabric:container-list` command to check the status of the `mychild` container and wait until the `[provision status]` is shown as `success`.

	**Note:** You may need to restart the `mychild` container to successfully provision it.  

8. Log into the `mychild` container using the `fabric:container-connect` command, as follows:

		fabric:container-connect mychild

9. In the `mychild` container's JBoss Fuse console, run `log:tail` to monitor the container's log.
10. Copy the request files (`src/data/request?.xml`) in the project to the input directory (`instances/mychild/work/sap-trfc-destination-fabric/input`) of the quick start route.
11. In the container's log observe the requests sent by the endpoint.
12. Using the SAP GUI, run transaction `SE16`, Data Browser, and display the contents of the table `SCUSTOM`.
13. Search the table (Edit > Find..) for the newly created Customer records: `Fred Flintstone`, `Wilma Flintstone`, `Barney Rubble`, and `Betty Rubble`. 

Stopping and Uninstalling the Quickstart
----------------------------------------

To uninstall the quick start and stop the JBoss Fuse run-time perform the following in the JBoss Fuse console:

1. Disconnect from the child container by typing `Ctrl-D` at the console prompt.
2. Stop and delete the child container by entering the following command at the JBoss Fuse console:

		fabric:container-stop mychild
		fabric:container-delete mychild

3. Delete the quick start's profile by entering the following command at the JBoss Fuse console: 

		fabric:profile-delete sap-trfc-destination-fabric

4. Run `osgi:shutdown -f` to shutdown the JBoss Fuse runtime.

