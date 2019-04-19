package com.jiuyuan.util;

import java.util.BitSet;

public class BitSetBuilder {

    private BitSet bitSet;

    public BitSetBuilder(int nbits) {
        bitSet = new BitSet(nbits);
    }

    /**
     * @param first inclusive
     * @param last inclusive
     */
    public BitSetBuilder range(int first, int last) {
        for (int i = first; i <= last; i++) {
            bitSet.set(i);
        }
        return this;
    }

    public BitSetBuilder set(int... indices) {
        for (int i : indices) {
            bitSet.set(i);
        }
        return this;
    }

    public BitSet get() {
        return bitSet;
    }
}
