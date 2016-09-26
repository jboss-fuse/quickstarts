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
package org.switchyard.quickstarts.camel.dozer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Exchange;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Functional test for the camel-dozer quickstart.
 * 
 */
@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML)
public class OrderServiceTest  {

    private SwitchYardTestKit _testKit;
    private CamelContext _camelContext;

    @ServiceOperation("OrderService.order")
    private Invoker _order;
    
    private static final String INPUT_FILEPATH = "src/test/resources/abc-order.xml";
    private static final String OUTPUT_FILEPATH = "src/test/resources/xyz-order.json";

    @Test
    public void testOrder() throws Exception {
        _testKit.removeService("StoreService");
        MockHandler mock = _testKit.registerInOnlyService("StoreService");
        
        _order.sendInOnly(readFile(INPUT_FILEPATH));
        
        LinkedBlockingQueue<Exchange> exchanges = mock.getMessages();
        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(jsonUnprettyPrint(readFile(OUTPUT_FILEPATH))
                , exchanges.take().getMessage().getContent(String.class));
    }
    
    private String readFile(String filePath) throws Exception {
        String content;
        FileInputStream fis = new FileInputStream(filePath);
        try {
            content = _camelContext.getTypeConverter().convertTo(String.class, fis);
        } finally {
            fis.close();
        }
        return content;
    }
    
    private String jsonUnprettyPrint(String jsonString) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        JsonNode node = mapper.readTree(jsonString);
        return node.toString();
    }
}

