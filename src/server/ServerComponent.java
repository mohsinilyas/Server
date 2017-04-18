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
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IL
 */
public class ServerComponent implements Runnable {
    
    private Server server;
    private int min;
    private int max;
    private Message message;
    private CommandType command;
    private DatagramPacket dp;
    private int magicNo;
    private boolean commandIsLegal;
    private List<RequestObject> requestList;
    private List<WorkerObject> workerList;
    private List<Integer> randomNumberList;
    
    ServerComponent(DatagramPacket dp) {
        this.server = Server.getInstance();
        this.command = CommandType.PING;
        this.magicNo = server.getMagicNo();
        this.message = new Message(this.magicNo);
        this.commandIsLegal = false;   
        this.dp = dp;
    }
    
    //@override
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
    
    
    
    public synchronized void compute() {
        
        getList();
        
        boolean found = false; // refers to true if worker/request client is already in list
        int clientID = message.getClientID();
        
        // check if it exists in the queue and it is a request client
        for(RequestObject req : requestList) {
            if ( req.getClientID() == clientID ) {
                found = true;
                requestResponse(req);
                break;
            }
        }
                
        // check if it exists in the queue and it is a worker client
        for(WorkerObject work : workerList) {
            if( work.getClientID() == clientID ) {
                found  = true;
                workerResponse(work);
                break;
            }
        }
        
        // if this request is not in both of the queues
        if(!found) {
            
            int commandNumber = message.getCommandNo();
            
            if ( commandNumber == CommandType.REQUEST_TO_JOIN.getCode() ) { // refers to new worker client
                newWorker();
            } else if ( commandNumber == CommandType.HASH.getCode() ) { // refers to new request client 
                newRequest();
            }         
        } 
    }
    
    
    public void requestResponse(RequestObject req) {
        
        System.out.println("Ignoring the ping from request client, because worker clients are still computing the result");
        
    }
    
    public void workerResponse(WorkerObject worker) {
        
        // implementation required
        switch(message.getCommandNo()) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        
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
        
        addWorkerList(worker);
        
    }
    
    public void newRequest() {
        
        int ID;
        
        //Entertain request only when workers are available
        if(!workerList.isEmpty()) {
            RequestObject request = new RequestObject();
            ID = generateRandomNumber();
            
            request.setClientID(ID);
            request.setIa(dp.getAddress());
            request.setPort(dp.getPort());
            addRequestList(request);
            
            divideWorkAndSend();
            
        } else {
            System.out.println("New Request to compute hash has been declined due to unavailability of workers");
        }
        
    }
    
    public void divideWorkAndSend() {
        
        // implementation required
        int totalValue = 62; // refers to values from 0-9 and A-Z and a-z
        int presentWorkers = workerList.size();
        int division = totalValue/presentWorkers;
        List<Integer> list = new ArrayList<>();
        
        if( totalValue % presentWorkers != 0 ) {
            int minusValue = 0;
            
            do {
                minusValue = totalValue - division;
                if( totalValue > minusValue && division < minusValue) {
                    list.add(division);
                    totalValue = minusValue;
                } else {
                    list.add(totalValue);
                    totalValue -= totalValue;
                }      
            } while ( totalValue != 0 );   
        } else {
            do {
                list.add(division);
                totalValue -= division;
            } while ( totalValue != 0);
        }
        char startLetter = '0';
        char endLetter = '0';
        for ( int i = 0; i < presentWorkers; i++) {
            division = list.remove(i);
            for( int j=1; j <= division; j++) {
                if( endLetter == 57 ) {
                    endLetter = (char) (endLetter + 8);
                } else if ( endLetter == 90 ) {
                    endLetter = (char) (endLetter + 7);
                } else {
                    endLetter = (char) (endLetter + 1);
                }
            }
        }
        
    }
    
    public int generateRandomNumber() {
        
        boolean random = false;
        int number = 0;
        
        while(!random) {
            number = ThreadLocalRandom.current().nextInt(min, max + 1);
            if(!randomNumberList.contains(number)) {
                random = true;
            }
        }
        
        addRandomList(number);
        
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
    
    public void getList() {
        requestList = server.getRequestList();
        workerList = server.getWorkerList();
        randomNumberList = server.getRandomNumber(); 
    }

    /**
     * @param worker the workerList to set
     */
    public void addWorkerList(WorkerObject worker) {
        workerList.add(worker);
    }


    /**
     * @param request the requestList to set
     */
    public void addRequestList(RequestObject request) {
        requestList.add(request);
    }
    
    /**
     * @param number the randomNumberList to set
     */
    public void addRandomList(int number) {
        randomNumberList.add(number);
    }
    
}
