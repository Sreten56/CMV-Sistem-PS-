/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AMKlub;
import model.CVM;
import model.Drzava;
import model.Kategorija;
import operacije.Operacije;
import pomocne.Pomocne;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;

/**
 *
 * @author Srecko
 */
public class Obrada_Klijentskih_Zahteva extends Thread {

    private Socket soket;

    public Obrada_Klijentskih_Zahteva(Socket soket) {
        this.soket = soket;
    }

    @Override
    public void run() {

        while (true) {

            Klijentski_Zahtev kz = primiZahtev();
            Serverski_Odgovor so = new Serverski_Odgovor();

            switch (kz.getOperacija()) {

                case Operacije.PRIJAVA:

                    if (kz.getParametar() instanceof CVM) {
                        CVM cvm = (CVM) kz.getParametar();
                        boolean prijavljen = Controller.getInstance().login(cvm.getEmail(), cvm.getLozinka());
                        if (prijavljen == true) {
                            so.setOdgovor("USPEH!");
                        } else {
                            so.setOdgovor("NEUSPEH!");
                        }
                    }
                    // boolean prijavljen = Controller.getInstance().login(kz.getCVM);
                    // so.setOdgovor(prijavljen);
                    break;

                case Operacije.REGISTRACIJA:

                    if (kz.getParametar() instanceof CVM) {
                        CVM cvm = (CVM) kz.getParametar();
                        String privremenaLozinka = Pomocne.generateTemporaryPassword();
                        cvm.setLozinka(privremenaLozinka);
                        boolean registrovan = Controller.getInstance().dodaj_CVM(cvm);
                        if (registrovan) {
                            Pomocne.posalji_Email(cvm.getEmail(), privremenaLozinka);
                            so.setOdgovor("USPEH!");
                        } else {
                            so.setOdgovor("NEUSPEH!");
                        }
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_NAZIV_CVM:
                    if (kz.getParametar() instanceof CVM) {
                        CVM cvm = (CVM) kz.getParametar();
                        String nazivCVM = Controller.getInstance().vrati_naziv_CVM(cvm.getEmail(), cvm.getLozinka());
                        so.setOdgovor(nazivCVM != null ? nazivCVM : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_OSNOVNE_PODATKE:
                    if (kz.getParametar() instanceof CVM) {
                        CVM cvm = (CVM) kz.getParametar();
                        List<CVM> podaci = Controller.getInstance().vrati_podatke(cvm.getEmail(), cvm.getLozinka());
                        so.setOdgovor(podaci != null ? podaci : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_SVE_CVM:
                    List<CVM> sviCVM = Controller.getInstance().vrati_sve_CVM();
                    so.setOdgovor(sviCVM != null ? sviCVM : "NEUSPEH!");
                    break;

                case Operacije.POPUNI_CB_DRZAVE:
                    try {
                        List<Drzava> sveDrzave = Controller.getInstance().getAllDrzave();
                        so.setOdgovor(sveDrzave != null ? sveDrzave : "NEUSPEH!");
                    } catch (Exception e) {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_SVE_PODATKE_CVM_DRZAVA:

                    if (kz.getParametar() instanceof CVM) {

                        CVM cvm = (CVM) kz.getParametar();
                        List<Object[]> sviPodaciCVM = Controller.getInstance().vrati_SVE_podatke(cvm.getEmail(), cvm.getLozinka());
                        //List<CVMDrzava> sviPodaciCVM = Controller.getInstance().vrati_SVE_podatke(cvm.getEmail(), cvm.getLozinka());

                        so.setOdgovor(sviPodaciCVM != null ? sviPodaciCVM : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_SVE_POTREBNE_INFO_ZA_TAKMICARA_KAT_I_AMK:
                    List<Object[]> sviPodaci = Controller.getInstance().vrati_SVE_potrebne_info_tak_kat_amk();
                    so.setOdgovor(sviPodaci != null ? sviPodaci : "NEUSPEH!");
                    break;

                case Operacije.VRATI_SVE_KATEGORIJE:
                    List<Kategorija> sveKategorije = Controller.getInstance().vrati_SVE_kategorije();
                    if (sveKategorije != null && !sveKategorije.isEmpty()) {
                        so.setOdgovor(sveKategorije);
                    } else {
                        so.setOdgovor("NEUSPEH!"); // Vraća poruku u slučaju greške ili prazne liste
                    }
                    break;
                    
                case Operacije.VRATI_SVE_AMK:
                    List<AMKlub> sviAMK = Controller.getInstance().vrati_sve_AMK();
                    if (sviAMK != null && !sviAMK.isEmpty()) {
                        so.setOdgovor(sviAMK);
                    }
                    else{
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                default:
                    System.out.println("GRESKA!");
                    so.setOdgovor("GRESKA!");
            }
            // so.setOdgovor("USPEH!");
            posaljiOdgovor(so);
        }
    }

    private Klijentski_Zahtev primiZahtev() {

        try {
            ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());
            return (Klijentski_Zahtev) ois.readObject();

        } catch (IOException ex) {
            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void posaljiOdgovor(Serverski_Odgovor so) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(soket.getOutputStream());
            oos.writeObject(so);
            oos.flush();

        } catch (IOException ex) {
            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
