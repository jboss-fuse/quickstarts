package org.switchyard.quickstarts.validate.xml;

import java.io.StringReader;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.switchyard.validate.ValidationFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public final class Transformer {

    @org.switchyard.annotations.Transformer
    public Node transformValidationFailureExceptionToNode(ValidationFailureException from) {
        StringBuilder buf = new StringBuilder()
                .append("<soap:Fault xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n")
                .append("\t<faultcode>soap:Server</faultcode>\n")
                .append("\t<faultstring>\n")
                .append("\t\t>>>>> Content validation failed!!! (transformed to SOAP Fault)\n")
                .append("\t\t>>>>> Detail: [").append(from.getValidationResult().getDetail()).append("]\n")
                .append("\t</faultstring>\n")
                .append("</soap:Fault>");
        DOMResult dom = new DOMResult();
        try {
            TransformerFactory.newInstance().newTransformer().transform(
                new StreamSource(new StringReader(buf.toString())), dom);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ((Document) dom.getNode()).getDocumentElement();
    }

}
