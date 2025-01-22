/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Srecko
 */
public class Pokreni_Server extends Thread {

    private volatile boolean running = true; // Dodata promenljiva za kontrolu rada servera
    private ServerSocket serverskiSoket;

    @Override
    public void run() {

        try {
            serverskiSoket = new ServerSocket(9000);
            System.out.println("Server je pokrenut.");

            while (running) {
                try {
                    Socket soket = serverskiSoket.accept();
                    System.out.println("KLIJENT JE POVEZAN!");
                    
                    Obrada_Klijentskih_Zahteva nit = new Obrada_Klijentskih_Zahteva(soket);
                    nit.start();
                    
                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Server je zaustavljen.");
                    } else {
                        throw e; // Prosledi grešku ako nije uzrokovana zatvaranjem
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Pokreni_Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (serverskiSoket != null && !serverskiSoket.isClosed()) {
                    serverskiSoket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Pokreni_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void stopServer() {
        running = false; // Postavljanje running na false
        try {
            if (serverskiSoket != null) {
                serverskiSoket.close(); // Zatvaranje ServerSocket-a da prekine accept()
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    
    
}
