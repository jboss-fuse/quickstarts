package org.switchyard.quickstarts.demo.security.propagation.jms.security;

import java.io.Serializable;
import java.util.Set;

import org.switchyard.quickstarts.demo.security.propagation.jms.Work;
import org.switchyard.security.credential.Credential;

/**
 * This CredentialedWork holds a pair of Work and security credentials.
 */
@SuppressWarnings("serial")
public class CredentialedWork implements Serializable {

    private Work _work;
    private Set<Credential> _credentials;

    protected CredentialedWork(Work work, Set<Credential> credentials) {
        _work = work;
        _credentials = credentials;
    }
    
    protected Work getWork() {
        return _work;
    }
    protected Set<Credential> getCredentials() {
        return _credentials;
    }
}
