package org.finace.utils.test;

import java.io.IOException;
import java.net.*;

/**
 * Created by Ness on 2016/12/23.
 */
public class DatagramClient {

    public static void main(String[] args) throws IOException {

        InetAddress inet = InetAddress.getLocalHost();

        SocketAddress address = new InetSocketAddress(inet, 9999);

        byte[] b = "hello server".getBytes();

        DatagramPacket packet = new DatagramPacket(b, b.length, address);


        DatagramSocket socket = new DatagramSocket();

        //向服务器发送数据
        socket.send(packet);


        //接受数据
        socket.receive(packet);

        String result = new String(b, 0, packet.getLength());

        System.err.print("serverResp: "+result + "\n");


    }

}
