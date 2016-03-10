Introduction
============
This quickstart demonstrates how policy can be used to control the security characteristics of a
service invocation.  The only service in the application is a Bean service called "WorkService".
SSL is used for "confidentiality", and Certificate Authentication is used for "clientAuthentication".


Building the quickstart
======================

To build the quickstart :

```
mvn clean install
```


Running the quickstart
======================

1. Start EAP in standalone mode:

        ${AS}/bin/standalone.sh

2. Build and deploy the quickstart

        mvn install -Pdeploy

3. Execute the test. (See "Options" section below.)

4. Check the server console for output from the service.

5. Undeploy the application

        mvn clean -Pdeploy


Fuse
-----

1. Edit ${FUSE_HOME}/etc/org.ops4j.pax.web.cfg file, and add the following contents:

org.osgi.service.http.secure.enabled=true

2. Edit ${FUSE_HOME}/etc/jetty.xml, and add the following contents :

```
<Call name="addConnector">
  <Arg>
    <!-- The SslSelectChannelConnector class uses the Java NIO SslEngine -->
    <New class="org.eclipse.jetty.server.ssl.SslSelectChannelConnector">
      <Arg>
        <New class="org.eclipse.jetty.http.ssl.SslContextFactory">
        <!-- Protect against the POODLE security vulnerability -->
        <Set name="ExcludeProtocols">
          <Array type="java.lang.String">
            <Item>SSLv3</Item>
          </Array>
        </Set>
        <Set name="keyStoreType">JKS</Set>
        <Set name="keyStore">quickstarts/switchyard/demos/policy-security-cert/connector.jks</Set>
        <Set name="keyStorePassword">changeit</Set>
        <Set name="keyManagerPassword">changeit</Set>
        </New>
      </Arg>
      <Set name="port">8183</Set>
      <Set name="maxIdleTime">30000</Set>
    </New>
  </Arg>
</Call>
```

3. Start the Fuse server :

${FUSE_HOME}/bin/fuse

4. Install the feature for the policy-security-cert demo :

JBossFuse:karaf@root> features:install switchyard-demo-policy-security-cert

6. When executing the test (as directed below in the "Options" section), add the following system property: -Dorg.switchyard.component.soap.client.port=8183

7. Undeploy the quickstart:

JBossFuse:karaf@root> features:uninstall switchyard-demo-policy-security-cert


Karaf
-----
1. Create a ${KARAF}/quickstarts/demos/policy-security-cert/ directory, and copy users.jks, roles.properties, and connector.jks into it.

2. Create a ${KARAF}/etc/org.ops4j.pax.web.cfg file, with the following contents:

org.osgi.service.http.enabled=true
org.osgi.service.http.port=8181
org.osgi.service.http.secure.enabled=true
org.osgi.service.http.port.secure=8183
org.ops4j.pax.web.ssl.keystore=quickstarts/demos/policy-security-cert/connector.jks
org.ops4j.pax.web.ssl.keystore.type=JKS
org.ops4j.pax.web.ssl.password=changeit
org.ops4j.pax.web.ssl.keypassword=changeit
org.ops4j.pax.web.ssl.clientauthwanted=false
org.ops4j.pax.web.ssl.clientauthneeded=false

4. Start the Fuse server :

${FUSE_HOME}/bin/fuse

5. Add the features URL for the respective version of SwitchYard.   Replace {SWITCHYARD-VERSION}
with the version of SwitchYard that you are using (ex. 2.0.0): 

karaf@root> features:addurl mvn:org.switchyard.karaf/switchyard/{SWITCHYARD-VERSION}/xml/features

6. Install the feature for the policy-security-cert :

karaf@root> features:install switchyard-demo-policy-security-cert

7. When executing the test (as directed below in the "Options" section), add the following system property: -Dorg.switchyard.component.soap.client.port=8183

8. Undeploy the quickstart:

karaf@root> features:uninstall switchyard-demo-policy-security-cert



Wildfly
----------


1. Start Wildfly in standalone mode :

        ${WILDFLY}/bin/standalone.sh

2. Build and deploy the demo :

        mvn install -Pdeploy -Pwildfly

3. Execute the test. (See "Options" section below.)

4. Check the server console for output from the service.

5. Undeploy the application

        mvn clean -Pdeploy -Pwildfly

     Warning --> Wildfly 8.0.0 When the application is undeployed, it is required to restart the server to get all the undeployment changes done.





Options
=======

When running with no options:

    mvn exec:java

, you will be hitting the http (non-SSL) URL, and see this in your log:

    Caused by: org.switchyard.exception.SwitchYardException: Required policies have not been provided: authorization clientAuthentication confidentiality

When running with this option:

    mvn exec:java -Dexec.args="confidentiality clientAuthentication" -Djavax.net.ssl.trustStore=connector.jks

, you will be hitting the https (SSL) URL and providing authentication information, and see this in your log:

    :: WorkService :: Received work command => CMD-1398262622127 (caller principal=UserPrincipal@640268438[name=kermit], in roles? 'friend'=true 'enemy'=false)

    (Because the WorkService is secured, you will see the not-null principal, and true for the expected security role.)

You can play with the exec.args and only specify one of "confidentiality" or "clientAuthentication". I bet you can guess what will happen... ;)
