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

  
    ServerComponent listen;
    
    static volatile Server server = null;
    Message message;
    int magicNo = 15440;
    boolean shutDown = false;
    CommandType command;
    private List<RequestObject> requestList;
    private List<WorkerObject> workerList;
    
    Server() throws SocketException {
        message = new Message(magicNo);
        listen = new ServerComponent(9999);
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
    
    public void receivedData(DatagramPacket dp) throws IOException {
        System.out.println("Parsing the data");
        message.parseBytesIntoMessageObject(dp.getData());
        setCommandType(message.getCommandNo());
        if(command.getCode() != 9) {
            System.out.println("Computing the command");
            ResponseGenerator resGen = new ResponseGenerator(message, command, dp);
            new Thread(resGen).run();
        } else {
            System.out.println("Illegal command");
        }
        System.out.println("Value "+command.PING.getCode());
        System.out.println("Password's hash is "+message.getHash());
    }
    
    public void openSocket() throws IOException {
        listen.startListen();
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
    public void setRequestList(List<RequestObject> requestList) {
        this.requestList = requestList;
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
    public void setWorkerList(List<WorkerObject> workerList) {
        this.workerList = workerList;
    }
}