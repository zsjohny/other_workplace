package com.finace.miscroservice.commons.handler;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

import static com.finace.miscroservice.commons.auth.XSSInjectInterceptor.xssClean;


/**
 * Xss的过滤拦截request
 */
public class XssHttpServletRequestWrapperHandler extends HttpServletRequestWrapper {
    private HttpServletRequest orgRequest;


    public XssHttpServletRequestWrapperHandler(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    /**
     * json 参数获取 requestBody
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        String result = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(orgRequest.getInputStream()))) {
            String line = "";
            StringBuilder lines = new StringBuilder();
            while ((line = br.readLine()) != null) {
                lines.append(xssClean(orgRequest,line));
            }
            result = lines.toString();
        }

        return new WrappedServletInputStream(new ByteArrayInputStream(result.getBytes()));
    }

    @Override
    public String getParameter(String name) {
        return xssClean(orgRequest,super.getParameter(name));
    }


    @Override
    public String[] getParameterValues(String name) {

        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = xssClean(orgRequest, arr[i]);
            }
        }
        return arr;
    }

    @Override
    public String getQueryString() {
        return xssClean(orgRequest, super.getQueryString());
    }

    @Override
    public String getHeader(String name) {
        return xssClean(orgRequest, super.getHeader(name));
    }


    private class WrappedServletInputStream extends ServletInputStream {
        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        private InputStream stream;

        public WrappedServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}