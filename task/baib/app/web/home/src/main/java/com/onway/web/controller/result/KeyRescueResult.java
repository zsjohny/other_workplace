package com.onway.web.controller.result;

/**
 * 一键救援返回结果集
 * @author wenqiang.Wang
 * @version $Id: keyRescueResult.java, v 0.1 2016年12月28日 下午3:10:48 wenqiang.Wang Exp $
 */
public class KeyRescueResult extends JsonResult {
    private static final long serialVersionUID = 741231858441822688L;

    /**
     * @param bizSucc
     * @param errCode
     * @param message
     */
    public KeyRescueResult(boolean bizSucc, String errCode, String message) {
        super(bizSucc, message, message);
    }

    private String title;

    private String rescueUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRescueUrl() {
        return rescueUrl;
    }

    public void setRescueUrl(String rescueUrl) {
        this.rescueUrl = rescueUrl;
    }

}
