package org.finace.utils.test;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ness on 2016/12/23.
 */
public class TcpClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("hello server");
        bw.flush();
        socket.shutdownOutput();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String len = "";
        StringBuilder builder = new StringBuilder();
        while ((len = br.readLine()) != null) {
            builder.append(len);
        }
        System.out.println(builder.toString());
        bw.close();
        br.close();
        socket.close();
    }
}
