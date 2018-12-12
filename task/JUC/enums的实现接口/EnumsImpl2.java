package com.test.config;

public enum EnumsImpl2 implements Test3 {
    RED(1) {
        @Override
        public int say() {
            return -1;
        }
    }, BLACK(2) {
        @Override
        public int say() {
            return -2;
        }
    };
    private int color;

    EnumsImpl2(int color) {
        this.color = color;
    }


}
