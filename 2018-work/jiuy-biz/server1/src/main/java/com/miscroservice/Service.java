package com.miscroservice;


import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Service {


    private int INT_PORT = 8080;

    public static void main(String[] args) {

        new Service().start();


    }


    /**
     * 解压zip
     *
     * @param zipFile zip的位置
     * @param dstPath 目标位置
     * @throws Exception
     */
    public static void unzip(String zipFile, String dstPath) throws Exception {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();

            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");

                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }

        }


    }

    public static void copyDir(String source, String dest) throws IOException {

        File file = new File(source);

        if (file == null) {
            return;
        }

        for (File tmp : file.listFiles()) {
            if (tmp.isDirectory()) {
                continue;
            }
            Files.copy(tmp.toPath(), new FileOutputStream(new File(dest, tmp.getName())));

        }


    }

    private void start() {

        startTomcat();
    }


    public void startTomcat() {
        Tomcat tomcat = null;
        try {
            String fileName = "";

            File[] files = Paths.get(".").toFile().listFiles();
            for (File file : files) {
                if (file.getName().endsWith("war")) {
                    fileName = file.getName();
                    break;
                }
            }

            if (fileName.isEmpty()) {
                System.out.println("current directory has no war");
                return;
            }

            String serverName = fileName.split("\\.")[0];

            String baseDir = System.getProperty("java.io.tmpdir") + File.separator + fileName.hashCode();
            System.out.printf("start unzip %s\n", fileName);

            unzip(fileName, baseDir);
            System.out.printf("start server baseDir %s \n", baseDir);

            tomcat = new Tomcat();
            tomcat.setPort(INT_PORT);
            tomcat.setBaseDir(baseDir);
            tomcat.setSilent(false);
            tomcat.addWebapp("/" + serverName, baseDir);
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {

            if (tomcat != null) {
                try {
                    tomcat.stop();
                } catch (LifecycleException e1) {

                }
            }
            System.exit(-1);
        }

    }


}
