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
package org.switchyard.quickstarts.transform.datamapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.camel.CamelContext;
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.common.io.Files;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Functional test for the transform-datamapper quickstart.
 * 
 */
@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        mixins = CDIMixIn.class)
public class FileBindingsTest  {
    private static final Logger LOGGER = Logger.getLogger(FileBindingsTest.class);
    
    private static final String INPUT_SOURCE_PATH = "src/test/resources/abc-order.xml";
    private static final String INPUT_DESTINATION_PATH = "target/input/abc-order.xml";
    private static final String OUTPUT_EXPECTED_PATH = "src/test/resources/xyz-order.json";
    private static final String OUTPUT_ACTUAL_PATH = "target/output/xyz-order.json";

    private CamelContext _camelContext;

    @Test
    public void testFile() throws Exception {
        File output = new File(OUTPUT_ACTUAL_PATH);
        if (output.exists()) {
            output.delete();
        }
        
        Files.copy(new File(INPUT_SOURCE_PATH), new File(INPUT_DESTINATION_PATH));
        for (int i=0; (!output.exists()) && i<10; i++) {
            LOGGER.info("Waiting for an output file to be written...");
            Thread.sleep(1000);
        }
        
        Assert.assertTrue("The output file '" + output.getPath() + "' was not found", output.exists());
        Assert.assertEquals(jsonUnprettyPrint(readFile(OUTPUT_EXPECTED_PATH))
                            , jsonUnprettyPrint(readFile(OUTPUT_ACTUAL_PATH)));
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

