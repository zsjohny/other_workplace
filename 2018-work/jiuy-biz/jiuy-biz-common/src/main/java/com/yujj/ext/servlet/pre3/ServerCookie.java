package com.yujj.ext.servlet.pre3;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;

/**
 * Copied and modified from tomcat source code.
 */
public class ServerCookie {

    private static final String OLD_COOKIE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";

    private static final ThreadLocal<DateFormat> OLD_COOKIE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            DateFormat df = new SimpleDateFormat(OLD_COOKIE_PATTERN, Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            return df;
        }
    };

    private static final String ancientDate = OLD_COOKIE_FORMAT.get().format(new Date(10000));

    public static String buildCookieHeaderValue(Cookie cookie, boolean isHttpOnly) {
        return buildCookieHeaderValue(cookie.getVersion(), cookie.getName(), cookie.getValue(), cookie.getPath(),
            cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(), isHttpOnly);
    }

    public static String buildCookieHeaderValue(int version, String name, String value, String path, String domain,
                                               String comment, int maxAge, boolean isSecure, boolean isHttpOnly) {
        StringBuffer buf = new StringBuffer();
        // Servlet implementation checks name
        buf.append(name);
        buf.append("=");
        // Servlet implementation does not check anything else

        /*
         * The spec allows some latitude on when to send the version attribute with a Set-Cookie header. To be nice to
         * clients, we'll make sure the version attribute is first. That means checking the various things that can
         * cause us to switch to a v1 cookie first. Note that by checking for tokens we will also throw an exception if
         * a control character is encountered.
         */
        // Start by using the version we were asked for
        int newVersion = version;

        // If it is v0, check if we need to switch
        if (newVersion == 0 &&
            (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(value) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 &&
                CookieSupport.isV0Token(value))) {
            // HTTP token in value - need to use v1
            newVersion = 1;
        }

        if (newVersion == 0 && comment != null) {
            // Using a comment makes it a v1 cookie
            newVersion = 1;
        }

        if (newVersion == 0 &&
            (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(path) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 &&
                CookieSupport.isV0Token(path))) {
            // HTTP token in path - need to use v1
            newVersion = 1;
        }

        if (newVersion == 0 &&
            (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(domain) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 &&
                CookieSupport.isV0Token(domain))) {
            // HTTP token in domain - need to use v1
            newVersion = 1;
        }

        // Now build the cookie header
        // Value
        maybeQuote(buf, value);
        // Add version 1 specific information
        if (newVersion == 1) {
            // Version=1 ... required
            buf.append("; Version=1");

            // Comment=comment
            if (comment != null) {
                buf.append("; Comment=");
                maybeQuote(buf, comment);
            }
        }

        // Add domain information, if present
        if (domain != null) {
            buf.append("; Domain=");
            maybeQuote(buf, domain);
        }

        // Max-Age=secs ... or use old "Expires" format
        if (maxAge >= 0) {
            if (newVersion > 0) {
                buf.append("; Max-Age=");
                buf.append(maxAge);
            }
            // IE6, IE7 and possibly other browsers don't understand Max-Age.
            // They do understand Expires, even with V1 cookies!
            if (newVersion == 0 || CookieSupport.ALWAYS_ADD_EXPIRES) {
                // Wdy, DD-Mon-YY HH:MM:SS GMT ( Expires Netscape format )
                buf.append("; Expires=");
                // To expire immediately we need to set the time in past
                if (maxAge == 0) {
                    buf.append(ancientDate);
                } else {
                    OLD_COOKIE_FORMAT.get().format(new Date(System.currentTimeMillis() + maxAge * 1000L), buf,
                        new FieldPosition(0));
                }
            }
        }

        // Path=path
        if (path != null) {
            buf.append("; Path=");
            maybeQuote(buf, path);
        }

        // Secure
        if (isSecure) {
            buf.append("; Secure");
        }

        // HttpOnly
        if (isHttpOnly) {
            buf.append("; HttpOnly");
        }

        return buf.toString();
    }

    /**
     * Quotes values if required.
     */
    private static void maybeQuote(StringBuffer buf, String value) {
        if (value == null || value.length() == 0) {
            buf.append("\"\"");
        } else if (CookieSupport.alreadyQuoted(value)) {
            buf.append('"');
            buf.append(escapeDoubleQuotes(value, 1, value.length() - 1));
            buf.append('"');
        } else if (CookieSupport.isHttpToken(value) && !CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 ||
            CookieSupport.isV0Token(value) && CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0) {
            buf.append('"');
            buf.append(escapeDoubleQuotes(value, 0, value.length()));
            buf.append('"');
        } else {
            buf.append(value);
        }
    }

    /**
     * Escapes any double quotes in the given string.
     * 
     * @param s the input string
     * @param beginIndex start index inclusive
     * @param endIndex exclusive
     * @return The (possibly) escaped string
     */
    private static String escapeDoubleQuotes(String s, int beginIndex, int endIndex) {

        if (s == null || s.length() == 0 || s.indexOf('"') == -1) {
            return s;
        }

        StringBuffer b = new StringBuffer();
        for (int i = beginIndex; i < endIndex; i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                b.append(c);
                // ignore the character after an escape, just append it
                if (++i >= endIndex) {
                    throw new IllegalArgumentException("Invalid escape character in cookie value.");
                }
                b.append(s.charAt(i));
            } else if (c == '"') {
                b.append('\\').append('"');
            } else {
                b.append(c);
            }
        }

        return b.toString();
    }
}
