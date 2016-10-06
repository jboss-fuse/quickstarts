Introduction
============
This quickstart demonstrates the use of a JMS binding for a BPEL service.  Also of
note : the Camel JMS binding in the quickstart uses WSDL as it's interface type
instead of Java.


![BPEL JMS Binding Quickstart](https://github.com/jboss-switchyard/quickstarts/raw/master/bpel-service/jms_binding/bpel-jms-binding.jpg)


Building the quickstart
======================

To build the quickstart :

```
mvn clean install
```


Running the quickstart
======================

EAP
----------
1. Start EAP in standalone-full mode:

        ${AS}/bin/standalone.sh --server-config=standalone-full.xml

2. Create an application user:

        ${AS}/bin/add-user.sh -a --user guest --password guestp.1 --group guest

3. Build and deploy the quickstart

        mvn install -Pdeploy

4. Execute HornetQClient.   See the "Expected Output" heading for the expected results.

        mvn exec:java

5. Undeploy the quickstart:
        mvn clean -Pdeploy


Fuse
----------
1. Check that the karaf user is created on the  ${KARAF_HOME}/etc/users.properties .  In case no karaf user
   is created add these lines:

 karaf = karaf,_g_:admingroup
_g_\:admingroup = group,admin,manager,viewer,webconsole

2. Start the Fuse server :

${FUSE_HOME}/bin/fuse

3. Install the feature for the bpel-jms-binding quickstart :

JBossFuse:karaf@root> features:install switchyard-quickstart-bpel-jms-binding

4. To submit a webservice request to invoke the SOAP gateway, run the quickstart client :
<br/>
```
mvn exec:java -Pkaraf
```
<br/>

5. Undeploy the quickstart:

JBossFuse:karaf@root> features:uninstall switchyard-quickstart-bpel-jms-binding


Wildfly
----------
1. Start Wildfly in standalone-full mode:

        ${AS}/bin/standalone.sh --server-config=standalone-full.xml

2. Create an application user:

        ${AS}/bin/add-user.sh -a --user guest --password guestp.1 --group guest

3. Build and deploy the quickstart

        mvn install -Pwildfly -Pdeploy 

4. Execute HornetQClient.   See the "Expected Output" heading for the expected results.

        mvn exec:java -Pwildfly

5. Undeploy the quickstart:

        mvn clean -Pdeploy -Pwildfly


Karaf
----------
1. Check that the karaf user is created on the  ${KARAF_HOME}/etc/users.properties .  In case no karaf user
   is created add these lines:

 karaf = karaf,_g_:admingroup
_g_\:admingroup = group,admin,manager,viewer,webconsole

2. Start the Karaf server :

${KARAF_HOME}/bin/karaf

3. Add the features URL for the respective version of SwitchYard.   Replace {SWITCHYARD-VERSION}
with the version of SwitchYard that you are using (ex. 2.0.0): 

karaf@root> features:addurl mvn:org.switchyard.karaf/switchyard/{SWITCHYARD-VERSION}/xml/features

4. Install ActiveMQ broker if it's not running yet :

karaf@root> features:install activemq-broker-noweb

5. Install the feature for the bpel-jms-binding quickstart :

karaf@root> features:install switchyard-quickstart-bpel-jms-binding

6. To submit a webservice request to invoke the SOAP gateway, run the quickstart client :
<br/>
```
mvn exec:java -Pkaraf
```
<br/>

7. Undeploy the quickstart:

karaf@root> features:uninstall switchyard-quickstart-bpel-jms-binding



Expected Output
===============
```
Message sent. Waiting for reply ...
REPLY: 
<sayHelloResponse xmlns="http://www.jboss.org/bpel/examples">
<tns:result xmlns:tns="http://www.jboss.org/bpel/examples">Hello Skippy</tns:result>
</sayHelloResponse>
```
