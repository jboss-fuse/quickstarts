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
package org.jboss.quickstarts.fuse.sap_idoc_destination_standalone;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SapTransactionalIDocDestinationEndpoint;
import org.fusesource.camel.component.sap.model.idoc.Document;
import org.fusesource.camel.component.sap.model.idoc.Segment;

/**
 * A {@link CreateWilmaIDoc} is a processor bean which builds a FLCUSTOMER_CREATEFROMDATA01 IDoc 
 * to create a flight customer record in SAP for 'Wilma Flintstone'.
 * 
 * @author William Collins (punkhornsw@gmail.com)
 *
 */
public class CreateWilmaIDoc {

	public void createIDoc(Exchange exchange) throws Exception {

        // Create document and initialize transmission parameters
		SapTransactionalIDocDestinationEndpoint endpoint = exchange.getContext().getEndpoint("sap-idoc-destination:quickstartDest:FLCUSTOMER_CREATEFROMDATA01", SapTransactionalIDocDestinationEndpoint.class);
        Document document = endpoint.createDocument();
		document.setMessageType("FLCUSTOMER_CREATEFROMDATA");
		document.setRecipientPartnerNumber("QUICKCLNT");
		document.setRecipientPartnerType("LS");
		document.setSenderPartnerNumber("QUICKSTART");
		document.setSenderPartnerType("LS");
		
		// Retrieve document segments.
		Segment rootSegment = document.getRootSegment();
		Segment headerSegment = rootSegment.getChildren("E1SCU_CRE").add();
		Segment newCustomerSegment = headerSegment.getChildren("E1BPSCUNEW").add();
		
		// Fill in New Customer Info
		newCustomerSegment.put("CUSTNAME", "Wilma Flintstone");
		newCustomerSegment.put("FORM", "Mrs.");
		newCustomerSegment.put("STREET", "123 Rubble Lane");
		newCustomerSegment.put("POSTCODE", "01234");
		newCustomerSegment.put("CITY", "Bedrock");
		newCustomerSegment.put("COUNTR", "US");
		newCustomerSegment.put("PHONE", "800-555-1212");
		newCustomerSegment.put("EMAIL", "wilma@bedrock.com");
		newCustomerSegment.put("CUSTTYPE", "P");
		newCustomerSegment.put("DISCOUNT", "005");
		newCustomerSegment.put("LANGU", "E");
		
		// Set the request in in the body of the exchange's message.
		exchange.getIn().setBody(document);
	}

}
