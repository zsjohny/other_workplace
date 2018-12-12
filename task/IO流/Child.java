package org.finace.utils.test;

/**
 * Created by Ness on 2016/12/22.
 */
public class Child extends Sub {

    public Child() {
        super("hello");
        System.out.println("child");
    }

    public static void main(String[] args) {
        Child child = new Child();

    }

}
