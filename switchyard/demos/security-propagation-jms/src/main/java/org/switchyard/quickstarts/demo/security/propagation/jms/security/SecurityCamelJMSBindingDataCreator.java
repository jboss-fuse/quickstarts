/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.demo.security.propagation.jms.security;

import org.apache.camel.Message;
import org.jboss.logging.Logger;
import org.switchyard.component.camel.common.composer.BindingDataCreator;

/**
 * BindingDataCreator service implementation for propagating security credentials over camel-jms binding.
 * The META-INF/services/org/switchyard/component/camel/JmsQueueEndpoint file enables this class to be
 * loaded by SwitchYard camel component, and create a SecurityCamelJMSBindingData.
 */
public class SecurityCamelJMSBindingDataCreator implements BindingDataCreator<SecurityCamelJMSBindingData> {

    private static final Logger _log = Logger.getLogger(SecurityCamelJMSBindingDataCreator.class);
    
    @Override
    public SecurityCamelJMSBindingData createBindingData(Message message) {
        _log.info("Creating " + SecurityCamelJMSBindingData.class.getName());
        return new SecurityCamelJMSBindingData(message);
    }

}
