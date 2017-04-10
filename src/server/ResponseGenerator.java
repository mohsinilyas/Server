/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author IL
 */
public class ResponseGenerator {
    
    Server server;
    
    ResponseGenerator() throws SocketException {
        server = new Server();
    }
    
    public void computeCommand(Message message, CommandType command, DatagramPacket dp) { 
        
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
                    workerResponse(command, message, dp);
                    break;
                default:
                    break;
            }
        } else {
            switch(clientName) {
                case "Request":
                    newRequest(command, message, dp);
                    break;
                case "Worker":
                    newWorker(command, message, dp);
                    break;
                default:
                    break;
            }
        }
    }
    
    public void workerResponse(CommandType command, Message message, DatagramPacket dp) {
        
        
    }
    
    public void newWorker(CommandType command, Message message, DatagramPacket dp) {
        
    }
    
    public void newRequest(CommandType command, Message message, DatagramPacket dp) {
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
            
            
        } else {
            System.out.println("New Request to compute hash has been declined due to unavailability of workers");
        }
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
