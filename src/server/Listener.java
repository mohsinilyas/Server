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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class Listener implements Runnable{
    
    private UDPConnection conn;
    private int port;
    private boolean shutDown;
    private byte[] b;
    private Server server;
    
    
    Listener(int port) {
        this.port = port;
        this.conn = new UDPConnection(port);
        this.b = new byte[1024];
        this.shutDown = false;
    }
    
    @Override
    public void run() {
        try {
            startListen();
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startListen() throws IOException {
        server = Server.getInstance();
        shutDown = server.getShutDown();   
        while(!shutDown) {
            byte[] bArr = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bArr, bArr.length);
            System.out.println("Listening for Packets on port "+port);
            dp = conn.receive(b);
            System.out.println("Packet received");
            server.addBlockingQueue(dp);
        }
    }
    
    public void sendPacket(byte[] b, InetAddress ia, int port) throws IOException {       
        conn.send(b, ia, port);
    }
    
    public void openSocket(int port) {
        this.port = port;
    }
     
}
