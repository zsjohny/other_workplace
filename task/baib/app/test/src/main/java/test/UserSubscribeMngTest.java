package test;

import java.util.HashMap;
import java.util.Map;

/**
 * 预约后台管理测试
 * 
 * @author jiaming.zhu
 * @version $Id: UserSubscribeMngTest.java, v 0.1 2016年9月13日 下午3:46:06 zjm Exp $
 */
public class UserSubscribeMngTest {

    public static void main(String[] args) {

        String url = Config.localUrl + "mng/getUserSubscribeList.do";

        Map<String, String> params = new HashMap<String, String>();

        params.put("cStatus", "accpt");
        //        params.put("type", "GZ");

        String response = HttpUtils.URLPost(url, params, "UTF-8");

        System.out.println(response);

    }
}
