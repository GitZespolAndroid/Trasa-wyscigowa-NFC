package com.example.jt.nfcaplikacja4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Trasa {

    /// ID występujące w komentarzach do indeksowania danych w talbicy obiektow (metoda niżej)
    private int id; //id trasy ID:1
    private String nazwa; //nazwa trasy ID:2
    private double distanceKm; //dystans trasy w km ID:3
    private double distanceMile; //dystans trasy w milach ID:4
    private String distance; //łancuch w milach/km zależnie od pola km(t/f), gdy trasa niesprawdzona "nieznany" ID:5
    private boolean km; //km(T) czy mile(F) ID:6

    private String[] tablicaKodowNFC; //tablica naklejek w ułożonych w kolejnosci wg. trasy ID:7

    private String najlepszyUżytkownik; //nazwa mistrza trasy ID:8


    private String najlepszyCzas; // czas mistrza trasy ID:9

    private static int nextId =1; //do przyznawania kolejnych ID
    private final static double ZAMIANA_JEDNOSTEK = 1.61;
    private final static String znacznik  = "@Sign@"; //oddzielanie


    private PrintWriter zapisPliku; // strumień do zapisu infomracji o trasie w pliku
    private static Scanner odczytPliku; //strumień do odczytu informacji o trasie z pliku

    //Konstruktor domyślny
    public Trasa() {
        this.id = nextId;
        this.nazwa = "Bez nazwy";
        this.distanceKm = 0;
        this.distanceMile = 0;
        this.distance = "brak danych";
        this.km = true;
        this.tablicaKodowNFC = new String[1];
        this.najlepszyUżytkownik = "Nikt nie przebiegł jeszcze tej trasy";
        this.najlepszyCzas = "Brak";
        this.zapisPliku = null;
        nextId++;
    }




    //Konstruktur z tablicy obiektow - oczyt
    public Trasa(int id, String nazwa, double distanceKm, double distanceMile, String distance, boolean km, String[] tablicaKodowNFC, String najlepszyUżytkownik, String najlepszyCzas) {
        this.id = id;
        this.nazwa = nazwa;
        this.distanceKm = distanceKm;
        this.distanceMile = distanceMile;
        this.distance = distance;
        this.km = km;
        this.tablicaKodowNFC = tablicaKodowNFC;
        this.najlepszyUżytkownik = najlepszyUżytkownik;
        this.najlepszyCzas = najlepszyCzas;

    }

    // Gettery Settery///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */

    public String getNazwa() {
        return nazwa;
    }

    /**
     *
     * @param nazwa
     */

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    /**
     *
     * @return
     */

    public double getDistanceKm() {
        return distanceKm;
    }

    /**
     *
     * @param distanceKm
     */

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    /**
     *
     * @return
     */

    public double getDistanceMile() {
        return distanceMile;
    }

    /**
     *
     * @param distanceMile
     */

    public void setDistanceMile(double distanceMile) {
        this.distanceMile = distanceMile;
    }

    /**
     *
     * @return
     */

    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     */

    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     */

    public boolean isKm() {
        return km;
    }

    /**
     *
     * @param km
     */

    public void setKm(boolean km) {
        this.km = km;
    }

    /**
     *
     * @return
     */

    public String[] getTablicaKodowNFC() {
        return tablicaKodowNFC;
    }

    /**
     *
     * @param tablicaKodowNFC
     */

    public void setTablicaKodowNFC(String[] tablicaKodowNFC) {
        this.tablicaKodowNFC = tablicaKodowNFC;
    }

    /**
     *
     * @return
     */

    public String getNajlepszyUżytkownik() {
        return najlepszyUżytkownik;
    }

    /**
     *
     * @param najlepszyUżytkownik
     */

    public void setNajlepszyUżytkownik(String najlepszyUżytkownik) {
        this.najlepszyUżytkownik = najlepszyUżytkownik;
    }

    /**
     *
     * @return
     */

    public String getNajlepszyCzas() {
        return najlepszyCzas;
    }

    /**
     *
     * @param najlepszyCzas
     */

    public void setNajlepszyCzas(String najlepszyCzas) {
        this.najlepszyCzas = najlepszyCzas;
    }

    /**
     *
     * @param nextId
     */


    public static void setNextId(int nextId) {
        Trasa.nextId = nextId;
    }

    /**
     *
     * @param miles
     * @return
     */

    /// Reszta metod

    //Zamiana jednostek

    /**
     *
     * @param miles
     * @return
     */
    public static double zamianaNaKm(double miles) {
        return (miles*ZAMIANA_JEDNOSTEK);
    }

    /**
     *
     * @param km
     * @return
     */
    public static double zamianaNaMile(double km) {
        return (km/ZAMIANA_JEDNOSTEK);
    }

    //metoda zwracająca String z informacjami o trasie oddzielonymi znacznikami

    /**
     *
     * @return
     */
    public String getPrintString() {
        String znacznik = ")@(*%&$_";
        String koncowy="";
        for (int i=0; i<tablicaKodowNFC.length; i++) {
            koncowy+=tablicaKodowNFC[i]+znacznik;
        }
        return koncowy;
    }

    //metoda zwracajaca tablice obiektow dajacych informacje o trasie- potrzebne do zapisu trasy

    /**
     *
     * @return
     */
    public Object[] utworzenieTablicyZInformacjiami() {
        Object[] tablicaDanych = new Object[9];
        tablicaDanych[0] = id;
        tablicaDanych[1] = nazwa;
        tablicaDanych[2] = distanceKm;
        tablicaDanych[3] = distanceMile;
        tablicaDanych[4] = distance;
        tablicaDanych[5] = km;
        tablicaDanych[6] = najlepszyUżytkownik;
        tablicaDanych[7] = najlepszyCzas;
        tablicaDanych[8] = tablicaKodowNFC;
        return tablicaDanych;
    }

    //Zapis do pliku

    /**
     *
     * @throws FileNotFoundException
     */
    public void zapisDoPliku() throws FileNotFoundException {
        File plikTrasy = new File(this.nazwa + ".txt");
        zapisPliku = new PrintWriter(plikTrasy);
        String tekstPliku = "";
        Object[] tablicaDanych = new Object[8];
        tablicaDanych = utworzenieTablicyZInformacjiami();
        for (int i=0; i<tablicaDanych.length-1; i++) {
            tekstPliku += (tablicaDanych[i] + znacznik);
        }
        for (int i=0; i<tablicaKodowNFC.length; i++) {
            tekstPliku += tablicaKodowNFC[i]+ znacznik;
        }
        zapisPliku.println(tekstPliku);
        zapisPliku.close();
    }


    //odczyt pliku


    /**
     *
     *
     * @param odczytywanyPlik
     * @return
     * @throws FileNotFoundException
     */
    public static Trasa wczytywanieTrasyZPliku (File odczytywanyPlik) throws FileNotFoundException {
        odczytPliku = new Scanner(odczytywanyPlik);
        String zawartoscTxt = odczytPliku.nextLine();
        String[] tablicaZawartosciTxt = zawartoscTxt.split(znacznik);
        String[] tempTab = new String[tablicaZawartosciTxt.length-8];
        int tempId = Integer.valueOf(tablicaZawartosciTxt[0]);
        String tempNazwa = tablicaZawartosciTxt[1];
        double tempDistanceKm = Double.valueOf(tablicaZawartosciTxt[2]);
        double tempDistanceMile = Double.valueOf(tablicaZawartosciTxt[3]);
        String tempDistance = tablicaZawartosciTxt[4];
        boolean tempkm = Boolean.valueOf(tablicaZawartosciTxt[5]);
        String tempNajlepszyUżytkownik = tablicaZawartosciTxt[6];
        String tempNajlepszyCzas = tablicaZawartosciTxt[7] ;
        for (int i=8; i<tablicaZawartosciTxt.length; i++) {
            tempTab[i-8]=tablicaZawartosciTxt[i];
        }
        Trasa tempTrasa = new Trasa(tempId, tempNazwa, tempDistanceKm, tempDistanceMile, tempDistance, tempkm, tempTab, tempNajlepszyUżytkownik, tempNajlepszyCzas);
        return tempTrasa;
    }
}