/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Properties;

/**
 *
 * @author milyas
 */
public abstract class ClientObject {
    public abstract byte[] convertMessageIntoBytes(Properties prop);
}
