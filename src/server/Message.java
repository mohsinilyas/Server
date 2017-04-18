/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author IL
 */
public class Message {
    
    private int magicNo;
    private char[] clientID ;
    private int commandNo;
    private char[] key_range_start;
    private char[] key_range_end;
    private char[] hash;
    
    int sizeOfMagicNumber = 5;
    int sizeOfClientID = 4;
    int sizeofCommandCode = 1;
    int sizeOfKey_Range_Start = 4;
    int sizeOfKey_Range_End = 4;
    int sizeOfHash = 32;
    
    Message(int magicNo) {
        this.magicNo = magicNo;
        this.clientID = new char[] {'0','0','0','0'};
        this.commandNo = 0;
        this.hash  = new char[32];
        this.key_range_start = new char[] {'0','0','0','0'};
        this.key_range_end = new char[] {'0','0','0','0'};
    } 
    

    /**
     * @return the magicNo
     */
    public int getMagicNo() {
        return magicNo;
    }

    /**
     * @param magicNo the magicNo to set
     */
    public void setMagicNo(int magicNo) {
        this.magicNo = magicNo;
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
     * @return the commandNo
     */
    public int getCommandNo() {
        return commandNo;
    }

    /**
     * @param commandNo the commandNo to set
     */
    public void setCommandNo(int commandNo) {
        this.commandNo = commandNo;
    }

    /**
     * @return the key_range_start
     */
    public String getKey_range_start() {
        return String.valueOf(key_range_start);
    }

    /**
     * @param key_range_start the key_range_start to set
     */
    public void setKey_range_start(char[] key_range_start) {
        this.key_range_start = key_range_start;
    }

    /**
     * @return the key_range_end
     */
    public String getKey_range_end() {
        return String.valueOf(key_range_end);
    }

    /**
     * @param key_range_end the key_range_end to set
     */
    public void setKey_range_end(char[] key_range_end) {
        this.key_range_end = key_range_end;
    }
    
    /**
     * @return the hash
     */
    public String getHash(){
        return String.valueOf(hash);
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(char[] hash) {
        this.hash = hash;
    }
    
    public byte[] convertMessageObjectIntoBytes(){

        String s = "";
        s += String.valueOf(getMagicNo());
        s += String.valueOf(getClientID());
        s += String.valueOf(getCommandNo());
        s += getKey_range_start();
        s += getKey_range_end();
        s += getHash();

        byte[] result = s.getBytes();
        return result;
    }
    
    public void parseBytesIntoMessageObject(byte[] bytes){

        String result;
        
        
        int start = 0;
       
        result = new String(bytes,0, sizeOfMagicNumber);
        this.setMagicNo(Integer.parseInt(result));
        start = sizeOfMagicNumber;
        
        result = new String(bytes, start, sizeOfClientID);
        this.setClientID(result.toCharArray());
        start+=sizeOfClientID;
        
        result = new String(bytes, start, sizeofCommandCode);
        this.setCommandNo(Integer.parseInt(result));
        start += sizeofCommandCode;
        
        result = new String(bytes,start, sizeOfKey_Range_Start);
        this.setKey_range_start(result.toCharArray());
        start+= sizeOfKey_Range_Start;
        
        result = new String(bytes,start, sizeOfKey_Range_End);
        this.setKey_range_end(result.toCharArray());
        start+= sizeOfKey_Range_End;
      
        result = new String(bytes,start, sizeOfHash);
        this.setHash(result.toCharArray());
        
    }
}