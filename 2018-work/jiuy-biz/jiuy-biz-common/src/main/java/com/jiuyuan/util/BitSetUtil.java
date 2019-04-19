package com.jiuyuan.util;

import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

public class BitSetUtil {

    public static BitSet deserialize(int nbits, String stringForm) {
        BitSet bitSet = new BitSet(nbits);
        if (StringUtils.isNotBlank(stringForm)) {
            String[] indices = StringUtils.split(stringForm, ",{} ");
            for (String index : indices) {
                bitSet.set(Integer.valueOf(index));
            }
        }
        return bitSet;
    }
}
