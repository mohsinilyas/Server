/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class Server {

  
    private Listener listen;
    private Receiver receiver;
    private List<Integer> randomNumber;
    private static volatile Server server = null;
    private Message message;
    private int magicNo;
    private boolean shutDown;
    private CommandType command;
    private List<RequestObject> requestList;
    private List<WorkerObject> workerList;
    private BlockingQueue<DatagramPacket> queue;
            
    Server() throws SocketException {
        this.shutDown = false;
        this.magicNo = 15440;
        queue = new ArrayBlockingQueue<>(20);
        requestList = new ArrayList<>();
        workerList = new ArrayList<>();
        randomNumber = new ArrayList<>();
    }
    
    public static Server getInstance() {
        if( server == null ) {
            synchronized(Server.class) {
                if( server == null ){
                    try {
                        server = new Server();
                    } catch (SocketException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }       
        }
        return server;
    }
    
    public void addBlockingQueue(DatagramPacket dp) {
        try {
            queue.put(dp);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BlockingQueue<DatagramPacket> getBlockingQueue() {
        return queue;
    }
    
    public void startListening() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    listen = new Listener(9999);
                    listen.startListen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }
    
    public void startServer() {
        startListening();
        startReceiving();
    }
    
    public void startReceiving() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    receiver = new Receiver();
                    listen.startListen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }
    
    public void sendPing(DatagramPacket dp) {
        Message message = new Message(15440);
        byte[] b;
        message.setClientID(55);
        message.setCommandNo(0);
        message.setHash(new char[32]);
        message.setKey_range_start("0000".toCharArray());
        message.setKey_range_end("0000".toCharArray());
        b = message.convertMessageObjectIntoBytes();
        try {
            listen.sendPacket(b, dp.getAddress(), dp.getPort());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the requestList
     */
    public List<RequestObject> getRequestList() {
        return requestList;
    }

    /**
     * @param requestList the requestList to set
     */
    public void setRequestList(RequestObject requestList) {
        this.requestList.add(requestList);
    }

    /**
     * @return the workerList
     */
    public List<WorkerObject> getWorkerList() {
        return workerList;
    }

    /**
     * @param workerList the workerList to set
     */
    public void setWorkerList(WorkerObject workerList) {
        this.workerList.add(workerList);
    }

    /**
     * @return the randomNumber
     */
    public List<Integer> getRandomNumber() {
        return randomNumber;
    }

    /**
     * @param randomNumber the randomNumber to set
     */
    public void setRandomNumber(Integer randomNumber) {
        this.randomNumber.add(randomNumber);
    }

    /**
     * @return the magicNo
     */
    public int getMagicNo() {
        return magicNo;
    }
    
    /**
     * @return the shutDown
     */
    public boolean getShutDown() {
        return shutDown;
    }

    /**
     * @param shutDown the shutdown to set
     */
    public void setShutDown(boolean shutDown) {
        this.shutDown = shutDown;
    }
}