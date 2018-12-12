package org.finace.utils.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ness on 2016/12/23.
 */
public class TcpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket accept = serverSocket.accept();
        InputStream is = accept.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String len = "";
        StringBuilder build = new StringBuilder();
        while ((len = br.readLine()) != null) {
            build.append(len);
        }
        accept.shutdownInput();
        System.out.println(build.toString());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        bw.write("hello client");
        bw.flush();
        bw.close();
        br.close();

    }
}
