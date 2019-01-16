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
package org.jboss.quickstarts.fuse.sap_idoclist_destination_standalone;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SapTransactionalIDocListDestinationEndpoint;
import org.fusesource.camel.component.sap.model.idoc.DocumentList;
import org.fusesource.camel.component.sap.model.idoc.Document;
import org.fusesource.camel.component.sap.model.idoc.Segment;

/**
 * A {@link CreateFlintstoneIDocList} is a processor bean which builds a {@link DocumentList} containing 
 * FLCUSTOMER_CREATEFROMDATA01 IDoc documents to create a flight customer records in SAP for the Flintstones.
 * 
 * @author William Collins (punkhornsw@gmail.com)
 *
 */
public class CreateFlintstoneIDocList {

	public void createRequest(Exchange exchange) throws Exception {

        // Create document list
		SapTransactionalIDocListDestinationEndpoint endpoint = exchange.getContext().getEndpoint("sap-idoclist-destination:quickstartDest:FLCUSTOMER_CREATEFROMDATA01", SapTransactionalIDocListDestinationEndpoint.class);
		DocumentList documentList = endpoint.createDocumentList();
		
		//
		// Fred Flinstone IDoc
		//
		
		// Create and add document to list.
        Document fredDocument = documentList.add();
        
        // Initialize documents transmission parameters
		fredDocument.setMessageType("FLCUSTOMER_CREATEFROMDATA");
		fredDocument.setRecipientPartnerNumber("QUICKCLNT");
		fredDocument.setRecipientPartnerType("LS");
		fredDocument.setSenderPartnerNumber("QUICKSTART");
		fredDocument.setSenderPartnerType("LS");
		
		// Retrieve document segments.
		Segment rootSegment = fredDocument.getRootSegment();
		Segment headerSegment = rootSegment.getChildren("E1SCU_CRE").add();
		Segment newCustomerSegment = headerSegment.getChildren("E1BPSCUNEW").add();
		
		// Fill in New Customer Info
		newCustomerSegment.put("CUSTNAME", "Fred Flintstone");
		newCustomerSegment.put("FORM", "Mr.");
		newCustomerSegment.put("STREET", "123 Rubble Lane");
		newCustomerSegment.put("POSTCODE", "01234");
		newCustomerSegment.put("CITY", "Bedrock");
		newCustomerSegment.put("COUNTR", "US");
		newCustomerSegment.put("PHONE", "800-555-1212");
		newCustomerSegment.put("EMAIL", "fred@bedrock.com");
		newCustomerSegment.put("CUSTTYPE", "P");
		newCustomerSegment.put("DISCOUNT", "005");
		newCustomerSegment.put("LANGU", "E");
		
		//
		// Wilma Flinstone IDoc
		//
		
		// Create and add document to list.
        Document wilmaDocument = documentList.add();
        
        // Initialize documents transmission parameters
        wilmaDocument.setMessageType("FLCUSTOMER_CREATEFROMDATA");
        wilmaDocument.setRecipientPartnerNumber("QUICKCLNT");
        wilmaDocument.setRecipientPartnerType("LS");
        wilmaDocument.setSenderPartnerNumber("QUICKSTART");
        wilmaDocument.setSenderPartnerType("LS");
		
		// Retrieve document segments.
		rootSegment = wilmaDocument.getRootSegment();
		headerSegment = rootSegment.getChildren("E1SCU_CRE").add();
		newCustomerSegment = headerSegment.getChildren("E1BPSCUNEW").add();
		
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
		exchange.getIn().setBody(documentList);
	}

}
