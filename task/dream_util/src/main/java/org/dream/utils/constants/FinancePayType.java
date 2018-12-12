package org.dream.utils.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class FinancePayType {
	private String platform;
	private String name;

	public FinancePayType(String platform, String name) {
		super();
		this.platform = platform;
		this.name = name;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static final FinancePayType BAOFOO = new FinancePayType("baofoo", "宝付支付");
	public static final FinancePayType IPAYNOW = new FinancePayType("ipaynow", "现在支付");
	public static final FinancePayType ALIPAY = new FinancePayType("alipay", "支付宝");
	public static final FinancePayType TIANFUBAO_WECHAT = new FinancePayType("tianfubaowechat", "天付宝微信");
	public static final FinancePayType VIAPAY_ALIPAY = new FinancePayType("viapayalipay", "ViaPay支付宝");
	public static final FinancePayType TIANFUBAO = new FinancePayType("tianfubao", "天付宝");
	public static final FinancePayType MANUAL = new FinancePayType("rengong", "人工");
	public static final FinancePayType JINGDONG = new FinancePayType("jingdong","京东");
	public static final FinancePayType FUYOU = new FinancePayType("fuyou","富友");
	

	private static List<FinancePayType> list = new ArrayList<FinancePayType>();

	private static void init() {
		Field[] platformType = FinancePayType.class.getDeclaredFields();
		for (int i = 0; i < platformType.length; i++) {
			Field f = platformType[i];
			if (FinancePayType.class.isAssignableFrom(f.getType())) {
				try {
					FinancePayType type = (FinancePayType) f.get(FinancePayType.class);
					list.add(type);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<FinancePayType> getFinancyType() {
		if (list.size() == 0) {
			init();
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(FinancePayType.getFinancyType());
	}
}
