package com.e_commerce.miscroservice.publicaccount.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Tests {
    public static void main(String[] args) throws FileNotFoundException {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAIcd52tIwHUmb9";
        String accessKeySecret = "ceqlIEb9n18pukurAGiR2XFGBMbrYp";

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);


        InputStream inputStream = new FileInputStream(new File(""));
        PutObjectResult putObjectResult = ossClient.putObject("yjj-img-new", "proxy/qrcode/" + "build.gradle", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
