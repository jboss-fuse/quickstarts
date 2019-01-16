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
package org.jboss.quickstarts.fuse.sap_qidoclist_destination_fuse;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SapTransactionalIDocListDestinationEndpoint;
import org.fusesource.camel.component.sap.model.idoc.DocumentList;
import org.fusesource.camel.component.sap.model.idoc.Document;
import org.fusesource.camel.component.sap.model.idoc.Segment;

/**
 * A {@link CreateRubbleIDocList} is a processor bean which builds a {@link DocumentList} containing 
 * FLCUSTOMER_CREATEFROMDATA01 IDoc documents to create a flight customer records in SAP for the Rubbles.
 * 
 * @author William Collins (punkhornsw@gmail.com)
 *
 */
public class CreateRubbleIDocList {

	public void createRequest(Exchange exchange) throws Exception {

        // Create document list
		SapTransactionalIDocListDestinationEndpoint endpoint = exchange.getContext().getEndpoint("sap-idoclist-destination:quickstartDest:FLCUSTOMER_CREATEFROMDATA01", SapTransactionalIDocListDestinationEndpoint.class);
		DocumentList documentList = endpoint.createDocumentList();
		
		//
		// Barney Rubble IDoc
		//
		
		// Create and add document to list.
        Document barneyDocument = documentList.add();
        
        // Initialize documents transmission parameters
		barneyDocument.setMessageType("FLCUSTOMER_CREATEFROMDATA");
		barneyDocument.setRecipientPartnerNumber("QUICKCLNT");
		barneyDocument.setRecipientPartnerType("LS");
		barneyDocument.setSenderPartnerNumber("QUICKSTART");
		barneyDocument.setSenderPartnerType("LS");
		
		// Retrieve document segments.
		Segment rootSegment = barneyDocument.getRootSegment();
		Segment headerSegment = rootSegment.getChildren("E1SCU_CRE").add();
		Segment newCustomerSegment = headerSegment.getChildren("E1BPSCUNEW").add();
		
		// Fill in New Customer Info
		newCustomerSegment.put("CUSTNAME", "Barney Rubble");
		newCustomerSegment.put("FORM", "Mr.");
		newCustomerSegment.put("STREET", "123 Pebble Road");
		newCustomerSegment.put("POSTCODE", "98765");
		newCustomerSegment.put("CITY", "Flagstone");
		newCustomerSegment.put("COUNTR", "US");
		newCustomerSegment.put("PHONE", "800-555-1313");
		newCustomerSegment.put("EMAIL", "barney@flagstone.com");
		newCustomerSegment.put("CUSTTYPE", "P");
		newCustomerSegment.put("DISCOUNT", "005");
		newCustomerSegment.put("LANGU", "E");
		
		//
		// Betty Rubble IDoc
		//
		
		// Create and add document to list.
        Document bettyDocument = documentList.add();
        
        // Initialize documents transmission parameters
        bettyDocument.setMessageType("FLCUSTOMER_CREATEFROMDATA");
        bettyDocument.setRecipientPartnerNumber("QUICKCLNT");
        bettyDocument.setRecipientPartnerType("LS");
        bettyDocument.setSenderPartnerNumber("QUICKSTART");
        bettyDocument.setSenderPartnerType("LS");
		
		// Retrieve document segments.
		rootSegment = bettyDocument.getRootSegment();
		headerSegment = rootSegment.getChildren("E1SCU_CRE").add();
		newCustomerSegment = headerSegment.getChildren("E1BPSCUNEW").add();
		
		// Fill in New Customer Info
		newCustomerSegment.put("CUSTNAME", "Betty Rubble");
		newCustomerSegment.put("FORM", "Mrs.");
		newCustomerSegment.put("STREET", "123 Pebble Road");
		newCustomerSegment.put("POSTCODE", "98765");
		newCustomerSegment.put("CITY", "Flagstone");
		newCustomerSegment.put("COUNTR", "US");
		newCustomerSegment.put("PHONE", "800-555-1313");
		newCustomerSegment.put("EMAIL", "betty@flagstone.com");
		newCustomerSegment.put("CUSTTYPE", "P");
		newCustomerSegment.put("DISCOUNT", "005");
		newCustomerSegment.put("LANGU", "E");
		

        // Set the request in in the body of the exchange's message.
		exchange.getIn().setBody(documentList);
	}

}
