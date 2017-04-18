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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class UDPConnection {
        DatagramSocket ds;
       
    //For server    
    UDPConnection(int port) {
            try {
                ds = new DatagramSocket(port);
            } catch (SocketException ex) {
                Logger.getLogger(UDPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    //For worker and request 
    UDPConnection() {
            try {
                ds = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(UDPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public DatagramPacket receive(byte[] b) {
        DatagramPacket dp = new DatagramPacket(b, b.length);
            try {
                ds.receive(dp);
            } catch (IOException ex) {
                Logger.getLogger(UDPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        return dp;
    }
    
    public void send(byte[] b, InetAddress ia, int port) {
        DatagramPacket dp = new DatagramPacket(b, b.length, ia, port);
            try {
                ds.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(UDPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
