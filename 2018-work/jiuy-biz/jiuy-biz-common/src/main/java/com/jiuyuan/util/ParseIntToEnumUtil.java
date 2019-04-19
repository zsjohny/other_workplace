package com.jiuyuan.util;

import com.jiuyuan.util.enumeration.IntEnum;

public final class ParseIntToEnumUtil<E> {

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E parse(Integer enumCode, Class<E> clazz){
		Class<?>[] intfaces = clazz.getInterfaces();
		E ieDefault = null;
		if(null != intfaces && intfaces.length != 0){
			for(Class<?> cls : intfaces){
				if(cls.isAssignableFrom(IntEnum.class)){
					Enum<E> [] consts = clazz.getEnumConstants();
					for(Enum<E> c : consts){
						IntEnum ie = (IntEnum)c;
						if(ie.getIntValue() == enumCode){
							return (E)ie;
						}
					}
				}
			}
		}
		return ieDefault;
	}
}
