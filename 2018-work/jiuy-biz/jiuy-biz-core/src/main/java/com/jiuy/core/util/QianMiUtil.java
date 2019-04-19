package com.jiuy.core.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jiuy.core.service.GlobalSettingService;
import com.qianmi.open.api.ApiException;
import com.qianmi.open.api.DefaultOpenClient;
import com.qianmi.open.api.OpenClient;
import com.qianmi.open.api.domain.cloudshop.Item;
import com.qianmi.open.api.domain.cloudshop.ItemCat;
import com.qianmi.open.api.domain.cloudshop.SellerCat;
import com.qianmi.open.api.request.D2pLogisticsSendRequest;
import com.qianmi.open.api.request.D2pTradeFinanceAuditRequest;
import com.qianmi.open.api.request.D2pTradePackRequest;
import com.qianmi.open.api.request.D2pTradesSoldGetRequest;
import com.qianmi.open.api.request.ItemAddRequest;
import com.qianmi.open.api.request.ItemDetailUpdateRequest;
import com.qianmi.open.api.request.ItemImageUploadRequest;
import com.qianmi.open.api.request.ItemListingRequest;
import com.qianmi.open.api.request.ItemSellercatUpdateRequest;
import com.qianmi.open.api.request.ItemSkuUpdateRequest;
import com.qianmi.open.api.request.ItemcatAddRequest;
import com.qianmi.open.api.request.ItemcatsGetRequest;
import com.qianmi.open.api.request.ItemsAllListRequest;
import com.qianmi.open.api.request.LogisticsCompaniesGetRequest;
import com.qianmi.open.api.request.SellercatAddRequest;
import com.qianmi.open.api.request.SellercatsGetRequest;
import com.qianmi.open.api.response.D2pLogisticsSendResponse;
import com.qianmi.open.api.response.D2pTradeFinanceAuditResponse;
import com.qianmi.open.api.response.D2pTradePackResponse;
import com.qianmi.open.api.response.D2pTradesSoldGetResponse;
import com.qianmi.open.api.response.ItemAddResponse;
import com.qianmi.open.api.response.ItemDetailUpdateResponse;
import com.qianmi.open.api.response.ItemImageUploadResponse;
import com.qianmi.open.api.response.ItemListingResponse;
import com.qianmi.open.api.response.ItemSellercatUpdateResponse;
import com.qianmi.open.api.response.ItemSkuUpdateResponse;
import com.qianmi.open.api.response.ItemcatAddResponse;
import com.qianmi.open.api.response.ItemcatsGetResponse;
import com.qianmi.open.api.response.ItemsAllListResponse;
import com.qianmi.open.api.response.LogisticsCompaniesGetResponse;
import com.qianmi.open.api.response.SellercatAddResponse;
import com.qianmi.open.api.response.SellercatsGetResponse;
import com.qianmi.open.api.response.TokenResponse;
import com.qianmi.open.api.tool.util.OAuthUtils;
import com.qianmi.open.api.tool.util.QianmiContext;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@SuppressWarnings("unused")
public class QianMiUtil {
	
	private static final String appKey = "10001233";
	private static final String appSecret = "IQNXzKLPdstOOsn0G9XtQ86QlyfSjMwp";
	private static final String url = "http://gw.api.qianmi.com/api";
	
	private static final String code = "793fc6c484695cc65417c22d967aff7d";
	private static final String accessToken = "83d1453824cc4f0289c46b70e9632fdf";
	private static final String refreshToken = "d23d3dd8bbb9462e84627bcc5ac9380c";

	private static String product_fields = "num_iid,title,price,skus";
	private static String product_title = "网漏肩中长上衣2";
	private static String product_outer_id = "TY1607291027_test";
	private static String product_unit = "件";
	private static String product_brand_id = "440";
	private static String product_brand_name = "yu姐姐";
	private static String product_skus_json = "[{\"outer_id\":\"yjjskuid_0005\",\"price\":\"1999.00\",\"quantity\":\"10\",\"sku_props_values\":{\"尺码\":\"S\",\"颜色分类\":\"白色\"}},{\"outer_id\":\"yjjskuid_0006\",\"price\":\"1999.00\",\"quantity\":\"10\",\"sku_props_values\":{\"尺码\":\"M\",\"颜色分类\":\"黑色\"}}]";
	private static String product_image = "";
	private static String product_desc = "这是2016yjj冬季上衣";
	
	private static String order_fields = "total_results,tid,flow_status,buyer_nick,reciver_name,reciver_state,reciver_city,reciver_district,reciver_address,reciver_zip,reciver_mobile,reciver_phone,num,orders,created,modified";
	private static String order_flow_status = "pending_pack";
	private static String order_pay_status = "-1";
	private static String order_start_created = "2016-09-12 12:20:20";
	private static String order_end_created = "2016-12-12 12:20:20";
	private static Integer order_page_size = 100;
	private static Integer order_page_no = 0;
	
	private static String send_tid = "T20161018181763848";
	private static String send_pack_items = "O20161018183051792:1";
	private static String send_out_sid = "111222";
	private static String send_deliver_time = "2016-10-20 08:48:10";
	private static String send_post_fee = "10";
	private static String send_ship_type_id = "express";
	private static String send_company_id = "100206588";
	private static String send_company_name = "千米自营快递（自有）";
	
	private static String pack_tid = "T20161018181763848";
	private static String pack_packItems = "O20161018183051792:1";
	
	private static String audit_tid = "T20161018181763848";
	
	private static String cat_parent_cid = "1002";
	private static String cat_name = "饮料";
	
	private static String sku_sku_id = "g3055739";
	private static String sku_num_iid = "1447783";
	private static Double sku_weight = 880D;
	private static Double sku_market_price = 100D;
	
	public static void main(String[] args) throws ApiException {
//		System.out.println(updateProdDetail(accessToken, "", ""));
//		System.out.println(updateProdSellerCat(accessToken, "1447788", "10041002,10041001"));
		System.out.println(getSellercat(accessToken));
//		System.out.println(updateSku(accessToken, sku_sku_id, sku_num_iid, sku_weight, sku_market_price));
//		System.out.println(getItemCats(accessToken));
//		System.out.println(product_skus_json);
//		System.out.println(pack(accessToken, pack_tid, pack_packItems));
//		System.out.println(getToken(appKey, appSecret, code));
//		System.out.println(refreshToken(refreshToken));
//		System.out.println(productList(accessToken));
//		System.out.println(putOnProduct("1409336"));
//		System.out.println(addProduct(accessToken, product_fields, product_title, product_outer_id, product_unit, product_skus_json, product_image, product_desc, product_brand_id, product_brand_name));
//		System.out.println(getSendCompanyInfo(accessToken));
//		System.out.println(getOrders(accessToken, order_start_created, order_end_created, order_page_no, order_page_size));
//		System.out.println(send(accessToken, send_tid, send_pack_items, send_company_id, send_out_sid, send_post_fee, send_ship_type_id, send_company_name, send_deliver_time));
	}	
	
	/**
	 * 修改商品的详情介绍
	 * 
	 * @param accesstoken
	 * @param num_iid
	 * @param seller_cids
	 * @return
	 * @throws ApiException
	 */
	public static Object updateProdDetail(String accesstoken, String num_iid, String desc) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemDetailUpdateRequest req = new ItemDetailUpdateRequest();
		req.setNumIid(num_iid);
		req.setDesc(desc);
		ItemDetailUpdateResponse response = client.execute(req, accessToken);
		return response.isSuccess();
	}
	
	/**
	 * 商品挂靠销售渠道的展示类目，挂靠方式为全量挂靠，调用此接口以后，原先挂靠的展示类目将删除，将挂靠以当前传入的展示类目编号
	 * 
	 * @param accesstoken
	 * @param num_iid
	 * @param seller_cids			需要挂靠的销售渠道中的展示类目id列表，多个id之间以逗号隔开，每次只能挂靠单个销售渠道。挂靠方式为全量挂靠，调用此接口以后，原先挂靠的展示类目将删除，将挂靠当前传入的展示类目
	 * @param site	1：云订货(D2P) 2：云商城(D2C) 默认 1
	 * @return
	 * @throws ApiException
	 */
	public static Object updateProdSellerCat(String accesstoken, String num_iid, String seller_cids) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemSellercatUpdateRequest req = new ItemSellercatUpdateRequest();
		req.setSellerCids(seller_cids);
		req.setNumIid(num_iid);
		req.setSite("1");
		ItemSellercatUpdateResponse response = client.execute(req, accessToken);
		return response.getBody();
	}
	
	/**
	 * 新增店铺(销售渠道)的商品展示类目--文档有bug, p_seller_cid不传会报错
	 * 
	 * @param accesstoken 
	 * @param name 	类目名
	 * @param site	产品线：1：云订货(D2P) 2：云商城(D2C)
	 * @param p_seller_cid	父目录编号，不传则默认顶级目录
	 * @return
	 * @throws ApiException
	 */
	public static Object addSellercat(String accesstoken, String name, String p_seller_cid) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		SellercatAddRequest req = new SellercatAddRequest();
		req.setName(name);
//		req.setpSellerCid(null);
		req.setSite("1");
		SellercatAddResponse response = client.execute(req, accessToken);
		
		return response.getBody();
	}
	
	/**
	 * 获取店铺(销售渠道)的商品展示类目
	 * 
	 * @param accesstoken
	 * @return
	 * @throws ApiException
	 */
	public static List<SellerCat> getSellercat(String accesstoken) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		SellercatsGetRequest req = new SellercatsGetRequest();
		req.setFields("seller_cid,p_seller_cid,name,status,is_leaf.depth,orde");
		
		SellercatsGetResponse response = client.execute(req, accesstoken);
		
		return response.getSellerCats();
	}
 	
	/**
	 * 更新SKU信息
	 * 
	 * @param accessToken
	 * @param sku_id 	sku编号 ，g开头
	 * @param num_iid 	商品编号ID
	 * @param weight	sku的重量,单位kg,最多支持3位小数
	 * @param market_price	sku的市场价，单位元，保留2位小数
	 * @return
	 * @throws ApiException
	 */
	public static Boolean updateSku(String accessToken, String sku_id, String num_iid, Double weight, Double market_price) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemSkuUpdateRequest req = new ItemSkuUpdateRequest();
		req.setSkuId(sku_id);
		req.setNumIid(num_iid);
		req.setWeight(weight + "");
		req.setMarketPrice(market_price + "");
		ItemSkuUpdateResponse response = client.execute(req, accessToken);
		
		return response.isSuccess();
	}
	
	/**
	 * 新增商品仓库的商品类目
	 * 
	 * @param accessToken
	 * @return
	 * @throws ApiException
	 */
	public static ItemCat addItemCats(String accessToken, String name, String parent_cid) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemcatAddRequest req = new ItemcatAddRequest();
		req.setName(name);
		req.setParentCid(parent_cid);
		ItemcatAddResponse response = client.execute(req, accessToken);
		
		return response.getItemCat();
	}
	
	/**
	 * 商品类目查询接口
	 * 
	 * @param accessToken
	 * @return
	 * @throws ApiException
	 */
	public static int getItemCats(String accessToken) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemcatsGetRequest req = new ItemcatsGetRequest();
		req.setFields("cid,item_cid,p_item_cid,name,status,is_leaf,depth,parent_cid");
		ItemcatsGetResponse response = client.execute(req, accessToken);
		
		return response.getItemCats().size();
	}
	
	/**
	 * 添加/修改商品图片
	 * 
	 * @param accessToken
	 * @param num_iid 商品编号
	 * @param image 商品图片
	 * @return
	 * @throws ApiException
	 */
	public static String uploadProdImg(String accessToken, String num_iid, String image) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemImageUploadRequest req = new ItemImageUploadRequest();
		req.setNumIid(num_iid);
		req.setFields("position,url");
		req.setImage(image);
		ItemImageUploadResponse response = client.execute(req, accessToken);
		
		return response.getBody();
	}
	
	public static String productList(String accessToken) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemsAllListRequest req = new ItemsAllListRequest();
		req.setFields("num_iid,title,price");
		req.setPageSize(10);
		req.setPageNo(0);
		ItemsAllListResponse response = client.execute(req, accessToken);
		
		return response.getBody();
	}
	
	/**
	 * 云订货(D2P)订单打包出库，同一订单可以分多次出库。多次出库的商品总量不能大于商品的购买数量。
	 * 
	 * @param tid 订单编号
	 * @param packItems 发货包裹清单，包含商品单编号和商品出库数量，格式：oid:num;oid:num。商品出库数量为整数，且出库总数量不能大于商品的购买数量
	 * @return
	 * @throws ApiException
	 */
	public static Boolean pack(String accessToken, String tid, String packItems) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		D2pTradePackRequest req = new D2pTradePackRequest();
		req.setPackItems(packItems);
		req.setTid(tid);
		D2pTradePackResponse response = client.execute(req, accessToken);
		
		return response.getPack().getIsSuccess();
	}
	
	/**
	 * 查询商家添加的常用物流公司信息，用于发货接口
	 * 
	 * @return
	 * @throws ApiException
	 */
	public static String getSendCompanyInfo(String accessToken) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		LogisticsCompaniesGetRequest req = new LogisticsCompaniesGetRequest();
		req.setFields("id,code,name,seller_nick,url");
		LogisticsCompaniesGetResponse response = client.execute(req, accessToken);
		
		return response.getBody();
	}
	
	/**
 	 * 云订货(D2P)订单在线发货处理(支持部分发货)）
	 * 
	 * @param tid 包裹单编号
	 * @param packId 包裹单编号
	 * @param companyId admin卖家物流公司编号
	 * @param outSid 运单号，具体一个物流公司真实的运单号码
	 * @param postFee 物流费用，单位 元，整数部分不超过999999，精确到2位小数
	 * @param shipTypeId 发货方式编号 self:自提,express:快递,logistics:物流,virtual:虚拟发货,post:平邮,ems:EMS
	 * @param companyName 
	 * @param deliverTime 发货时间，格式yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ApiException
	 */
	public static String send(String accessToken, String tid, String packItems, String companyId, String outSid, String postFee,
			String shipTypeId, String companyName, String deliverTime) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		D2pLogisticsSendRequest req = new D2pLogisticsSendRequest();
		req.setTid(tid);
		req.setPackItems(packItems);
		req.setCompanyId(companyId);
		req.setOutSid(outSid);
		req.setPostFee(postFee);
		req.setShipTypeId(shipTypeId);
		req.setCompanyName(companyName);
		req.setDeliverTime(deliverTime);
		D2pLogisticsSendResponse response = client.execute(req, accessToken);
		
//		return response.getShipping().getIsSuccess()
		return response.getBody();
	}
	
	/**
	 * 获取卖家在云订货(D2P)已卖出的订单
	 * 
	 * @param accessToken
	 * @param flowStatus 	订单流程状态，只能是以下几种状态中的一种，不传则查询全部。
	 * 						pending_audit_trade：待订单审核，pending_audit_finance：待财务审核，
	 * 						pending_pack：待出库，pending_deliver：待发货，pending_receive：待收货确认，
	 * 						received：已收货。
	 * @param payStatus 	订单支付状态, -1:全部, 0:未支付, 1:已支付, 2:已退款,4:部分退款
	 * @param startCreated 	查询交易的创建开始时间，格式：yyyy-MM-dd HH:mm:ss，只能查询三个月之内的交易信息
	 * @param endCreated 	查询交易创建的结束时间，格式：yyyy-MM-dd HH:mm:ss
	 * @param pageSize
	 * @return
	 * @throws ApiException
	 */
	public static String getOrders(String accessToken, String startCreated, String endCreated, Integer pageNo, Integer pageSize) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		D2pTradesSoldGetRequest req = new D2pTradesSoldGetRequest();
		req.setFields(order_fields);
		req.setFlowStatus(order_flow_status);
		req.setPayStatus(order_pay_status);
		req.setStartCreated(startCreated);
		req.setEndCreated(endCreated);
		req.setPageSize(pageSize);
		D2pTradesSoldGetResponse response = client.execute(req, accessToken);
		
		return response.getBody();
	}
	

	/**
	 * 发布一个新商品
	 * 
	 * @param field 
	 * 		num_iid,title,price 所发布商品的商品详情，返回字段参照Item商品结构，多个字段用”,”分隔；
	 * @param title 商品名称
	 * @param outer_id 商品的商家外部编码
	 * @param unit 商品的计量单位
	 * @param brand_id 商品品牌编号
	 * @param brand_name 品牌名称
	 * @param skus_json 构造SKU的json串。 price(销售价)必填、quantity(库存)必填
	 * @param image 商品主图，建议尺寸：800*800 PX,单张大小不得超过1M ，支持的文件类型：gif,jpg,jpeg,png
	 * @param desc 商品描述
	 * @return
	 * @throws ApiException
	 */
	public static Item addProduct(String accessToken, String fields, String title, String outer_id, String unit, String skus_json, String image, 
			String desc, String brand_id, String brand_name) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemAddRequest req = new ItemAddRequest();
		req.setFields(fields);
		req.setTitle(title);
		req.setOuterId(outer_id);
		req.setUnit(unit);
		req.setSkusJson(skus_json);
		req.setImage(image);
		req.setDesc(desc);
//		req.setBrandId(brand_id);
		req.setBrandName(brand_name);
		ItemAddResponse response = client.execute(req, accessToken);
		
		return response.getItem();
	}

	/**
	 * 单条商品上架
	 * 
	 * @param num_iid 商品编号
	 * @throws ApiException
	 */
	public static String putOnProduct(String accessToken, String num_iid) throws ApiException {
		OpenClient client = new DefaultOpenClient(url, appKey, appSecret);
		ItemListingRequest req = new ItemListingRequest();
		req.setNumIid(num_iid);
		ItemListingResponse response = client.execute(req, accessToken);
		
		return response.getMsg();
	}
	
	/**
     * 根据授权码获取token
     * 
     * @param appKey
     * @param appSecret
     * @param code
     * @return
     * @throws ApiException
     */
    public static String getToken(String appKey, String appSecret, String code) throws ApiException {
        QianmiContext context = OAuthUtils.getToken(appKey, appSecret, code);
        TokenResponse response = context.getTokenResponse();
        
        return response.getBody();
    }

    /**
     * 根据refreshToken刷新token
     * 
     * @param appKey
     * @param appSecret
     * @param refreshToken
     * @return
     * @throws ApiException
     */
    public static String refreshToken(String refreshToken) throws ApiException {
        QianmiContext context = OAuthUtils.refreshToken(appKey, appSecret, refreshToken);
        TokenResponse response = context.getTokenResponse();
        
        return response.getBody();
    }

}
