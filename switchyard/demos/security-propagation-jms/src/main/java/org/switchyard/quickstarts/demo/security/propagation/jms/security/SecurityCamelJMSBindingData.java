package org.switchyard.quickstarts.demo.security.propagation.jms.security;

import java.util.Set;

import org.apache.camel.Message;
import org.jboss.logging.Logger;
import org.switchyard.component.camel.common.composer.CamelBindingData;
import org.switchyard.component.common.composer.SecurityBindingData;
import org.switchyard.security.credential.Credential;

/**
 *  SecurityBindingData implementation for camel-jms endpoint which extracts original Work object
 *  and security credentials from the CredentialedWork object.
 */
public class SecurityCamelJMSBindingData extends CamelBindingData implements SecurityBindingData {

    public static final String HEADER_SECURITY_CREDENTIALS = "org.switchyard.quickstarts.demo.security.propagation.jms.SecurityCredentials";

    private static final Logger _log = Logger.getLogger(SecurityCamelJMSBindingData.class);

    public SecurityCamelJMSBindingData(Message message) {
        super(message);
    }

    @Override
    public Set<Credential> extractCredentials() {
        Set<Credential> credentials = null;
        Object body = getMessage().getBody();
        if (body instanceof CredentialedWork) {
            _log.info("Security Credential is found in camel message - extracting.");
            CredentialedWork sw = (CredentialedWork)body;
            credentials = sw.getCredentials();
            getMessage().setBody(sw.getWork());
        }
        return credentials;
    }

}
