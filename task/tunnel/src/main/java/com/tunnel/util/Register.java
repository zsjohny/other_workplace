//package com.tunnel.util;
//
//import javax.swing.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Pattern;
//
///**
// * Created by Ness on 2016/10/23.
// */
//public class Register {
//    public static Map<String, String> map = new HashMap<>();
//    JFrame frame = new JFrame("Register");
//
//
//    public void loadRegister() {
//
//        frame.setContentPane(new Register().panel1);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    private JPanel panel1;
//    private JButton 注册Button;
//    public JTextField textField1;
//
//    public Register() {
//        注册Button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//                if (textField1.getText().length() == 0 || !Pattern.compile("http://\\w+.\\w+.\\w+:\\d+/register").matcher(textField1.getText()).find()) {
//                    textField1.setText("请输入正确网址");
//                    return;
//                }
//
//                if ("正在注册请等待...".equalsIgnoreCase(textField1.getText())) {
//                    return;
//                }
//                try {
//                    URL url = new URL(textField1.getText());
//                    textField1.setText("正在注册请等待...");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.setUseCaches(false);
//                    connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
//                    connection.setRequestMethod("POST");
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                    connection.connect();
//
//
//                    String len = reader.readLine();
//                    StringBuilder sb = new StringBuilder();
//                    while (len != null) {
//                        sb.append(len);
//
//                        len = reader.readLine();
//
//                    }
//
//                    map.put("key", sb.toString());
//                    textField1.setText("注册成功，请等待程序启动,谢谢支持!!");
//                    reader.close();
//
//                } catch (Exception e1) {
//                    System.out.println(e1);
//                }
//
//
//            }
//
//
//        });
//        textField1.addMouseListener(new MouseAdapter() {
//        });
//        textField1.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                if ("请输入正确网址".equals(textField1.getText()) || "正在注册请等待..".equals(textField1.getText())) {
//                    textField1.setText("");
//                }
//
//            }
//        });
//    }
//
//    private void createUIComponents() {
//
//    }
//}
