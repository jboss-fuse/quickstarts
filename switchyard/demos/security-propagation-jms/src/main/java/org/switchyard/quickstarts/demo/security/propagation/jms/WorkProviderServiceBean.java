/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.switchyard.quickstarts.demo.security.propagation.jms;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.switchyard.Exchange;
import org.switchyard.ExchangeSecurity;
import org.switchyard.component.bean.Service;

/**
 * The implementation of WorkProviderService. It receives a JMS message sent by WorkProviderService,
 * validate the security condition and send back the WorkAck object into replyTo JMS queue.
 */
@Service(value=WorkService.class, name="WorkProviderService")
public class WorkProviderServiceBean implements WorkService {

    private static final Logger LOGGER = Logger.getLogger(WorkProviderServiceBean.class);
    private static final String MSG_BEFORE = ":: WorkProviderService :: Received work command => %s (caller principal=%s, in roles? 'friend'=%s 'enemy'=%s) :: Creating a WorkAck";
    private static final String MSG_AFTER = ":: WorkProviderService :: Returning workAck command => %s (Received=%s, caller principal=%s, in roles? 'friend'=%s 'enemy'=%s)";

    @Inject
    private Exchange exchange;

    @Override
    public WorkAck doWork(Work work) {
        String cmd = work.getCommand();
        ExchangeSecurity es = exchange.getSecurity();
        String msg = String.format(MSG_BEFORE, cmd, es.getCallerPrincipal(), es.isCallerInRole("friend"), es.isCallerInRole("enemy"));
        LOGGER.info(msg);
        WorkAck ack = new WorkAck().setCommand(cmd);
        if (es.isCallerInRole("friend")) {
            // Set received=true only when the caller is in "friend" role
            ack.setReceived(true);
        } else {
            // Otherwise set received=false
            ack.setReceived(false);
        }
        msg = String.format(MSG_AFTER, cmd, ack.isReceived(), es.getCallerPrincipal(), es.isCallerInRole("friend"), es.isCallerInRole("enemy"));
        if (ack.isReceived()) {
            LOGGER.info(msg);
        } else {
            // And output as a error message if received=false
            LOGGER.error(msg);
        }
        return ack;
    }

}
