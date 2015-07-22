/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.Servidor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thais
 */
public class ServidorMain {

    public static void main(String[] args) {
        Servidor servidor = new Servidor("10.4.6.39", 8000);

        try {
            servidor.estabeleceConexao();
        } catch (IOException ex) {
            Logger.getLogger(ServidorMain.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
}
