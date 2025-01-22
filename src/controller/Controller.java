/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import baza.DB_Broker;
import java.util.ArrayList;
import java.util.List;
import model.AMKlub;
import model.CVM;
import model.Drzava;
import model.Kategorija;
import transfer.Klijentski_Zahtev;

/**
 *
 * @author Srecko
 */
public class Controller {

    private static Controller instance;
    private DB_Broker dbb;

    List<CVM> podaciCVM;

    private Controller() {

        dbb = new DB_Broker();
        podaciCVM = new ArrayList<>();

    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public List<CVM> getPodaciCVM() {
        return podaciCVM;
    }

    public void setPodaciCVM(List<CVM> podaciCVM) {
        this.podaciCVM = podaciCVM;
    }

    public boolean login(String email, String lozinka) {

        return dbb.login(email, lozinka);

    }

    public boolean dodaj_CVM(CVM cvm) {
        return dbb.dodaj_CVM(cvm);
    }

    public String vrati_naziv_CVM(String email, String lozinka) {

        return dbb.vrati_naziv_CVM(email, lozinka);
    }

    public List<CVM> vrati_podatke(String email, String lozinka) {
        return dbb.vrati_podatke(email, lozinka);
    }

    public List<CVM> vrati_sve_CVM() {
        return dbb.vrati_sve_CVM();
    }

//    public List<CVM> vrati_SVE_podatke(String email, String lozinka) {
//        return dbb.vrati_SVE_podatke(email, lozinka);
//    }
    
    public List<Object[]> vrati_SVE_podatke(String email, String lozinka) {
        return dbb.vrati_SVE_podatke(email, lozinka);
    }

    public List<Drzava> getAllDrzave() throws Exception {
        return dbb.getAllDrzave();
    }

    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
        return dbb.vrati_SVE_potrebne_info_tak_kat_amk();
    }

    public List<Kategorija> vrati_SVE_kategorije() {
        return dbb.vrati_SVE_kategorije();
    }

    public List<AMKlub> vrati_sve_AMK() {
        return dbb.vrati_sve_AMK();
    }
}
