package org.dream.utils.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceType extends ConstType {

	public FinanceType(Integer type, Integer typeDetail, String remark) {
		super(type, typeDetail, remark);
	}

	// static private Map<Integer, List<FinanceType>> map = new HashMap<>();
	private static List<FinanceType> list = new ArrayList<FinanceType>();

	private static void init() throws Exception {
		Field[] IO_fields = FinanceIOType.class.getDeclaredFields();
		for (int i = 0; i < IO_fields.length; i++) {
			Field f = IO_fields[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceIOType type = (FinanceIOType) f.get(FinanceIOType.class);
				list.add(type);
			}
		}
		Field[] consume = FinanceConsumeType.class.getDeclaredFields();
		for (int i = 0; i < consume.length; i++) {
			Field f = consume[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceConsumeType type = (FinanceConsumeType) f.get(FinanceConsumeType.class);
				list.add(type);
			}
		}
		Field[] settle = FinanceSettleType.class.getDeclaredFields();
		for (int i = 0; i < settle.length; i++) {
			Field f = settle[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceSettleType type = (FinanceSettleType) f.get(FinanceSettleType.class);
				list.add(type);
			}
		}
		Field[] chargeBack = FinanceChargeBackType.class.getDeclaredFields();
		for (int i = 0; i < chargeBack.length; i++) {
			Field f = chargeBack[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceChargeBackType type = (FinanceChargeBackType) f.get(FinanceChargeBackType.class);
				list.add(type);
			}
		}
		Field[] inner = FinanceInnerType.class.getDeclaredFields();
		for (int i = 0; i < inner.length; i++) {
			Field f = inner[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceInnerType type = (FinanceInnerType) f.get(FinanceInnerType.class);
				list.add(type);
			}
		}
		Field[] system = FinanceSystemType.class.getDeclaredFields();
		for (int i = 0; i < system.length; i++) {
			Field f = system[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceSystemType type = (FinanceSystemType) f.get(FinanceSystemType.class);
				list.add(type);
			}
		}
		Field[] transfer = FinanceTransferType.class.getDeclaredFields();
		for (int i = 0; i < transfer.length; i++) {
			Field f = transfer[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceTransferType type = (FinanceTransferType) f.get(FinanceTransferType.class);
				list.add(type);
			}
		}
		Field[] commission = FinanceCommissionType.class.getDeclaredFields();
		for (int i = 0; i < commission.length; i++) {
			Field f = commission[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceCommissionType type = (FinanceCommissionType) f.get(FinanceCommissionType.class);
				list.add(type);
			}
		}
		Field[] shopping = FinanceShoppingType.class.getDeclaredFields();
		for (int i = 0; i < shopping.length; i++) {
			Field f = shopping[i];
			if (FinanceType.class.isAssignableFrom(f.getType())) {
				FinanceShoppingType type = (FinanceShoppingType) f.get(FinanceShoppingType.class);
				list.add(type);
			}
		}
		// Field[] order_fields = FinanceOrderType.class.getDeclaredFields();
		// for (int i = 0; i < order_fields.length; i++) {
		// Field f = order_fields[i];
		// if (FinanceType.class.isAssignableFrom(f.getType())) {
		// try {
		// FinanceOrderType type = (FinanceOrderType)
		// f.get(FinanceOrderType.class);
		// list.add(type);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// Field[] system_fields = FinanceSystemType.class.getDeclaredFields();
		// for (int i = 0; i < system_fields.length; i++) {
		// Field f = system_fields[i];
		// if (FinanceType.class.isAssignableFrom(f.getType())) {
		// try {
		// FinanceSystemType type = (FinanceSystemType)
		// f.get(FinanceSystemType.class);
		// list.add(type);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// Field[] transfer_fields =
		// FinanceTransferType.class.getDeclaredFields();
		// for (int i = 0; i < transfer_fields.length; i++) {
		// Field f = transfer_fields[i];
		// if (FinanceType.class.isAssignableFrom(f.getType())) {
		// try {
		// FinanceTransferType type = (FinanceTransferType)
		// f.get(FinanceTransferType.class);
		// list.add(type);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
	}

	public static List<FinanceType> getFinancyType() throws Exception {
		if (list.size() == 0) {
			init();
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		List<FinanceType> list = FinanceType.getFinancyType();
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i));
	}
}
