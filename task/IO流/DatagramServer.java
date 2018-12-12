package org.finace.utils.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by Ness on 2016/12/23.
 */
public class DatagramServer {

    public static void main(String[] args) throws IOException {
        SocketAddress address = new InetSocketAddress(9999);
        DatagramSocket socket = new DatagramSocket(address);

        byte[] b = new byte[1024];

        DatagramPacket packet = new DatagramPacket(b, b.length);

        //接受数据
        socket.receive(packet);
        String result = new String(b, 0, packet.getLength());

        System.err.print("clientResp: ");
        System.out.print(result + "\n");

        packet.setData("hello client".getBytes());
        socket.send(packet);


        socket.close();


    }

}
