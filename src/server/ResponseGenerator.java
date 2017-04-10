/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author IL
 */
public class ResponseGenerator implements Runnable{
    
    Server server;
    List<Integer> randNum;
    int min;
    int max;
    List<RequestObject> requestList;
    List<WorkerObject> workerList;
    Message message;
    CommandType command;
    DatagramPacket dp;
    
    ResponseGenerator(Message message, CommandType command, DatagramPacket dp) throws SocketException {
        this.server = new Server();
        this.requestList = server.getRequestList();
        this.workerList = server.getWorkerList();
        this.message = message;
        this.command = command;
        this.dp = dp;
    }
    
    public void run() { 
        
        compute();
        Thread.currentThread().stop();
    }
    
    public void compute() {
        
        boolean found = false;
        String clientName = "";
        int commandNo = message.getCommandNo();
        
        for(RequestObject ls : requestList) {
            if ( ls.getID() == commandNo ) {
                found = true;
                clientName = "Request";
                break;
            }
        }
        
        for(WorkerObject ls : workerList) {
            if( ls.getID() == commandNo ) {
                found  = true;
                clientName = "Worker";
                break;
            }
        }
        
        if(found) {
            // we are ignoring the request client packet as it would be a ping
            switch(clientName) {
                case "Worker":
                    workerResponse();
                    break;
                default:
                    break;
            }
        } else {
            switch(clientName) {
                case "Request":
                    newRequest();
                    break;
                case "Worker":
                    newWorker();
                    break;
                default:
                    break;
            }
        } 
    }
    
    public void workerResponse() {
        
        
    }
    
    public void newWorker() {
        
    }
    
    public void newRequest() {
        int ID;
        
        //Entertain request only when workers are available
        if(!workerList.isEmpty()) {
            RequestObject obj = new RequestObject();
            ID = generateRandomNumber();
            
            obj.setID(ID);
            obj.setIa(dp.getAddress());
            obj.setPort(dp.getPort());
            
            randNum.add(ID);
            requestList.add(obj);
            
            divideWorkAndSend();
        } else {
            System.out.println("New Request to compute hash has been declined due to unavailability of workers");
        }
    }
    
    public void divideWorkAndSend() {
        
    }
    
    public int generateRandomNumber() {
        boolean random = false;
        int number = 0;
        
        while(!random) {
            number = ThreadLocalRandom.current().nextInt(min, max + 1);
            if(!randNum.contains(number)) {
                random = true;
            }
        }
        
        return number;
    }
}
