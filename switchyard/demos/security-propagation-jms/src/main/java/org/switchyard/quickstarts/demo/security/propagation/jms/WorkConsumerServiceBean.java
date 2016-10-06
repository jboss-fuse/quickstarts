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
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

/**
 * The implementation of WorkConsumerService. It receives a Work command via SOAP request
 * and forward it into JMS queue via StoreService. Then StoreService will return back a reply
 * message retrieved from replyTo JMS queue.
 */
@Service(WorkService.class)
public class WorkConsumerServiceBean implements WorkService {

    private static final Logger LOGGER = Logger.getLogger(WorkConsumerServiceBean.class);
    private static final String MSG_BEFORE = ":: WorkConsumerService :: Received work command => %s (caller principal=%s, in roles? 'friend'=%s 'enemy'=%s) :: Forwarding to StoreService";
    private static final String MSG_AFTER = ":: WorkConsumerService :: Received workAck command => %s (Received=%s, caller principal=%s, in roles? 'friend'=%s 'enemy'=%s)";

    @Inject
    private Exchange exchange;

    @Inject @Reference("StoreService")
    private WorkService _storeService;

    @Override
    public WorkAck doWork(Work work) {
        String cmd = work.getCommand();
        ExchangeSecurity es = exchange.getSecurity();
        String msg = String.format(MSG_BEFORE, cmd, es.getCallerPrincipal(), es.isCallerInRole("friend"), es.isCallerInRole("enemy"));
        LOGGER.info(msg);
        // Forward a Work command into JMS queue
        
        WorkAck ack = _storeService.doWork(work);
        msg = String.format(MSG_AFTER, ack.getCommand(), ack.isReceived(), es.getCallerPrincipal(), es.isCallerInRole("friend"), es.isCallerInRole("enemy"));
        if (ack.isReceived()) {
            LOGGER.info(msg);
        } else {
            // received=false means that WorkProviderService couldn't find the caller is in "friend" role
            LOGGER.error(msg);
        }
        return ack;
    }

}
