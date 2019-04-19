package com.jiuyuan.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.SmsRecordMapper;
import com.jiuyuan.entity.ucpaas.client.AbsRestClient;
import com.jiuyuan.entity.ucpaas.client.JsonReqClient;
import com.jiuyuan.entity.ucpaas.client.XmlReqClient;

/**
 * 接口文档参考http://docs.ucpaas.com/doku.php<br>
 * 
 *
 */
@Service
public class UcpaasService {
    private static final Logger logger = LoggerFactory.getLogger(UcpaasService.class);
    
    private static final boolean json = true; //true  json 请求， false XML请求 
    
    private static final String accountSid = "bf556fda1feaeab46ff2d06c6dd96927";
    
    private static final String token = "1fcdf24ff833a26e86964abbcc72235a";
    
    private static final String appId = "7828de81a08244729e6869657f5fb364";//语音appId
    
//    private static final String appId = "b298d6cc182a48da9ba7f6084d92b6ff";//短信appId
    
    private static final String templateId = "51619";
    
  //  private static final String to = "13675881551";
    
  //  private static final String para = "0000";
    @Autowired
    private MemcachedService memcachedService;

    
    @Autowired
    private SmsRecordMapper smsRecordMapper;

	public Boolean testTemplateSMS(boolean json,String accountSid,String authToken,String appId,String templateId,String to, String param){
		try {
			String result = InstantiationRestAPI(json).templateSMS(accountSid, authToken, appId, templateId, to, param);
//			String result = "{\"resp\":{\"respCode\":\"000000\",\"templateSMS\":{\"createDate\":\"20170519182658\",\"smsId\":\"037756edde7b7582e66f653ffe196bd8\"}}}";
			logger.info("Response content is: " + result);
			JSONObject jsonArray = JSON.parseObject(JSON.parseObject(result).get("resp").toString());
			if(jsonArray.get("respCode").toString().equals("000000")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		UcpaasService ucpaasService = new UcpaasService();
		boolean success = ucpaasService.testTemplateSMS(true,accountSid,token,appId,"255196","15215135450","");
		if(success){
//			logger.info("成功！");
			System.out.println("成功！");
		}else{
//			logger.info("失败！");
			System.out.println("失败！");
		}
	}
	
	public Boolean testVoiceCode(boolean json,String accountSid,String authToken,String appId,String to,String verifyCode){
		try {
			String result = InstantiationRestAPI(json).voiceCode(accountSid, authToken, appId, to, verifyCode);
			logger.info("Response content is: " + result);
			JSONObject jsonArray = JSON.parseObject(JSON.parseObject(result).get("resp").toString());
			if(jsonArray.get("respCode").toString().equals("000000")){
				return true;
			}else{
				logger.info("云之讯返回码：" + jsonArray.get("respCode").toString()+"（发送成功返回码应该为000000）");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	public static AbsRestClient InstantiationRestAPI(boolean enable) {
		if(enable) {
			return new JsonReqClient();
		} else {
			return new XmlReqClient();
		}
	}
//	public static void main(String[] args) throws IOException {
//		UcpaasService ucpaasService = new UcpaasService();
//		//ucpaasService.sendVoiceCode("13675881551");
//		ucpaasService.send("13675881551", "voice" , 2);
//		
//	}

	/**
	 * 发送短信随机码
	 * 
	 * @param phone
	 * @param randomCode
	 * int app  1:会员版 2：门店版  3：后台工具
	 */
    public boolean send(final String phone ,String sendType, int app) {
        long time = System.currentTimeMillis();
//        int count = smsRecordMapper.getRecordCount(phone, time - DateUtils.MILLIS_PER_DAY);
//        if (count >= 9) { // 一天内同一个手机号发送过多会被运营商视为短信轰炸，导致用户接收不了短信
//            logger.error("sms send limited, phone:{}, already send count:{}", phone, count);
//            return false;
//        }
        String key =  phone;
        String groupKey = MemcachedKey.UCPAAS_VERIFY_CODE;
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
          	return false;
        }
       // final String jsonParams = params.toJSONString();
        int verifyCode = (int) (Math.random() * 9000 + 1000);
        boolean success = false;
        if(sendType != null && sendType.equals("sms")){
        	success = testTemplateSMS(json, accountSid, token, appId, templateId, phone, verifyCode + "");
        	
        }else if(sendType != null && sendType.equals("voice")){
        	success = testVoiceCode(json, accountSid, token, appId, phone, verifyCode + "");
        	
        }
        
        if (success) { 
        	
        	if(sendType != null && sendType.equals("sms") && app == 1 ){        	
        		smsRecordMapper.addSmsRecord(phone, "", time);  	
            }else if(sendType != null && sendType.equals("voice") && app == 1){
            	smsRecordMapper.addSmsRecord(phone, "", time);
            }
            else if(sendType != null && sendType.equals("sms") && app == 2){        	
        		smsRecordMapper.addStoreSmsRecord(phone, "", time, 0, 0, 1);  	
        	}else if(sendType != null && sendType.equals("voice") && app == 2){
        		smsRecordMapper.addStoreSmsRecord(phone, "", time, 0, 1, 1);
        	}
            memcachedService.set(groupKey, key, 3 * DateConstants.SECONDS_PER_MINUTE, verifyCode + "");
          //  smsRecordMapper.addSmsRecord(phone, jsonParams, time);
        }

        return success;
	}
	/**
	 * 发送语音随机码
	 * 
	 * @param phone
	 * @param randomCode
	 */
//    public boolean sendVoiceCode(final String phone) {
//        long time = System.currentTimeMillis();
//
//        
//        String key = phone;
//        String groupKey = MemcachedKey.UCPAAS_VERIFY_CODE;
//       // final String jsonParams = params.toJSONString();
//        int verifyCode = (int) (Math.random() * 9000 + 1000);
//        boolean success = testVoiceCode(json, accountSid, token, appId, phone, verifyCode + "");
//        
//        if (success) {         
//            smsRecordMapper.addSmsRecord(phone, "", time, 0, 1, 1);
//            memcachedService.set(groupKey, key, 3 * DateConstants.SECONDS_PER_MINUTE, verifyCode + "");
//          //  smsRecordMapper.addSmsRecord(phone, jsonParams, time);
//        }
//
//        return success;
//	}
    
	/**
	 * 验证短信随机码是否正确
	 * 
	 * @param phone
	 * @param randomCode
	 */
    public boolean verifyCode(String phone, String code) {
        //long time = System.currentTimeMillis();
        String key = phone;
        String groupKey = MemcachedKey.UCPAAS_VERIFY_CODE;
        Object obj = memcachedService.get(groupKey, key);
      //  Object obj = null;// memcachedService.get(groupKey, key);
        
        if (obj != null && code.equals((String)obj)) {
        	return true;
        } else {
        	return false;
        }

	}
    
	
}
