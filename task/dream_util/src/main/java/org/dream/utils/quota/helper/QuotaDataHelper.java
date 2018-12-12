package org.dream.utils.quota.helper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.dream.model.order.FuturesContractsModel;
import org.dream.model.quota.SimpleQuota;
import org.dream.utils.prop.SpringProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dream.utils.quota.SimpleDateFormatUtil.*;

/**
 * Created by yhj on 16/10/27.
 */
public class QuotaDataHelper {

    private final static Logger LOG = LoggerFactory.getLogger(QuotaDataHelper.class);

    @Autowired
    SpringProperties springProperties;

    @Autowired
    RedisTemplate redisTemplate;

    private boolean isAuthorAccess = true;

    public void writeQuotaFile(String varietyType, SimpleQuota simpleQuota) {

        Double lastPrice = simpleQuota.getLastPrice();
        // 保存到分时图数据中
        String fileAddress = springProperties.getProperty("dream_quota.diagram") + "/" + varietyType
                + ".fst";
        // 创建时间
        Calendar calendar = Calendar.getInstance();
        String time = DATE_FORMAT5.format(calendar.getTime());
        time += "00";
        // 写入行情数据到fst文件中
        StringBuffer bf = new StringBuffer();
        bf.append(varietyType).append(",").append(lastPrice).append(",").append(time).append("|");
        try {
            File file = getFileAndSetAccess(fileAddress);
            //--------------读写权限----------------------

            FileUtils.write(file, bf.toString(), true);
        } catch (IOException e) {
            LOG.error("保存分时图失败 {}", e.getMessage());
        }
    }


    public static void sendReStartEmial(final Collection orderQuotaInfoModels, final int count) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                sentMail("navy9447@qq.com",
                        "行情获取失败提示",
                        "订阅的合约" + orderQuotaInfoModels + "恢复了" + count + "次了，请及时修复");
            }
        }).start();
    }


    public static void sentMail(String to, String title, String msg) {
        try {
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.setHostName("smtp.163.com");
            htmlEmail.setCharset("utf-8");
            htmlEmail.setFrom("15924179757@163.com", title);
            htmlEmail.setAuthenticator(new DefaultAuthenticator("15924179757@163.com", "s985595"));

            if (to.contains(";")) {
                for (String s : to.split(";")) {
                    if (s != null) {
                        htmlEmail.addTo(s, title, "utf-8");
                    }
                }
            }

            htmlEmail.setSubject(title).setMsg(msg);
            htmlEmail.send();
        } catch (EmailException e) {
            //Ignore
        }
    }


//    public void deleteDoubleVariety(String varietyType){
//        //删除以前重复的合约
//        for (Object object : Quota.newestQuota.entrySet().toArray()) {
//            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) object;
//            if ((!entry.getKey().equals(varietyType)) && entry.getKey().startsWith(varietyType.replaceAll("\\d+", ""))) {
//                Quota.newestQuota.remove(entry.getKey());
//                Quota.newestQuota.remove(entry.getKey() + SimpleQuota.SIMPLE_QUOTA_SUFFIX);
//                //同时删除以前redis中重复的合约
//                redisTemplate.opsForHash().delete(Quota.QUOTA_DATA_PREFIX, entry.getKey().split("_")[0]);
//                LOG.info("从缓存中删除旧的合约{}", entry.getKey().split("_")[0]);
//
//            }
//        }
//    }


    /**
     * 修改分时图名称
     *
     * @param varietyTypeSet 分时图的名称Set
     */
    public void renameForQuotaFile(Set<String> varietyTypeSet, boolean forceRename) {

        try {
            if (varietyTypeSet == null || varietyTypeSet.isEmpty()) {
                return;
            }
            for (String varietyType : varietyTypeSet) {
                varietyType = varietyType.replaceAll("\\d+", "");

                String fileAddress = springProperties.getProperty("dream_quota.diagram") + "/" + varietyType + ".fst";
                File file = getFileAndSetAccess(fileAddress);

                Date date = new Date();

                String p = file.getParent();
                File newFile = new File(p + File.separator + varietyType + DATE_FORMAT1.format(date) + ".fst");
                if (newFile.exists()) {//如果今天的文件已经存在，说明已经改过名字，不需要判断是否改名了。
                    LOG.info("{}分时图文件已存在,无需修改", varietyType);
                    continue;
                }
                if (file.exists()) {
                    Boolean needRename = forceRename;
                    if (!forceRename) {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.MINUTE, 2);
                        String code = MINUTES_FORMAT.format(c.getTime());
                        String fst = FileUtils.readFileToString(file);
                        if (StringUtils.isNoneBlank(fst)) {
                            // 一个分时图的数据格式为CL1611,43.8,20160917045800|CL1611,43.75,20160917045900|
                            Pattern pat = Pattern.compile(",\\d{8}" + code + "00\\|");
                            Matcher m = pat.matcher(fst);
                            if (m.find()) {

                                //如果在收到新开市消息后重启，新的fst里肯定不包含2分钟之后的数据，因此，也不会再改名.。
                                String fstPoint = m.group();
                                code = DATE_FORMAT5.format(date);
                                if (!fstPoint.contains(code)) {
                                    needRename = true;
                                }
                            }
                        }
                    }
                    if (needRename) {
                        if (file.renameTo(newFile)) {
                            LOG.info("{}分时图文件存在，名称修改完成", varietyType);
                        }
                    }

                }
                // 新建文件
                try {
                    file.createNewFile();
                    LOG.info("{}分时utils图文件创建完成", varietyType);
                    //------------------权限测试------------------
                    if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                        String cmd = springProperties.getProperty("quota.executor.commond");
                        Runtime.getRuntime().exec(cmd);
                        LOG.info("执行commond脚本：{},合约是{}", cmd, varietyType);
                    }
                    //------------------权限测试------------------

                } catch (IOException e) {
                    LOG.error("{}分时图文件[{}]创建失败", e.getMessage(), file.getPath(), e);
                }

            }
        } catch (Exception e) {
            LOG.warn("{}分时图名称修改出错", varietyTypeSet);
        }


    }

    private File getFileAndSetAccess(String fileAddress) {
        File file = new File(fileAddress);
        //--------------读写权限----------------------
        file.setExecutable(true);
        file.setReadable(true, false);
        file.setWritable(true, false);
        //--------------读写权限----------------------
        return file;
    }

    public String contracts2String(List<FuturesContractsModel> contractsModelList) {
        StringBuffer stringBuffer = new StringBuffer("");

        for (FuturesContractsModel model : contractsModelList) {
            stringBuffer.append(",").append(model.getContractsCode());
        }
        return stringBuffer.length() > 1 ? stringBuffer.substring(1) : "";
    }

    public boolean isAuthorAccess() {
        return isAuthorAccess;
    }

    public void setIsAuthorAccess(boolean isAuthorAccess) {
        LOG.warn(">>>>>>>>>>>>>>>>>>>isAuthorAccess设置为{},true表示保存数据,false表示不要保存数据",isAuthorAccess);
        this.isAuthorAccess = isAuthorAccess;
    }
}
