/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.SocketException;

/**
 *
 * @author IL
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {
        // TODO code application logic here
        Server server = Server.getInstance() ;
        server.openSocket();
        while(!server.shutDown) {
            System.out.println("Wait");
        }
  
    }
    
}
