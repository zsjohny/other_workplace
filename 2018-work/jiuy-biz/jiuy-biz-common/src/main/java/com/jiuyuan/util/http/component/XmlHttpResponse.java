package com.jiuyuan.util.http.component;

import org.apache.http.Header;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

public class XmlHttpResponse extends CachedHttpResponse {

    private Document document;

    public XmlHttpResponse(String uri, int statusCode, byte[] body, String charset, Header[] headers) {
        super(uri, statusCode, body, charset, headers);
    }

    public XmlHttpResponse(CachedHttpResponse httpResponse) {
        this(httpResponse.getUri(), httpResponse.getStatusCode(), httpResponse.getBody(), httpResponse.getCharset(),
            httpResponse.getHeaders());
    }

    public Document getResponseXml() throws DocumentException {
        if (document == null) {
            document = DocumentHelper.parseText(getResponseText());
        }
        return document;
    }

}