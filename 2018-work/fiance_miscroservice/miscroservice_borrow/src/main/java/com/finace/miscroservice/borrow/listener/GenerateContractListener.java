package com.finace.miscroservice.borrow.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.finace.miscroservice.borrow.service.Contract.ContractService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.ConvertNumber;
import com.finace.miscroservice.commons.utils.PDF.PDFHeaderFooter;
import com.finace.miscroservice.commons.utils.PDF.PDFKitUtil;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *自动生成合同
 */
@Component
public class GenerateContractListener extends MqListenerConvert{
    private static Log logger = Log.getInstance(GenerateContractListener.class);

    @Autowired
    private FinanceBidService financeBidService;

	@Autowired
	private ContractService contractService;
    
	@Override
	protected void transferTo(String transferData) {
		logger.info("自动生成合同,开始transferData={}", transferData);
		if( transferData == null ) {
			logger.warn("自动生成合同,解析参数为空={}", transferData);
			return;
		}

		try{
			//获取合同需要信息
			Map<String, Object> map = financeBidService.showHt(transferData);
			if( null != map ){
				Map<String, String> sendMap = new HashMap<>();
				String addUserMsg = "";
				//判断平台是否存在
				if(!contractService.isExistUser(ContractService.PLATFORM_COMPANY)){
					//新增平台
					sendMap.clear();
					sendMap.put("appUserId", ContractService.PLATFORM_COMPANY);   //平台自定义该用户的账号标识，小于等于49个字
					sendMap.put("cellNum", "18106562052");   //电话号码，1开头的11位数字
					sendMap.put("userType", "4");  //用户类型，1个人、2企业、4平台自身
					sendMap.put("userName", String.valueOf(map.get("prealname")));  //用户名称
					sendMap.put("certifyType", "4");  //实名认证证件类型
					sendMap.put("certifyNumber", "91330106328238952C");  //实名认证证件号码
					addUserMsg = contractService.addUser(sendMap);
					logger.info("开始添加平台{},结果addUserMsg={}",ContractService.PLATFORM_COMPANY, addUserMsg);
				}

				//判断出借人是否存在
				if(!contractService.isExistUser(String.valueOf(map.get("tuserId")))){
					//出借人
					sendMap.clear();
					sendMap.put("appUserId", String.valueOf(map.get("tuserId")));   //平台自定义该用户的账号标识，小于等于49个字
					sendMap.put("cellNum", String.valueOf(map.get("tphone")));   //电话号码，1开头的11位数字
					sendMap.put("userType", "1");  //用户类型，1个人、2企业、4平台自身
					sendMap.put("userName", String.valueOf(map.get("trealname")));  //用户名称
					sendMap.put("certifyType", "1");  //实名认证证件类型
					sendMap.put("certifyNumber", String.valueOf(map.get("tcard")));  //实名认证证件号码
					addUserMsg = contractService.addUser(sendMap);
					logger.info("开始添加出借人{},结果addUserMsg={}", map.get("tuserId"), addUserMsg);
				}

				//判断借款人
				/*if(!contractService.isExistUser(String.valueOf(map.get("juserId")))){
					//借款人
					sendMap.clear();
					sendMap.put("appUserId", String.valueOf(map.get("juserId")));   //平台自定义该用户的账号标识，小于等于49个字
					sendMap.put("cellNum", String.valueOf(map.get("jphone2")));   //电话号码，1开头的11位数字
					if (Constant.WEB_USER_TYPEID_J == Integer.valueOf(map.get("jtype").toString())) {
						sendMap.put("userType", "2");  //用户类型，1个人、2企业、4平台自身
						sendMap.put("certifyType", "4");  //实名认证证件类型
					}else{
						sendMap.put("userType", "1");  //用户类型，1个人、2企业、4平台自身
						sendMap.put("certifyType", "1");  //实名认证证件类型
					}
					sendMap.put("userName", String.valueOf(map.get("jrealname2")));  //用户名称
					sendMap.put("certifyNumber", String.valueOf(map.get("jcard")));  //实名认证证件号码
					addUserMsg = contractService.addUser(sendMap);
					logger.info("开始添加借款人{},结果addUserMsg={}", map.get("juserId"), addUserMsg);
				}*/

				String creatToken = contractService.getToken(ContractService.PLATFORM_COMPANY);
				if (null != creatToken) {
					logger.info("开始创建合同,creatToken={}", creatToken);
					//添加用户
					sendMap.clear();
					sendMap.put("defContractNo", String.valueOf(map.get("contract")));
					Map<String, String> pmap = new HashMap<>();
					//传递参数
					pmap.put("${contract}", String.valueOf(map.get("contract")));

					pmap.put("${jrealname}", String.valueOf(map.get("jrealname")));
					//pmap.put("${jaddress}", String.valueOf(map.get("jaddress")));
					pmap.put("${privacy}", String.valueOf(map.get("privacy")));
					pmap.put("${jphone}", String.valueOf(map.get("jphone")));

					pmap.put("${trealname}", String.valueOf(map.get("trealname")));
					pmap.put("${tusername}", String.valueOf(map.get("tusername")));
					pmap.put("${tcard}", String.valueOf(map.get("tcard")));
					pmap.put("${tphone}", String.valueOf(map.get("tphone")));

					pmap.put("${account}", String.valueOf(map.get("account")));
					pmap.put("${caccount}", ConvertNumber.numberToChinese(Double.valueOf(map.get("account").toString())));
					pmap.put("${buyamt}", String.valueOf(map.get("buyAmt")));

					pmap.put("${interest}", String.valueOf(map.get("interest")));
					pmap.put("${limitday}", String.valueOf(map.get("limitDay")));

					pmap.put("${dqry}", String.valueOf(map.get("dqr")).split("-")[0]);
					pmap.put("${dqrm}", String.valueOf(map.get("dqr")).split("-")[1]);
					pmap.put("${dqrd}", String.valueOf(map.get("dqr")).split("-")[2]);

					pmap.put("${qxry}", String.valueOf(map.get("qxr")).split("-")[0]);
					pmap.put("${qxrm}", String.valueOf(map.get("qxr")).split("-")[1]);
					pmap.put("${qxrd}", String.valueOf(map.get("qxr")).split("-")[2]);

					pmap.put("${apr}", String.valueOf(map.get("apr")));
					pmap.put("${loanuage}", String.valueOf(map.get("loanUage")));

					sendMap.put("param", JSONObject.toJSONString(pmap));
					String contractId = contractService.templateContract(sendMap, creatToken);
					if (null != contractId) {
						logger.info("开始合成合同,contractId={}", contractId);
						//生成合同
						Boolean isSuccess = contractService.addPartner(creatToken, contractId, contractService.PLATFORM_COMPANY, String.valueOf(map.get("tuserId")), String.valueOf(map.get("juserId")));
                        if(isSuccess){
							logger.info("开始签署合同,contractId={}", contractId);
                        	//签署合同
							Boolean isSign =  contractService.signContract(creatToken, contractId, contractService.PLATFORM_COMPANY, String.valueOf(map.get("tuserId")), String.valueOf(map.get("juserId")));
							if(isSign){
								financeBidService.updateYunContractIdById(contractId, transferData);
								logger.info("合同签署成功,contractId={}", contractId);
							}
						}
					}
				}
			}
		}catch (Exception e){
			logger.error("生成合同{}异常信息：{}",transferData,e);
		}

		/*PDFHeaderFooter headerFooter=new PDFHeaderFooter();
		PDFKitUtil kit = new PDFKitUtil();
		kit.setHeaderFooterBuilder(headerFooter);
		String path = kit.exportToFile("contractTmplate.pdf", map, "/root/Desktop/pdf/"+"content"+UUIdUtil.generateUuid()+".pdf");
		System.out.println(path);*/


	}


}
