package com.finace.miscroservice;

import java.io.IOException;
import java.net.Socket;


public class Ip extends Thread{
    private int[] p;
    Socket ss = null;

    public Ip(int[] p) {
        this.p = p;
    }

    public static void main(String[] args) {
        for(int i=0;i<5000;i=i+100){
            new Ip(new int[]{
                    i+1,i+100
            }).start();
        }
    }
    @Override
    public void run() {
        System.err.println("启动线程");
        for(int i=p[0]; i<p[1];i++){
            try {
//                System.out.println(i);
                ss = new Socket("127.0.0.1",i);
                System.err.println("扫描到端口： " + i);

            } catch (IOException e) {

            }
        }
    }
//    testCompile "com.google.protobuf:protobuf-java:3.5.1"
//    testCompile "org.openjdk.jmh:jmh-core:1.18"
//    testCompile "org.openjdk.jmh:jmh-generator-annprocess:1.18"

}