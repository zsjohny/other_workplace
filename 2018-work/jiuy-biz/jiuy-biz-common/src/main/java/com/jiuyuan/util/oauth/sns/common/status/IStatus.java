package com.jiuyuan.util.oauth.sns.common.status;

public interface IStatus {

    String getTitle(int limit);

    String getContent(int limit);
}
