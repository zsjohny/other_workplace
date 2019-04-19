package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

public class RandomUtil {

	/**
	 * 从 [0,max) 中取出count个不相同的随机数
	 */
	public static List<Integer> randomNumbersNoRepeat(int max, int count) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			int num;
			do {
				num = RandomUtils.nextInt(0, max);
			} while (list.contains(num) == true);
			list.add(num);
		}
		return list;
	}

	/**
	 * 从list中随机取出count个元素
	 */
	public static <T> List<T> randomSelect(List<T> list, int count) {
		if (list.size() <= count) {
			return list;
		}

		List<T> result = new ArrayList<T>();
		List<Integer> indices = randomNumbersNoRepeat(list.size(), count);
		for (int index : indices) {
			result.add(list.get(index));
		}

		return result;
	}
}
