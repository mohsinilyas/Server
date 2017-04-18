/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class Receiver {
    
    private boolean shutDown;
    private Server server;
    private DatagramPacket dp;
    private BlockingQueue<DatagramPacket> queue;
    
    public Receiver() {
        this.server = Server.getInstance();
        this.shutDown = server.getShutDown();
        queue = server.getBlockingQueue();
    }
    
    public void run() {
        while(!shutDown) {
            try {
                dp = queue.take();
                ServerComponent component = new ServerComponent(dp);
                new Thread(component).start();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
