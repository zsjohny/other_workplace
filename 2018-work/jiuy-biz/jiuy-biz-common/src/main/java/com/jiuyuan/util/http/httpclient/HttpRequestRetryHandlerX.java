package com.jiuyuan.util.http.httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

@SuppressWarnings("deprecation")
public class HttpRequestRetryHandlerX implements HttpRequestRetryHandler {

    private static Class<?> SSL_EXCEPTION = null;

    static {
        try {
            SSL_EXCEPTION = Class.forName("javax.net.ssl.SSLException");
        } catch (ClassNotFoundException e) {
            SSL_EXCEPTION = null; // just to eliminate a warning of findbugs.
        }
    }

    /** the number of times a method will be retried */
    private final int retryCount;

    /** Whether or not methods that have successfully sent their request will be retried */
    private final boolean requestSentRetryEnabled;

    /** Whether or not methods that timed out will be retried */
    private final boolean timeoutRetryEnabled;

    public HttpRequestRetryHandlerX(int retryCount, boolean requestSentRetryEnabled, boolean timeoutRetryEnabled) {
        super();
        this.retryCount = retryCount;
        this.requestSentRetryEnabled = requestSentRetryEnabled;
        this.timeoutRetryEnabled = timeoutRetryEnabled;
    }

    /**
     * Default constructor, returns a handler that doesn't retry
     */
    public HttpRequestRetryHandlerX() {
        this(0, false, false);
    }

    /**
     * Used <code>retryCount</code> and <code>requestSentRetryEnabled</code> to determine if the given method should be
     * retried.
     */
    public boolean retryRequest(final IOException exception, int executionCount, final HttpContext context) {
        if (exception == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }

        HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpParams params = request.getParams();

        if (executionCount > getRetryCount(params)) {
            // Do not retry if over max retry count
            return false;
        }
        if (exception instanceof InterruptedIOException) {
            if (exception instanceof ConnectTimeoutException || exception instanceof SocketTimeoutException) {
                // Timeout
                if (!isTimeoutRetryEnabled(params)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            return false;
        }
        if (exception instanceof ConnectException) {
            // Connection refused
            return false;
        }
        if (SSL_EXCEPTION != null && SSL_EXCEPTION.isInstance(exception)) {
            // SSL handshake exception
            return false;
        }

        if (requestIsAborted(request)) {
            return false;
        }

        if (handleAsIdempotent(request)) {
            // Retry if the request is considered idempotent
            return true;
        }

        Boolean b = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
        boolean sent = (b != null && b.booleanValue());
        if (!sent || isRequestSentRetryEnabled(params)) {
            // Retry if the request has not been sent fully or
            // if it's OK to retry methods that have been sent
            return true;
        }

        // otherwise do not retry
        return false;
    }

    /**
     * @return <code>true</code> if this handler will retry methods that have successfully sent their request,
     *         <code>false</code> otherwise
     */
    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public boolean isRequestSentRetryEnabled(HttpParams params) {
        Object obj = params.getParameter(ClientPNamesX.REQUEST_SNET_RETRY_ENABLED);
        return ParamConverter.getBooleanValue(obj, this.requestSentRetryEnabled);
    }

    public boolean isTimeoutRetryEnabled(HttpParams params) {
        Object obj = params.getParameter(ClientPNamesX.TIMEOUT_RETRY_ENABLED);
        return ParamConverter.getBooleanValue(obj, this.timeoutRetryEnabled);
    }

    /**
     * @return the maximum number of times a method will be retried
     */
    public int getRetryCount() {
        return retryCount;
    }

    public int getRetryCount(HttpParams params) {
        Object obj = params.getParameter(ClientPNamesX.RETRY_COUNT);
        return ParamConverter.getIntValue(obj, this.retryCount);
    }

    /**
     * @since 4.2
     */
    protected boolean handleAsIdempotent(final HttpRequest request) {
        return !(request instanceof HttpEntityEnclosingRequest);
    }

    /**
     * @since 4.2
     */
    protected boolean requestIsAborted(final HttpRequest request) {
        HttpRequest req = request;
        if (request instanceof RequestWrapper) { // does not forward request to original
            req = ((RequestWrapper) request).getOriginal();
        }
        return (req instanceof HttpUriRequest && ((HttpUriRequest) req).isAborted());
    }
}
