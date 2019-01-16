SAP Quick Starts  
================  
 **This directory contains a set of quick start projects to get you started with using the suite of JBoss Fuse SAP Camel components in a Spring Boot Camel runtime.**  
![SAP Tool Suite](../sap_tool_suite.png "SAP Tool Suite")

***  
Author: William Collins - Fuse Team  
Level: Beginner to Advanced  
Technologies: SAP, Camel, Spring  
Summary: These quick starts demonstrates how to configure and use the suite of JBoss Fuse SAP Camel components to integrate Apache Camel with SAP.       
Target Product: Fuse  
Source: <http://github.com/jboss-fuse/quickstarts/sap/>  

***  

Overview  
--------  

It is recommended that you study and run the quick starts in the following order:  

###[sap-srfc-destination-spring-boot](sap-srfc-destination-spring-boot/README.md)  

Demonstrates how to perform *Synchronous Remote Function Calls* (sRFC) to SAP from Apache Camel.   

###[sap-trfc-destination-spring-boot](sap-trfc-destination-spring-boot/README.md)    

Demonstrates how to perform *Transactional Remote Function Calls* (tRFC) to SAP from Apache Camel.   

###[sap-qrfc-destination-spring-boot](sap-qrfc-destination-spring-boot/README.md)    

Demonstrates how to perform *Queued Remote Function Calls* (qRFC) to SAP from Apache Camel.   

###[sap-srfc-server-spring-boot](sap-srfc-server-spring-boot/README.md)  

Demonstrates how to handle *Synchronous Remote Function Calls* (sRFC) from SAP within Apache Camel.   
  
###[sap-trfc-server-spring-boot](sap-trfc-server-spring-boot/README.md)  

Demonstrates how to handle *Transactional Remote Function Calls* (tRFC) from SAP within Apache Camel.   

###[sap-idoc-destination-spring-boot](sap-idoc-destination-spring-boot/README.md)   

Demonstrates how to send an Intermediate Document (IDoc) to SAP from Apache Camel.   

###[sap-idoclist-destination-spring-boot](sap-idoclist-destination-spring-boot/README.md)   

Demonstrates how to send a list of Intermediate Document (IDoc) to SAP from Apache Camel.   

###[sap-qidoc-destination-spring-boot](sap-qidoc-destination-spring-boot/README.md)   

Demonstrates how to send an Intermediate Document (IDoc) using the qRFC protocol to SAP from Apache Camel.   

###[sap-qidoclist-destination-spring-boot](sap-qidoclist-destination-spring-boot/README.md)   

Demonstrates how to send a list of Intermediate Document (IDoc) using the qRFC protocol to SAP from Apache Camel.   

###[sap-idoclist-server-spring-boot](sap-idoclist-server-spring-boot/README.md)   

Demonstrates how to handle a list of Intermediate Document (IDoc) from SAP within Apache Camel.   

-----
For more information see:

* <https://access.redhat.com/documentation/en-us/red_hat_fuse/7.0/html-single/apache_camel_component_reference/#SAP> for more information about the JBoss Fuse SAP Camel components 
* <https://access.redhat.com/products/red-hat-fuse> for more information about using JBoss Fuse

System requirements
-------------------

To run these quick starts you will need:

* Maven 3.1.1 or higher
* JDK 1.8
* JBoss Fuse 7.0.0
* SAP JCo3 and IDoc3 libraries (sapjco3.jar, sapidoc3.jar and JCo native library for your OS platform)
* SAP instance with [Flight Data Application](http://help.sap.com/saphelp_erp60_sp/helpdata/en/db/7c623cf568896be10000000a11405a/content.htm) setup.
