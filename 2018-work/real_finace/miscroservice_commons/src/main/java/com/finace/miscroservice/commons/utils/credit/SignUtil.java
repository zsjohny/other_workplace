package com.finace.miscroservice.commons.utils.credit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * 签名工具类
 *
 * @author jack
 */
@Configuration
public class SignUtil {
    private static String keys;
    private static String pass;
    private static String crt;

    @Value("${creditgo.keys}")
    public void setKeys(String keys) {
        SignUtil.keys = keys;
    }

    @Value("${creditgo.pass}")
    public void setPass(String pass) {
        SignUtil.pass = pass;
    }

    @Value("${creditgo.crt}")
    public void setCrt(String crt) {
        SignUtil.crt = crt;
    }


    public SignUtil() {

    }

    /**
     * 获取签名
     *
     * @param signStr 待签名字符串
     * @return
     * @throws Exception
     */
    public static String sign(String signStr) {

        System.out.println((new StringBuilder()).append("(P2P-->即信端)待签名字符串：").append(signStr)
                .toString());
        String sign = null;
        RSAHelper signer = null;
        try {
            //Signature sig = Signature.getInstance(SIGN_ALGORITHMS);

			RSAKeyUtil rsaKey = new RSAKeyUtil(new File(keys), pass);
//            RSAKeyUtil rsaKey = new RSAKeyUtil(new ClassPathResource("cert/yb_sit.p12").getFile(), pass);
            signer = new RSAHelper(rsaKey.getPrivateKey());

            sign = signer.sign(signStr);
        } catch (Exception e) {
            System.out.println("签名校验异常" + e.getMessage());
        }
        System.out.println((new StringBuilder()).append("(P2P-->即信端)签名:").append(sign)
                .toString());
        return sign;
    }

    /**
     * 签名校验
     *
     * @param signText 待验的签名
     * @param dataText 待签名字符串
     * @return
     */
    public static boolean verify(String signText, String dataText) {
        signText = signText.replaceAll("[\\t\\n\\r]", "");
        System.out.println((new StringBuilder()).append("(即信端-->P2P)待签名字符串：").append(dataText)
                .toString());
        System.out.println((new StringBuilder()).append("(即信端-->P2P)签名：").append(signText)
                .toString());

        boolean b = false;
        try {
            RSAKeyUtil ru = new RSAKeyUtil(new File(crt));
            RSAHelper signHelper = new RSAHelper(ru.getPublicKey());
            b = signHelper.verify(dataText, signText);
        } catch (Exception e) {
            System.out.println("签名校验异常" + e.getMessage());
        }

        return b;
    }

    public static void main(String[] args) {
        Path path = Paths.get(".");
        System.out.println();

    }

    /**
     * 拼接字符串
     *
     * @param map
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String mergeMap(Map map) {
        map = new TreeMap(map);
        String requestMerged = "";
        StringBuffer buff = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (!"SIGN".equalsIgnoreCase(entry.getKey())) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                    buff.append("");
                } else {
                    buff.append(String.valueOf(entry.getValue()));
                }
            }
        }
        requestMerged = buff.toString();
        return requestMerged;
    }
}
