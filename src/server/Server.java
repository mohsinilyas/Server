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

/**
 *
 * @author IL
 */
public class Server {

  
    Listener listen;
    private List<Integer> randomNumber;
    static volatile Server server = null;
    Message message;
    int magicNo;
    boolean shutDown = false;
    CommandType command;
    private List<RequestObject> requestList;
    private List<WorkerObject> workerList;
    
    Server() throws SocketException {
        this.magicNo = 15440;
        this.listen = new Listener(9999);
    }
    
    public static Server getInstance() throws SocketException {
        if( server == null ) {
            synchronized(Server.class) {
                if( server == null ){
                    server = new Server();
                }
            }       
        }
        return server;
    }
    
  
    public void openSocket() throws IOException {
        listen.startListen();
    }
    
    
    
    public void sendPing(DatagramPacket dp) throws IOException {
        Message message = new Message(15440);
        byte[] b;
        message.setClientID(55);
        message.setCommandNo(0);
        message.setHash(new char[32]);
        message.setKey_range_start("0000".toCharArray());
        message.setKey_range_end("0000".toCharArray());
        b = message.convertMessageObjectIntoBytes();
        listen.sendPacket(b, dp.getAddress(), dp.getPort());
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
}