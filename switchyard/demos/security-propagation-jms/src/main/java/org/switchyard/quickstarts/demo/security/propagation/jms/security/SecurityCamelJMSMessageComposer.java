package org.switchyard.quickstarts.demo.security.propagation.jms.security;

import org.jboss.logging.Logger;
import org.switchyard.Exchange;
import org.switchyard.component.camel.common.composer.CamelBindingData;
import org.switchyard.component.camel.common.composer.CamelMessageComposer;
import org.switchyard.component.common.composer.MessageComposer;
import org.switchyard.quickstarts.demo.security.propagation.jms.Work;
import org.switchyard.security.SecurityMetadata;
import org.switchyard.security.context.SecurityContext;
import org.switchyard.security.context.SecurityContextManager;

/**
 * The custom MessageComposer to extract security credentials from SwitchYard SecurityContext and
 * carry them through a JMS queue. As JMS property only accepts primitives and String,　it needs to
 * package them into JMS message body. This is why this creates a CredentialedWork object which
 * holds a pair of original Work object and security credentials. The SecurityCamelJMSBindingData
 * extracts this CredentialedWork object at the JMS consumer side.
 */
public class SecurityCamelJMSMessageComposer extends CamelMessageComposer implements MessageComposer<CamelBindingData> {

    private static final Logger _log = Logger.getLogger(SecurityCamelJMSMessageComposer.class);
    
    @Override
    public CamelBindingData decompose(Exchange exchange, CamelBindingData target) throws Exception {
        CamelBindingData answer = super.decompose(exchange, target);

        SecurityContextManager scm = new SecurityContextManager(SecurityMetadata.getServiceDomain(exchange));
        SecurityContext securityContext = scm.getContext(exchange);
        Object body = answer.getMessage().getBody();
        if (body instanceof Work && securityContext != null) {
            _log.info("SecurityContext is detected - storing credentials into camel message");
            answer.getMessage().setBody(new CredentialedWork((Work)body, securityContext.getCredentials()));
        }
        return answer;
    }
}
