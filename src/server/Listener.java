/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author IL
 */
public class Listener {
    
    UDPConnection conn;
    int port;
    boolean shutDown;
    byte[] b;
    Server server;
    
    
    Listener(int port) throws SocketException {
        this.port = port;
        this.conn = new UDPConnection(port);
        this.b = new byte[1024];
        this.shutDown = false;
    }
    
    public void startListen() throws IOException {
        server = Server.getInstance();
        shutDown = server.shutDown;   
        while(!shutDown) {
            byte[] bArr = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bArr, bArr.length);
            System.out.println("Listening for Packets on port "+port);
            dp = conn.receive(b);
            System.out.println("Packet received");
            ServerComponent component = new ServerComponent(dp);
            new Thread(component).run();
        }
    }
    
    public void sendPacket(byte[] b, InetAddress ia, int port) throws IOException {       
        conn.send(b, ia, port);
    }
    
    public void openSocket(int port) {
        this.port = port;
    }
     
}
