FABRIC SAP Quick Starts  
=====================  
 **This directory contains a set of quick start projects to get you started with using the suite of JBoss Fuse SAP Camel components in a JBoss Fuse Fabric environment.**  
![SAP Tool Suite](../sap_tool_suite.png "SAP Tool Suite")

***  
Author: William Collins - Fuse Team  
Level: Beginner to Advanced  
Technologies: SAP, Camel, JBoss Fuse  
Summary: These quick starts demonstrates how to configure and use the suite of JBoss Fuse SAP Camel components to integrate Apache Camel with SAP in a JBoss Fuse environment.       
Target Product: Fuse  
Source: <http://github.com/punkhorn/sap-quickstarts/>  

***  

Overview  
--------  

It is recommended that you study and run the quick starts in the following order:  

###[sap-srfc-destination-fabric](sap-srfc-destination-fabric/README.md)  

Demonstrates how to perform *Synchronous Remote Function Calls* (sRFC) to SAP from Apache Camel in a JBoss Fuse Fabric Container.   
  
###[sap-trfc-destination-fabric](sap-trfc-destination-fabric/README.md)  

Demonstrates how to perform *Transactional Remote Function Calls* (tRFC) to SAP from Apache Camel in JBoss Fuse Fabric Container.      
  
###[sap-qrfc-destination-fabric](sap-qrfc-destination-fabric/README.md)  

Demonstrates how to perform *Queued Remote Function Calls* (qRFC) to SAP from Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-srfc-server-fabric](sap-srfc-server-fabric/README.md)   

Demonstrates how to handle *Synchronous Remote Function Calls* (sRFC) from SAP within Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-trfc-server-fabric](sap-trfc-server-fabric/README.md)  

Demonstrates how to handle *Transactional Remote Function Calls* (tRFC) from SAP within Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-idoc-destination-fabric](sap-idoc-destination-fabric/README.md)  

Demonstrates how to send an Intermediate Document (IDoc) to SAP from Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-idoclist-destination-fabric](sap-idoclist-destination-fabric/README.md)  

Demonstrates how to send a list of Intermediate Documents (IDoc) to SAP from Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-qidoc-destination-fabric](sap-qidoc-destination-fabric/README.md)  

Demonstrates how to send an Intermediate Document (IDoc) using the qRFC protocol to SAP from Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-qidoclist-destination-fabric](sap-qidoclist-destination-fabric/README.md)  

Demonstrates how to send a list of Intermediate Documents (IDoc) using the qRFC protocol to SAP from Apache Camel in JBoss Fuse Fabric Container.   
  
###[sap-idoclist-server-fabric](sap-idoclist-server-fabric/README.md)  

Demonstrates how to handle a list of Intermediate Documents (IDoc) from SAP within Apache Camel in JBoss Fuse Fabric Container.   
  
-----
For more information see:

* <https://access.redhat.com/documentation/en/red-hat-jboss-fuse/6.3/paged/apache-camel-component-reference/chapter-138-sap-component> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/documentation/en/red-hat-jboss-fuse/> for more information about using JBoss Fuse

System requirements
-------------------

To run these quick starts you will need:

* Maven 3.0.4 or higher
* JDK 1.7 or 1.8
* JBoss Fuse 6.3.0
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.
  