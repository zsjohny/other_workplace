package com.jiuyuan.util.tree.help.equality;

public class EqualsEqualityTester<T> implements EqualityTester<T> {

    public boolean equals(T a, T b) {
        if (a == b) {
            return true;
        } else if (a != null) {
            return a.equals(b);
        } else {
            return false;
        }
    }
}
