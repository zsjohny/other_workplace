package org.dream.utils.quota.msg;

/**
 * @author Boyce
 *         2016年8月9日 下午3:37:19
 */
public class XtraderLoginReq extends AbstractXtraderQuota {
    {
        wAssitantCmd = 2;
        MsgType = "UserLoginReq";


    }


    public String UserName;
    public String Password;
    public String AuthCode;


}
