package com.yujj.util.mail;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.util.CollectionUtil;


public class MailUtil {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private static final Pattern mailPattern = Pattern.compile("[\\d\\w.-]+@[\\d\\w.-]+\\.[\\d\\w.-]+");

    /** 163邮箱后缀 */
    public static final List<String> NETEASE_MAIL_SUFFIX = CollectionUtil.createList("163", "126", "yeah", "188");

    public static boolean isNeteaseMail(String email) {
        try {
            if (!isPossibleMail(email)) {
                return false;
            }
            String[] temp = email.split("@");
            String tempEmail = temp[1];
            temp = tempEmail.split("\\.");
            int len = temp.length;
            if (len >= 2) {
                tempEmail = temp[len - 2];
            }

            if (NETEASE_MAIL_SUFFIX.contains(tempEmail)) {
                return true;
            }
        } catch (Exception e) {
            // ignore
        }

        return false;
    }

    public static boolean isPossibleMail(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }
        return mailPattern.matcher(string.trim()).matches();
    }

    public static boolean authenticate(String smtpHost, String userNameOrMail, String password) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties);
        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
            transport.connect(smtpHost, userNameOrMail, password);
            return true;
        } catch (Exception e) {
            // Never log the password!
            log.error("Authentication failed. host: {}, userName: {}, password hash: {}", smtpHost, userNameOrMail,
                password.hashCode(), e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    // We can do nothing about it.
                    log.error("", e);
                }
            }
        }

        return false;
    }
}
