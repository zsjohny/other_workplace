package com.test.config;

public enum EnumsImpl implements Test3 {
    RED(1), BLACK(2);
    private int color;

    EnumsImpl(int color) {
        this.color = color;
    }

    @Override
    public int say() {

        return color;
    }


}
