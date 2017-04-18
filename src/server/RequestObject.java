/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.util.Properties;

/**
 *
 * @author IL
 */
public class RequestObject extends ClientObject {
    private char[] clientID;
    private InetAddress ia;
    private int port;
    private char[] hash;
    
    RequestObject() {
        this.clientID = "0000".toCharArray();
        this.port = 0;        
        this.hash = new char[32];
    }
    
    /**
     * @return the clientID
     */
    public String getHash() {
        return String.valueOf(hash);
    }

    /**
     * @param ID the clientID to set
     */
    public void setHash(char[] hash) {
        this.hash = hash;
    }
    
    /**
     * @return the clientID
     */
    public String getClientID() {
        return String.valueOf(clientID);
    }

    /**
     * @param ID the clientID to set
     */
    public void setClientID(char[] ID) {
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
    
    public byte[] convertMessageIntoBytes(Properties prop) {
        Message message = new Message(Integer.parseInt(prop.getProperty("magicNO")));
        
        message.setClientID(prop.getProperty("clientID").toCharArray());
        message.setHash(prop.getProperty("hash").toCharArray());
        
        return message.convertMessageObjectIntoBytes();
    }

    
}
