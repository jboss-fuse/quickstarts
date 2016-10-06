Introduction
============
This quickstart demonstrates how policy can be used to control the security characteristics of a
service invocation.  The only service in the application is a Bean service called "WorkService".
SSL is used for "confidentiality", and Basic Authentication is used for "clientAuthentication".


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

1. Create an application user:

        ${AS}/bin/add-user.sh -a --user kermit --password the-frog-1 --group friend

2. Start EAP in standalone mode:

        ${AS}/bin/standalone.sh

3. Build and deploy the quickstart

        mvn install -Pdeploy

4. Execute the test. (See "Options" section below.)

5. Check the server console for output from the service.

6. Undeploy the application

        mvn clean -Pdeploy


Fuse
-----

1. Edit the ${FUSE_HOME}/etc/org.ops4j.pax.web.cfg file, adding the following contents:

        org.osgi.service.http.secure.enabled=true

2. Edit the ${FUSE_HOME}/etc/jetty.xml file, adding the following contents at the end of the file :

        <New id="sslHttpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
        <Arg><Ref refid="httpConfig"/></Arg>
        <Call name="addCustomizer">
        <Arg><New class="org.eclipse.jetty.server.SecureRequestCustomizer"/></Arg>
        </Call>
        </New>
        <New id="sslContextFactory" class="org.eclipse.jetty.util.ssl.SslContextFactory">
        <Set name="KeyStorePath"><Property name="jetty.base" default="." />/<Property name="jetty.keystore" default="quickstarts/switchyard/demos/policy-security-basic/connector.jks"/></Set>
        <Set name="KeyStorePassword"><Property name="jetty.keystore.password" default="changeit"/></Set>
        <Set name="KeyManagerPassword"><Property name="jetty.keymanager.password" default="changeit"/></Set>
        <Set name="TrustStorePath"><Property name="jetty.base" default="." />/<Property name="jetty.truststore" default="quickstarts/switchyard/demos/policy-security-basic/connector.jks"/></Set>
        <Set name="TrustStorePassword"><Property name="jetty.truststore.password" default="changeit"/></Set>
        <Set name="EndpointIdentificationAlgorithm"></Set>
        <Set name="NeedClientAuth"><Property name="jetty.ssl.needClientAuth" default="false"/></Set>
        <Set name="WantClientAuth"><Property name="jetty.ssl.wantClientAuth" default="false"/></Set>
        <Set name="ExcludeCipherSuites">
        <Array type="String">
        <Item>SSL_RSA_WITH_DES_CBC_SHA</Item>
        <Item>SSL_DHE_RSA_WITH_DES_CBC_SHA</Item>
        <Item>SSL_DHE_DSS_WITH_DES_CBC_SHA</Item>
        <Item>SSL_RSA_EXPORT_WITH_RC4_40_MD5</Item>
        <Item>SSL_RSA_EXPORT_WITH_DES40_CBC_SHA</Item>
        <Item>SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA</Item>
        <Item>SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA</Item>
        </Array>
        </Set>
        </New>
        <Call id="httpsConnector" name="addConnector">
        <Arg>
        <New class="org.eclipse.jetty.server.ServerConnector">
        <Arg name="server"><Ref refid="Server" /></Arg>
        <Arg name="acceptors" type="int"><Property name="ssl.acceptors" default="-1"/></Arg>
        <Arg name="selectors" type="int"><Property name="ssl.selectors" default="-1"/></Arg>
        <Arg name="factories">
        <Array type="org.eclipse.jetty.server.ConnectionFactory">
        <Item>
        <New class="org.eclipse.jetty.server.SslConnectionFactory">
        <Arg name="next">http/1.1</Arg>
        <Arg name="sslContextFactory"><Ref refid="sslContextFactory"/></Arg>
        </New>
        </Item>
        <Item>
        <New class="org.eclipse.jetty.server.HttpConnectionFactory">
        <Arg name="config"><Ref refid="sslHttpConfig"/></Arg>
        </New>
        </Item>
        </Array>
        </Arg>
        <Set name="name">0.0.0.0:8443</Set>
        <Set name="host"><Property name="jetty.host" /></Set>
        <Set name="port"><Property name="https.port" default="8443" /></Set>
        <Set name="idleTimeout"><Property name="https.timeout" default="30000"/></Set>
        <Set name="soLingerTime"><Property name="https.soLingerTime" default="-1"/></Set>
        <Set name="acceptorPriorityDelta"><Property name="ssl.acceptorPriorityDelta" default="0"/></Set>
        <Set name="selectorPriorityDelta"><Property name="ssl.selectorPriorityDelta" default="0"/></Set>
        <Set name="acceptQueueSize"><Property name="https.acceptQueueSize" default="0"/></Set>
        </New>
        </Arg>
        </Call>

3. Add the following lines to ${FUSE_HOME}/etc/users.properties:

        kermit = the-frog-1,_g_:friend
        _g_\:friend = group,friend

4. Start the Fuse server :

        ${FUSE_HOME}/bin/fuse

5. Install the feature for the policy-security-basic demo :

        JBossFuse:karaf@root> features:install switchyard-demo-policy-security-basic

7. When executing the test (as directed below), add the following system property: -Dorg.switchyard.component.soap.client.port=8443

8. Undeploy the quickstart:

        JBossFuse:karaf@root> features:uninstall switchyard-demo-policy-security-basic


Karaf
-----
Instead of steps 1-3,6 above for EAP...

1. Create a ${KARAF}/quickstarts/demos/policy-security-basic/ directory, and copy connector.jks into it.

        ${AS}/bin/standalone.sh

2. Create a ${KARAF}/etc/org.ops4j.pax.web.cfg file, with the following contents:

        org.osgi.service.http.enabled=true
        org.osgi.service.http.port=8181
        org.osgi.service.http.secure.enabled=true
        org.osgi.service.http.port.secure=8183
        org.ops4j.pax.web.ssl.keystore=quickstarts/demos/policy-security-basic/connector.jks
        org.ops4j.pax.web.ssl.keystore.type=JKS
        org.ops4j.pax.web.ssl.password=changeit
        org.ops4j.pax.web.ssl.keypassword=changeit
        org.ops4j.pax.web.ssl.clientauthwanted=false
        org.ops4j.pax.web.ssl.clientauthneeded=false

3. Add this line to ${KARAF}/etc/users.properties:

         kermit = the-frog-1,_g_:friend
         _g_\:friend = group,friend

4. Start the Karaf server :

         ${KARAF_HOME}/bin/fuse

5. Add the features URL for the respective version of SwitchYard.   Replace {SWITCHYARD-VERSION}
with the version of SwitchYard that you are using (ex. 2.0.0): 

         karaf@root> features:addurl mvn:org.switchyard.karaf/switchyard/{SWITCHYARD-VERSION}/xml/features

6. Install the feature for the policy-security-basic demo :

         karaf@root> features:install switchyard-demo-policy-security-basic

7. When executing the test (as directed below), add the following system property: 

         HTTP PORT -Dorg.switchyard.component.soap.client.port=8181  
         HTTPS PORT -Dorg.switchyard.component.soap.client.port=8183  

8. Undeploy the quickstart:

         karaf@root> features:uninstall switchyard-demo-policy-security-basic



Wildfly
----------


1. Create an application user:

        ${WILDFLY}/bin/add-user.sh

        realm=ApplicationRealm Username=kermit Password=the-frog-1 group=friend

2. Start Wildfly in standalone mode :

        ${WILDFLY}/bin/standalone.sh

3. Build and deploy the demo :

        mvn install -Pdeploy  -Pwildfly

4. Execute the test. (See "Options" section below.)

5. Check the server console for output from the service.

6. Undeploy the application

        mvn clean -Pdeploy -Pwildfly

     Warning --> Wildfly 8.0.0 When the application is undeployed, it is required to restart the server to get all the undeployment changes done.



Options for Fuse
=======

When running with these options:

        mvn exec:java -Dexec.args="confidentiality" -Djavax.net.ssl.trustStore=connector.jks -Dorg.switchyard.component.soap.client.port=8443

you should see an error informing you that both the confidentiality and clientAuthentication policies are required.

When running with this option (HTTPS):

        mvn exec:java -Dexec.args="confidentiality clientAuthentication" -Djavax.net.ssl.trustStore=connector.jks -Dorg.switchyard.component.soap.client.port=8443

you will be hitting the https (SSL) URL and providing authentication information, and see this in your log:

:: WorkService :: Received work command => CMD-1398262304944 (caller principal=kermit, in roles? 'friend'=true 'enemy'=false)

(Because the WorkService is secured, you will see the not-null principal, and true for the expected security role.)



Options for EAP/Wildfly
=======

When running with no options:

        mvn exec:java

you will be hitting the http (non-SSL) URL, and see this in your log:

        Caused by: org.switchyard.exception.SwitchYardException: Required policies have not been provided: authorization clientAuthentication confidentiality

When running with this option (HTTPS):

        mvn exec:java -Dexec.args="confidentiality clientAuthentication" -Djavax.net.ssl.trustStore=connector.jks

you will be hitting the https (SSL) URL and providing authentication information, and see this in your log:

        :: WorkService :: Received work command => CMD-1398262304944 (caller principal=kermit, in roles? 'friend'=true 'enemy'=false)

(Because the WorkService is secured, you will see the not-null principal, and true for the expected security role.)

You can play with the exec.args and only specify one of "confidentiality" or "clientAuthentication". I bet you can guess what will happen... ;)
