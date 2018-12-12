package org.finace.utils.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Ness on 2017/1/2.
 */
public class NioFileCopy {
    public static void main(String[] args) throws IOException {
        FileChannel readChannel = new FileInputStream(new File("G:\\Users\\Ness\\Desktop\\new 1.txt")).getChannel();
        FileChannel wirteChannel = new FileOutputStream(new File("G:\\Users\\Ness\\Desktop\\newTest.txt")).getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10240);

        int len = 0;
        while ((len = readChannel.read(buffer)) != -1) {
            //将读状态变成写
            buffer.flip();
            wirteChannel.write(buffer);
            buffer.clear();
        }

        System.out.println("success...");
        readChannel.close();
        wirteChannel.close();

    }
}
