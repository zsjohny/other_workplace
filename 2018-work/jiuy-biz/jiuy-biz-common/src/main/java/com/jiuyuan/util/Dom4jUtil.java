package com.jiuyuan.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.xml.sax.SAXException;

public class Dom4jUtil {

    /**
     * <p>
     * <code>parseText</code> parses the given text as an XML document and returns the newly created Document.
     * </p>
     * 
     * @param text the XML text to be parsed
     * @return a newly parsed Document
     * @throws DocumentException if the document could not be parsed
     */
    public static Document parseText(String text) throws ParserConfigurationException,
        UnsupportedEncodingException, SAXException, IOException {
        Document result = null;

        String encoding = getEncoding(text);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
//        factory.setFeature("http://xml.org/sax/features/namespaces", false);
//        factory.setFeature("http://xml.org/sax/features/validation", false);
//        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
//        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(text.getBytes(encoding)));

        DOMReader reader = new DOMReader();
        result = reader.read(doc);

        // if the XML parser doesn't provide a way to retrieve the encoding,
        // specify it manually
        if (result.getXMLEncoding() == null) {
            result.setXMLEncoding(encoding);
        }

        return result;
    }

    public static String getEncoding(String text) {
        String result = null;

        String xml = text.trim();

        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");

            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();

                if ("encoding".equals(token)) {
                    if (tokens.hasMoreTokens()) {
                        result = tokens.nextToken();
                    }

                    break;
                }
            }
        }

        return result;
    }
}
