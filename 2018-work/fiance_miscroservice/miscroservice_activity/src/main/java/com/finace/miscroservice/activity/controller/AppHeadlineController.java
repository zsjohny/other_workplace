package com.finace.miscroservice.activity.controller;


import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import com.finace.miscroservice.activity.service.UserHeadlineChannelService;
import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 头条接口
 */
@InnerRestController
public class AppHeadlineController extends BaseController {
    private Log logger = Log.getInstance(AppHeadlineController.class);


    @Autowired
    private UserHeadlineChannelService userHeadlineChannelService;


    @RequestMapping(value = "ttandroid", method = RequestMethod.GET)
    public String ttandroid(String adid, String cid, String imei, String mac, String android_id,
                            String os, String timestamp, String convert_id, String callback) {
        try {
            logger.info("ttandroid渠道");
            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 保存头条回调信息
     *
     * @param adid
     * @param cid
     * @param imei
     * @param mac
     * @param android_id
     * @param os
     * @param timestamp
     * @param convert_id
     * @param callback
     */
    private void saveHeadline(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback, String status) {

        //如果不是真实数据不做处理
        if (os.equals("__OS__")) {
            return;
        }

        logger.info("头条数据检测回调数据请求成功，开始保存数据,imei={},android_id={},os={},timestamp={}", imei, android_id, os, timestamp);
        UserHeadlineChannelPO uc = new UserHeadlineChannelPO();
        uc.setAdid(adid);
        uc.setCid(cid);
        uc.setImei(imei);
        uc.setMac(mac);
        uc.setAndroidid(android_id);
        uc.setOs(Integer.valueOf(os));
        uc.setTimestamp(timestamp);
        uc.setConvertId(convert_id);
        uc.setCallback(callback);
        uc.setStatus(status);
        userHeadlineChannelService.addUserHeadlineChannel(uc);
        logger.info("头条数据检测回调数据保存成功,imei={},android_id={},os={},timestamp={}", imei, android_id, os, timestamp);
    }


    @RequestMapping(value = "ttandroid1", method = RequestMethod.GET)
    public String ttandroid1(String adid, String cid, String imei, String mac, String android_id,
                             String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "ttandroid2", method = RequestMethod.GET)
    public String ttandroid2(String adid, String cid, String imei, String mac, String android_id,
                             String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @ResponseBody
    @RequestMapping(value = "toutiao2", method = RequestMethod.GET)
    public String toutiao2(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {


            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @ResponseBody
    @RequestMapping(value = "toutiao3", method = RequestMethod.GET)
    public String toutiao3(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "toutiao4", method = RequestMethod.GET)
    public String toutiao4(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "toutiao5", method = RequestMethod.GET)
    public String toutiao5(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "toutiao6", method = RequestMethod.GET)
    public String toutiao6(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "toutiao7", method = RequestMethod.GET)
    public String toutiao7(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     * @return
     */
    @RequestMapping(value = "/headlinelast")
    public void repHeadline() {
        try {
		/*	List<Object> list = financeBidService.getFidChannel();
			for (Object object : list) {
				FinanceBid financeBid = (FinanceBid) object;
				UserHeadlineChannel userHeadlineChannel = userHeadlineChannelService.getUserByImei(financeBid.getChannel());
				if( null != userHeadlineChannel){
					Map<String, String> toMap = new HashMap<>();
					MD5 md5 = new MD5();
					toMap.put("callback", userHeadlineChannel.getCallback());
					toMap.put("muid", md5.getMD5ofStr(userHeadlineChannel.getImei()));
					toMap.put("source", "td");
					toMap.put("os", "0");
					toMap.put("event_type", "2");
					toMap.put("conv_time", userHeadlineChannel.getTimestamp());
//					String url = "http://ad.toutiao.com/track/activate/?callback=?"+userHeadlineChannel.getCallback()+""
//							+ "&muid="+imei+"&source=td&os=0&event_type="+1+"&conv_time="+userHeadlineChannel.getTimestamp();
					String url = "http://ad.toutiao.com/track/activate";
					String signature = HeadTools.getHmacSHA1(url, HeadTools.HEAD_KEY);
					signature = Base64.encode(signature);
					url = url+"&signature="+signature;
					String teString = HttpUtil.doPost(url, toMap, "UTF-8");
				}
			}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 威猛头条01
     *
     * @param adid
     * @param cid
     * @param imei
     * @param mac
     * @param android_id
     * @param os
     * @param timestamp
     * @param convert_id
     * @param callback
     * @return
     */
    @RequestMapping(value = "ttwmanzhuo1", method = RequestMethod.GET)
    public String ttwmanzhuo1(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmanzhuo2", method = RequestMethod.GET)
    public String ttwmanzhuo2(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "ttwmanzhuo3", method = RequestMethod.GET)
    public String ttwmanzhuo3(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "ttwmanzhuo4", method = RequestMethod.GET)
    public String ttwmanzhuo4(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "ttwmanzhuo5", method = RequestMethod.GET)
    public String ttwmanzhuo5(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmanzhuo6", method = RequestMethod.GET)
    public String ttwmanzhuo6(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "5");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     *
     * @param adid
     * @param cid
     * @param imei
     * @param mac
     * @param android_id
     * @param os
     * @param timestamp
     * @param convert_id
     * @param callback
     * @return
     */
    @RequestMapping(value = "ttwm301", method = RequestMethod.GET)
    public String ttwm301(String adid, String cid, String imei, String mac, String android_id,
                              String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "ttwm302", method = RequestMethod.GET)
    public String ttwm302(String adid, String cid, String imei, String mac, String android_id,
                          String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "ttwm303", method = RequestMethod.GET)
    public String ttwm303(String adid, String cid, String imei, String mac, String android_id,
                          String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    @RequestMapping(value = "ttwm304", method = RequestMethod.GET)
    public String ttwm304(String adid, String cid, String imei, String mac, String android_id,
                          String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwm305", method = RequestMethod.GET)
    public String ttwm305(String adid, String cid, String imei, String mac, String android_id,
                          String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwm306", method = RequestMethod.GET)
    public String ttwm306(String adid, String cid, String imei, String mac, String android_id,
                          String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }



    /**
     * 头条 威猛新版本
     *
     * @param adid
     * @param cid
     * @param imei
     * @param mac
     * @param android_id
     * @param os
     * @param timestamp
     * @param convert_id
     * @param callback
     * @return
     */
    @RequestMapping(value = "ttwmnew1", method = RequestMethod.GET)
    public String ttwmnew1(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew2", method = RequestMethod.GET)
    public String ttwmnew2(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew3", method = RequestMethod.GET)
    public String ttwmnew3(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew4", method = RequestMethod.GET)
    public String ttwmnew4(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew5", method = RequestMethod.GET)
    public String ttwmnew5(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew6", method = RequestMethod.GET)
    public String ttwmnew6(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew7", method = RequestMethod.GET)
    public String ttwmnew7(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmnew8", method = RequestMethod.GET)
    public String ttwmnew8(String adid, String cid, String imei, String mac, String android_id,
                           String os, String timestamp, String convert_id, String callback) {
        try {

            this.saveHeadline(adid, cid, imei, mac, android_id, os, timestamp, convert_id, callback, "0");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     * ios头条检测链接
     *
     * @param adid
     * @param cid
     * @param idfa
     * @param mac
     * @param os
     * @param timestamp
     * @param callback
     * @return
     */
    @RequestMapping(value = "ttwmios1", method = RequestMethod.GET)
    public String ttwmios1(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios1头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "ttwmios1", callback, "0");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmios2", method = RequestMethod.GET)
    public String ttwmios2(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios2头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "ttwmios2", callback, "0");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmios3", method = RequestMethod.GET)
    public String ttwmios3(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios3头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "ttwmios3", callback, "0");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmios4", method = RequestMethod.GET)
    public String ttwmios4(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios4头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "ttwmios4", callback, "0");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmios5", method = RequestMethod.GET)
    public String ttwmios5(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios5头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "ttwmios5", callback, "0");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "ttwmios6", method = RequestMethod.GET)
    public String ttwmios6(String adid, String cid, String idfa, String mac,
                           String os, String timestamp, String callback) {
        try {
            logger.info("ios6头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "", callback, "5");

            logger.info("ios头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    /**
     * @param adid
     * @param cid
     * @param idfa
     * @param mac
     * @param os
     * @param timestamp
     * @param callback
     * @return
     */
    @RequestMapping(value = "toutiaoios1", method = RequestMethod.GET)
    public String toutiaoios1(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios1头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios1", callback, "0");

            logger.info("toutiaoios1头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "toutiaoios2", method = RequestMethod.GET)
    public String toutiaoios2(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios2头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios2", callback, "0");

            logger.info("toutiaoios2头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "toutiaoios3", method = RequestMethod.GET)
    public String toutiaoios3(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios3头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios3", callback, "0");

            logger.info("toutiaoios3头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "toutiaoios4", method = RequestMethod.GET)
    public String toutiaoios4(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios4头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios4", callback, "0");

            logger.info("toutiaoios4头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "toutiaoios5", method = RequestMethod.GET)
    public String toutiaoios5(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios5头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios5", callback, "0");

            logger.info("toutiaoios5头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "toutiaoios6", method = RequestMethod.GET)
    public String toutiaoios6(String adid, String cid, String idfa, String mac,
                              String os, String timestamp, String callback) {
        try {
            logger.info("toutiaoios5头条检测链接,idfa={}", idfa);
            this.saveHeadline(adid, cid, idfa, mac, "", os, timestamp, "toutiaoios6", callback, "0");

            logger.info("toutiaoios5头条检测链接,返回成功,idfa={}", idfa);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


}










