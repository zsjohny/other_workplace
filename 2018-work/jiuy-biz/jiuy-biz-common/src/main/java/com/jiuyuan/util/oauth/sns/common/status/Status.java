package com.jiuyuan.util.oauth.sns.common.status;

import org.apache.commons.lang3.StringUtils;

public class Status implements IStatus {

    protected String title;

    protected String status;

    protected String link;

    public Status(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public Status(String title, String status, String link) {
        this.title = title;
        this.status = status;
        this.link = link;
    }

    @Override
    public String getTitle(int limit) {
        return StringUtils.abbreviate(title, limit);
    }

    @Override
    public String getContent(int limit) {
        if (StringUtils.isNotBlank(link)) {
            limit = limit - link.length() - 1;
        }
        return StringUtils.abbreviate(status, limit) + " " + link;
    }
}
