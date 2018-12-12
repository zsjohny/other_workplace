/**
 *
 */
package com.ouliao.util;

import com.ouliao.controller.versionfirst.OuLiaoSayController;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.service.versionfirst.OuLiaoService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author xiaoluo
 * @version $Id: DownloadController.java, 2016年3月5日 上午11:13:00
 */
@Controller
@RequestMapping(value = "user/download")
public class DownloadController {
    @Autowired
    private OuLiaoService ouLiaoService;

    // 下载
    @RequestMapping(value = "setUser/{uid}/{currentTime}/{downType}/download")

    public ResponseEntity<byte[]> download(@PathVariable("uid") String uid, @PathVariable("downType") String downType,
                                           @RequestParam(value = "name", defaultValue = "985595", required = false) String name, HttpServletResponse response) {
        User user = null;
        if (uid.matches("\\d+")) {
            user = ouLiaoService.queryUserByUserId(Integer.parseInt(uid));
            if (user == null) {
                return null;
            }
            uid = user.getUserNum();
        }

        // File file = new File("D:\\ouliao\\user\\" + downType + File.separator + uid + File.separator);
        File file = new File("/opt/ouliao/" + downType + File.separator + uid + File.separator);

        if ("head".equals(downType)) {

            if (!file.exists() || file.listFiles().length == 0) {
                try {
                    BufferedInputStream bis = new BufferedInputStream(
                            OuLiaoSayController.class.getClassLoader().getResourceAsStream("defaullt.jpg"));
                    BufferedOutputStream fos = new BufferedOutputStream(response.getOutputStream());

                    byte[] bs = new byte[1024];
                    int len = 0;
                    while ((len = bis.read(bs)) != -1) {
                        fos.write(bs, 0, len);
                    }
                    fos.close();
                    bis.close();
                    return null;
                } catch (Exception e) {
                }
            }
            File[] files = file.listFiles();
            try {
                for (File fe : files) {
                    if (fe.exists() && fe.getName().equals(user.getUserHeadPic())) {
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fe));
                        BufferedOutputStream fos = new BufferedOutputStream(response.getOutputStream());

                        byte[] bs = new byte[1024];
                        int len = 0;
                        while ((len = bis.read(bs)) != -1) {
                            fos.write(bs, 0, len);
                        }
                        fos.close();
                        bis.close();
                    }
                }
            } catch (Exception e) {
            }

        }

        if ("record".equals(downType)) {
            if (!file.exists()) {
                return null;
            }
            try {
                HttpHeaders headers = new HttpHeaders();
                // 设定格式
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                headers.setContentDispositionFormData("attachment",
                        new String(file.listFiles()[0].getName().getBytes("utf-8"), "iso8859-1"));

                // 转移下载数据
                byte[] bs = null;

                bs = FileUtils.readFileToByteArray(file.listFiles()[0]);

                return new ResponseEntity<byte[]>(bs, headers, HttpStatus.OK);

            } catch (Exception e) {

            }
        }


        if ("discovery".equals(downType)) {


            try {

                // 转移下载数据
                file = new File(file.getPath() + File.separator + name);
                if (!file.exists()) {
                    return null;
                }

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream fos = new BufferedOutputStream(response.getOutputStream());

                byte[] bs = new byte[1024];
                int len = 0;
                while ((len = bis.read(bs)) != -1) {
                    fos.write(bs, 0, len);
                }
                fos.close();
                bis.close();

            } catch (Exception e) {

            }
        }


        return null;
    }

    // 下载
    @RequestMapping(value = "download")
    public ResponseEntity<byte[]> downloadStore(HttpServletResponse response) {
        File file = new File("D:\\ouliao\\download\\ouliao.apk");
        try {
            HttpHeaders headers = new HttpHeaders();
            // 设定格式
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            headers.setContentDispositionFormData("attachment",
                    new String(file.getName().getBytes("utf-8"), "iso8859-1"));

            // 转移下载数据
            byte[] bs = null;

            bs = FileUtils.readFileToByteArray(file);

            return new ResponseEntity<byte[]>(bs, headers, HttpStatus.OK);

        } catch (Exception e) {

        }
        return null;

    }

}
