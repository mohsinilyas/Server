/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.util.List;

/**
 *
 * @author IL
 */
public class WorkerObject {
    
    private int clientID;
    private InetAddress ia;
    private int port;
    private char[] keyStartRange;
    private char[] keyEndRange;
    private List<State> state;
    
    WorkerObject() {
        clientID = 0;
        port = 0;
        keyStartRange = new char[5];
        keyEndRange = new char[5];
    }

    /**
     * @return the clientID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @param clientID the clientID to set
     */
    public void setClientID(int clientID) {
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
    public char[] getKeyStartRange() {
        return keyStartRange;
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
    public char[] getKeyEndRange() {
        return keyEndRange;
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
    
    public enum State {
        DONE_NOT_FOUND(0),
        DONE_FOUND(1),
        NOT_DONE(2),
        TIMED_OUT(3),
        IDLE(4);
        
        private int code;
        private State(int code) {
            this.code = code;
        }
        
        public int getCode() {
            return this.code;
        }
    }
    
}
