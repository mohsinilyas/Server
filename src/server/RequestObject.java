/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;

/**
 *
 * @author IL
 */
public class RequestObject {
    private int clientID;
    private InetAddress ia;
    private int port;
    
    RequestObject() {
        this.clientID = 0;
        this.port = 0;        
    }

    /**
     * @return the clientID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @param ID the clientID to set
     */
    public void setClientID(int ID) {
        this.clientID = ID;
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
    
    
    
}
