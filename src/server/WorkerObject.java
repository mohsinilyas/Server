/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author IL
 */
public class WorkerObject extends ClientObject {
    
    private char[] clientID;
    private InetAddress ia;
    private int port;
    private char[] keyStartRange;
    private char[] keyEndRange;
    private List<State> state;  
    
    WorkerObject() {
        clientID = "0000".toCharArray();
        port = 0;
        keyStartRange = new char[5];
        keyEndRange = new char[5];
        state = new ArrayList<>();
    }

    /**
     * @return the clientID
     */
    public String getClientID() {
        return String.valueOf(clientID);
    }

    /**
     * @param clientID the clientID to set
     */
    public void setClientID(char[] clientID) {
        this.clientID = clientID;
    }

    /**
     * @return the ia
     */
    public InetAddress getIa() {
        return ia;
    }

    /**
     * @param ia the ia to set
     */
    public void setIa(InetAddress ia) {
        this.ia = ia;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the keyStartRange
     */
    public String getKeyStartRange() {
        return String.valueOf(keyStartRange);
    }

    /**
     * @param keyStartRange the keyStartRange to set
     */
    public void setKeyStartRange(char[] keyStartRange) {
        this.keyStartRange = keyStartRange;
    }

    /**
     * @return the keyEndRange
     */
    public String getKeyEndRange() {
        return String.valueOf(keyEndRange);
    }

    /**
     * @param keyEndRange the keyEndRange to set
     */
    public void setKeyEndRange(char[] keyEndRange) {
        this.keyEndRange = keyEndRange;
    }


    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state.add(state);
    }
    
    public State getState() {
        return state.get(state.size()-1); // return last index
    }
    
    public enum State {
        DONE_NOT_FOUND(0),
        DONE_FOUND(1),
        NOT_DONE(2),
        TIMED_OUT(3),
        IDLE(4),
        JOB_SENT(5);
        
        private int code;
        private State(int code) {
            this.code = code;
        }
        
        public int getCode() {
            return this.code;
        }
    }
    
    @Override
    public byte[] convertMessageIntoBytes(Properties prop) {
        
        Message message = new Message(15440);
        
        message.setClientID(prop.getProperty("clientID").toCharArray());
        message.setCommandNo(Integer.parseInt(prop.getProperty("commandNO")));
        message.setKey_range_start(prop.getProperty("startRange").toCharArray());
        message.setKey_range_end(prop.getProperty("endRange").toCharArray());
        message.setHash(prop.getProperty("hash").toCharArray());
        
        return message.convertMessageObjectIntoBytes();
    }
    
}
