/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author IL
 */
public class UDPConnection {
        DatagramSocket ds;
       
    //For server    
    UDPConnection(int port) throws SocketException {
        ds = new DatagramSocket(port);
    }
    
    //For worker and request 
    UDPConnection() throws SocketException {
        ds = new DatagramSocket();
    }
    
    public DatagramPacket receive(byte[] b) throws IOException {
        DatagramPacket dp = new DatagramPacket(b, b.length);
        ds.receive(dp);
        return dp;
    }
    
    public void send(byte[] b, InetAddress ia, int port) throws IOException {
        DatagramPacket dp = new DatagramPacket(b, b.length, ia, port);
        ds.send(dp);
    }
}
