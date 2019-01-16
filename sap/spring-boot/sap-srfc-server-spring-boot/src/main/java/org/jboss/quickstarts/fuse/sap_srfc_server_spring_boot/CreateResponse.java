/**
 * Copyright 2016 Red Hat, Inc.
 * 
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.jboss.quickstarts.fuse.sap_srfc_server_spring_boot;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SapSynchronousRfcServerEndpoint;
import org.fusesource.camel.component.sap.model.rfc.Structure;
import org.fusesource.camel.component.sap.model.rfc.Table;

/**
 * A {@link CreateResponse} is a processor bean which builds a response for the GetList method of the FlightCustomer BAPI
 * and sets that response in the body of the exchange's message.
 * 
 * @author William Collins (punkhornsw@gmail.com)
 *
 */
public class CreateResponse {

	public void createResponse(Exchange exchange) throws Exception {

		// Create a response message from the endpoint to the GetList method of the FlightCustomer BAPI
		SapSynchronousRfcServerEndpoint endpoint = exchange.getContext().getEndpoint("sap-srfc-server:quickstartServer:BAPI_FLCUST_GETLIST", SapSynchronousRfcServerEndpoint.class);
		Structure response = endpoint.createResponse();
		
		// Add customer records to the response.
		Table<Structure> customerList = response.get("CUSTOMER_LIST", Table.class);
		
		// Fred Record
		Structure fred = customerList.add();
		fred.put("CUSTOMERID", "00000000");
		fred.put("CUSTNAME", "Fred Flintstone");
		fred.put("FORM", "Mr.");
		fred.put("STREET", "123 Rubble Lane");
		fred.put("POSTCODE", "01234");
		fred.put("CITY", "Bedrock");
		fred.put("COUNTR", "US");
		fred.put("PHONE", "800-555-1212");
		fred.put("EMAIL", "fred@bedrock.com");
		
		// Barney Record
		Structure barney = customerList.add();
		barney.put("CUSTOMERID", "00000001");
		barney.put("CUSTNAME", "Barney Rubble");
		barney.put("FORM", "Mr.");
		barney.put("STREET", "123 Pebble Road");
		barney.put("POSTCODE", "98765");
		barney.put("CITY", "Flagstone");
		barney.put("COUNTR", "US");
		barney.put("PHONE", "800-555-1313");
		barney.put("EMAIL", "barney@flagstone.com");

		// Wilma Record
		Structure wilma = customerList.add();
		wilma.put("CUSTOMERID", "00000002");
		wilma.put("CUSTNAME", "Wilma Flintstone");
		wilma.put("FORM", "Mrs.");
		wilma.put("STREET", "123 Rubble Lane");
		wilma.put("POSTCODE", "01234");
		wilma.put("CITY", "Bedrock");
		wilma.put("COUNTR", "US");
		wilma.put("PHONE", "800-555-1212");
		wilma.put("EMAIL", "wilma@bedrock.com");

		// Betty Record
		Structure betty = customerList.add();
		betty.put("CUSTOMERID", "00000003");
		betty.put("CUSTNAME", "Betty Rubble");
		betty.put("FORM", "Mrs.");
		betty.put("STREET", "123 Pebble Road");
		betty.put("POSTCODE", "98765");
		betty.put("CITY", "Flagstone");
		betty.put("COUNTR", "US");
		betty.put("PHONE", "800-555-1313");
		betty.put("EMAIL", "betty@flagstone.com");
		
		// Add Return status to response
		Table<Structure> rtn = response.get("RETURN", Table.class);
		Structure success = rtn.add();
		success.put("TYPE", "S");
		success.put("ID", "BC_IBF");
		success.put("MESSAGE", "Method was executed successfully");
		success.put("LOG_MSG_NO", "000000");
		

		// Set the response in in the body of the exchange's message.
		exchange.getIn().setBody(response);
	}

}
