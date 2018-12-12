package com.finace.miscroservice;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.utils.DesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static com.finace.miscroservice.commons.utils.DesUtil.KEY;

public class TCP {

    public static void main(String[] args) throws IOException {

        int index = 0;
        for (int i = 0; i < 10; i++) {
            try {
                Socket socket = new Socket("api.etongjin.net", 1020);
                OutputStream outputStream =
                        socket.getOutputStream();

                JSONObject jsonObject = new JSONObject();
                JSONObject json = new JSONObject();

                json.put("os", "android");
                json.put("androidid", "ed61c4f71c0f023d");
                json.put("imei", "99000763344671");
                json.put("mac", "ss");
                jsonObject.put("imei", json);
                jsonObject.put("uid", "");


                outputStream.write(DesUtil.encrypt(System.currentTimeMillis() + "_" + "a" + "_" + jsonObject.toJSONString(), KEY).getBytes());


                socket.shutdownOutput();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String len = null;
                while ((len = reader.readLine()) != null) {
                    System.out.println(len);
                    index++;
                }

            } catch (Exception e) {
                System.err.println(e);
            }
        }

        System.out.println(index);

    }

}
