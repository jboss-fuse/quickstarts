Introduction
============
This quickstart demonstrates the usage of the camel-dozer through SwitchYard declarative transformation.
The OrderService consumes a XML order file and the SwitchYard transformer transforms it into json format using dozer endpoint, then write to output file.

![Transform Dozer Quickstart](https://github.com/jboss-switchyard/quickstarts/raw/master/transform-dozer/transform-dozer.jpg)


Prerequisites
=============
Maven

Running the quickstart
======================


EAP
----------

1. Change directory to the ${EAP_HOME} directory.

2. Start EAP in standalone mode:

        bin/standalone.sh

Note : make sure that you execute bin/standalone.sh from the ${EAP_HOME} directory, and not from ${EAP_HOME}/bin or another
directory.

2. Build and deploy the Quickstart : 

        mvn install -Pdeploy

3. Copy src/test/resources/abc-order.xml to ${EAP}/target/input/abc-order.xml

4. Check the output file ${EAP_HOME}/target/output/xyz-order.json, it should look like src/test/resources/xyz-order.json

5. Undeploy the quickstart:

        mvn clean -Pdeploy


Fuse
=================================

1. Start the Fuse server :

${FUSE_HOME}/bin/fuse

2. Install the feature for the camel-dozer quickstart :

JBossFuse:karaf@root> features:install switchyard-quickstart-transform-datamapper

3. Copy src/test/resources/abc-order.xml to ${FUSE_HOME}/target/input/abc-order.xml

4. Check the output file ${FUSE_HOME}/target/output/xyz-order.json, it should look like src/test/resources/xyz-order.json

5. Undeploy the quickstart:

JBossFuse:karaf@root> features:uninstall switchyard-quickstart-transform-datamapper


Wildfly
----------

1. Change directory to the ${WILDFLY_HOME} directory.

2. Start WildFly in standalone mode:

        ${WILDFLY_HOME}/bin/standalone.sh

2. Build and deploy the Quickstart : 

        mvn install -Pdeploy -Pwildfly

3. Copy src/test/resources/abc-order.xml to ${WILDFLY_HOME}/target/input/abc-order.xml

4. Check the output file ${WILDFLY_HOME}/target/output/xyz-order.json, it should look like src/test/resources/xyz-order.json

4. Undeploy the quickstart:

        mvn clean -Pdeploy -Pwildfly


Karaf
=================================

1. Start the Karaf server :

${KARAF_HOME}/bin/karaf

2. Add the features URL for the respective version of SwitchYard.   Replace {SWITCHYARD-VERSION}
with the version of SwitchYard that you are using (ex. 2.0.0): 

karaf@root> features:addurl mvn:org.switchyard.karaf/switchyard/{SWITCHYARD-VERSION}/xml/features

3. Install the feature for the transform-datamapper quickstart :

karaf@root> features:install switchyard-quickstart-transform-datamapper

4. Copy src/test/resources/abc-order.xml to ${KARAF_HOME}/target/input/abc-order.xml

4. Check the output file ${KARAF_HOME}/target/output/xyz-order.json, it should look like src/test/resources/xyz-order.json

5. Undeploy the quickstart:

karaf@root> features:uninstall switchyard-quickstart-transform-datamapper



## Further Reading

1. [Transformation Documentation](https://docs.jboss.org/author/display/SWITCHYARD/Transformation)
2. [Dozer Transformer Documentation](https://docs.jboss.org/author/display/SWITCHYARD/Dozer+Transformer)
3. [Dozer Documentation](http://dozer.sourceforge.net/)
