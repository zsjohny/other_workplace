package com.jiuyuan.util.http.component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Header;
import org.apache.http.HttpStatus;

public class CachedHttpResponse {

    protected String uri;

    protected int statusCode;

    protected byte[] body;

    protected String charset;

    protected String responseText;

    protected Header[] headers;

    public CachedHttpResponse(String uri, int statusCode, byte[] body, String charset, Header[] headers) {
        this.uri = uri;
        this.statusCode = statusCode;
        this.body = body;
        this.charset = charset;
        this.headers = headers;
    }

    public String getUri() {
        return uri;
    }

    public boolean isStatusCodeOK() {
        return this.statusCode == HttpStatus.SC_OK;
    }

    public boolean isStatusCodeFine() {
        return this.statusCode >= 200 && this.statusCode < 300;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getCharset() {
        return charset;
    }

    public synchronized String getResponseText() {
        if (responseText == null) {
            try {
                responseText = new String(body, charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return responseText;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public Header[] getHeaders(String name) {
        ArrayList<Header> headersFound = new ArrayList<Header>();

        if (headers != null) {
            for (Header header : headers) {
                if (header.getName().equalsIgnoreCase(name)) {
                    headersFound.add(header);
                }
            }
        }

        return headersFound.toArray(new Header[headersFound.size()]);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("uri", getUri()).append("statusCode",
            getStatusCode()).append("responseText", getResponseText()).build();
    }
}