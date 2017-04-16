/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class ServerComponent implements Runnable{
    
    Server server;
    List<Integer> randomNumber;
    int min;
    int max;
    Message message;
    CommandType command;
    DatagramPacket dp;
    int magicNo;
    boolean commandIsLegal;
    
    ServerComponent(DatagramPacket dp) throws SocketException {
        this.server = new Server();
        this.message = new Message(magicNo);
        this.command = CommandType.PING;
        this.magicNo = server.magicNo;
        this.commandIsLegal = false;
        this.dp = dp;
        
    }
    
    @Override
    public void run() { 
        try {
            receivedData();
            if(commandIsLegal == true) {
                compute();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void receivedData() throws IOException {
        
        System.out.println("Parsing the data");
        message.parseBytesIntoMessageObject(dp.getData());
        setCommandType(message.getCommandNo());
        if( command.getCode() == CommandType.ILLEGAL.getCode() ) {
            commandIsLegal = false;
            System.out.println("Illegal command"); 
        } else {
            commandIsLegal = true;
        }
        System.out.println("Value "+CommandType.PING.getCode());
        System.out.println("Password's hash is "+message.getHash());
    
    }
    
    public void compute() {
        
        boolean found = false;
        int clientID = message.getClientID();
        
        // check if it exists in the queue and it is a request client
        for(RequestObject req : getRequestList()) {
            if ( req.getClientID() == clientID ) {
                found = true;
                requestResponse(req);
                break;
            }
        }
                
        // check if it exists in the queue and it is a worker client
        for(WorkerObject work : getWorkerList()) {
            if( work.getClientID() == clientID ) {
                found  = true;
                workerResponse(work);
                break;
            }
        }
        
        // if this request is not in both the queues
        if(!found) {
            
            int commandNumber = message.getCommandNo();
            
            if ( commandNumber == CommandType.REQUEST_TO_JOIN.getCode() ) {
                newWorker();
            } else if ( commandNumber == CommandType.HASH.getCode() ) {
                newRequest();
            }         
        } 
    }
    
    
    public void requestResponse(RequestObject req) {
        
        System.out.println("Ignoring the ping from request client, because worker clients are still computing the result");
        
    }
    
    public void workerResponse(WorkerObject worker) {
        
        // implementation required
        
    }
    
    public void newWorker() {
        
        int ID = generateRandomNumber();
        WorkerObject worker     = new WorkerObject();
        char[] keyStartRange    = {0000};
        char[] keyEndRange      = {0000};
        
        worker.setClientID(ID);
        worker.setIa(dp.getAddress());
        worker.setPort(dp.getPort());
        worker.setKeyStartRange(keyStartRange);
        worker.setKeyEndRange(keyEndRange);
        worker.setState(WorkerObject.State.IDLE);
        
        randomNumber.add(ID);
        setWorkerList(worker);
        
    }
    
    public void newRequest() {
        
        int ID;
        
        //Entertain request only when workers are available
        if(!getWorkerList().isEmpty()) {
            RequestObject request = new RequestObject();
            ID = generateRandomNumber();
            
            request.setClientID(ID);
            request.setIa(dp.getAddress());
            request.setPort(dp.getPort());
            
            randomNumber.add(ID);
            getRequestList().add(request);
            
            divideWorkAndSend();
            
        } else {
            System.out.println("New Request to compute hash has been declined due to unavailability of workers");
        }
        
    }
    
    public void divideWorkAndSend() {
        
        // implementation required
    
    }
    
    public int generateRandomNumber() {
        
        boolean random = false;
        int number = 0;
        
        while(!random) {
            number = ThreadLocalRandom.current().nextInt(min, max + 1);
            if(!randomNumber.contains(number)) {
                random = true;
            }
        }
        
        randomNumber.add(number);
        
        return number;
        
    }
    
    public void setCommandType(int number) {
        
        switch(number) {
            case 0:
                command = CommandType.PING;
                break;
            case 1:
                command = CommandType.REQUEST_TO_JOIN;
                break;
            case 2:
                command = CommandType.JOB;
                break;
            case 3:
                command = CommandType.ACK_JOB;
                break;
            case 4:
                command = CommandType.DONE_NOT_FOUND;
                break;
            case 5:
                command = CommandType.DONE_FOUND;
                break;
            case 6:
                command = CommandType.NOT_DONE;
                break;
            case 7:
                command = CommandType.SHUTDOWN;
                break;
            case 8:
                command = CommandType.HASH;
                break;
            default:
                command = CommandType.ILLEGAL;
                break;
        }
        
    }

    /**
     * @return the workerList
     */
    public List<WorkerObject> getWorkerList() {
        return server.getWorkerList();
    }

    /**
     * @param worker the workerList to set
     */
    public void setWorkerList(WorkerObject worker) {
        server.setWorkerList(worker);
    }

    /**
     * @return the requestList
     */
    public List<RequestObject> getRequestList() {
        return server.getRequestList();
    }

    /**
     * @param request the requestList to set
     */
    public void setRequestList(RequestObject request) {
        server.setRequestList(request);
    }
    
    
}
