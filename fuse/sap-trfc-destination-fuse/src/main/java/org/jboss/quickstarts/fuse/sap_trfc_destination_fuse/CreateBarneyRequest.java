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
package org.jboss.quickstarts.fuse.sap_trfc_destination_fuse;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SapTransactionalRfcDestinationEndpoint;
import org.fusesource.camel.component.sap.model.rfc.Structure;

/**
 * A {@link CreateBarneyRequest} is a processor bean which builds a request to the CreateFromData method of the FlightCustomer BAPI
 * to create a flight customer record in SAP for 'Barney Rubble'.
 * 
 * @author William Collins (punkhornsw@gmail.com)
 *
 */
public class CreateBarneyRequest {

	public void createRequest(Exchange exchange) throws Exception {

		// Create a request message from the endpoint to the CreateFromData method of the FlightCustomer BAPI
		SapTransactionalRfcDestinationEndpoint endpoint = exchange.getContext().getEndpoint("sap-trfc-destination:quickstartDest:BAPI_FLCUST_CREATEFROMDATA", SapTransactionalRfcDestinationEndpoint.class);
		Structure request = endpoint.createRequest();
		
		// Add customer data to the request for customer record.
		Structure customerData = request.get("CUSTOMER_DATA", Structure.class);
		customerData.put("CUSTNAME", "Barney Rubble");
		customerData.put("FORM", "Mr.");
		customerData.put("STREET", "123 Pebble Road");
		customerData.put("POSTCODE", "98765");
		customerData.put("CITY", "Flagstone");
		customerData.put("COUNTR", "US");
		customerData.put("PHONE", "800-555-1313");
		customerData.put("EMAIL", "barney@flagstone.com");
		customerData.put("CUSTTYPE", "P");
		customerData.put("DISCOUNT", "005");
		customerData.put("LANGU", "E");
		
		// Set the request in in the body of the exchange's message.
		exchange.getIn().setBody(request);
	}

}
