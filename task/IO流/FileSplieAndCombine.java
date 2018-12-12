package org.finace.utils.db;

import java.io.*;

/**
 * Created by Ness on 2016/12/22.
 */
public class FileSplieAndCombine {

    /**
     * 划分File
     *
     * @throws IOException
     */
    private static void getSpiltFIle() throws IOException {
        File file = new File("G:\\test\\test\\test.zip");
        if (!file.isFile()) {
            System.out.println("error");
            return;
        }

        long total = file.length();
        int splitFIleCount = 4;
        InputStream is = new FileInputStream(file);
        for (int i = 0; i < splitFIleCount; i++) {
            File saveFile = new File("G:\\test\\save\\" + i + ".zip");
            OutputStream os = new FileOutputStream(saveFile);
            byte[] b = new byte[1024];
            int len = 0;

            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
                if (saveFile.length() > total / splitFIleCount) {
                    break;

                }
            }
            os.close();
        }


        is.close();
    }

    /**
     * 合并File
     */
    private static void combineFile() throws IOException {
        File file = new File("G:\\test\\save");
        if (!file.isDirectory()) {
            System.out.println("error");
            return;
        }


        File[] files = file.listFiles();

        File saveFile = new File("G:\\test", "success.zip");

        OutputStream os = new FileOutputStream(saveFile);
        InputStream is;

        for (File fe : files) {
            if (!fe.exists()) {
                break;

            }
            is = new FileInputStream(fe);

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }

            is.close();
        }
        os.close();
        System.out.println("success");
    }

    public static void main(String[] args) throws IOException {
        combineFile();

    }

}
