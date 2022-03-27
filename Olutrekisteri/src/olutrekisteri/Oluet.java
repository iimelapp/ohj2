package olutrekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Huolehtii varsinaisen rekisterin ylläpidosta. Osaa lisätä ja poistaa oluita.
 * Lukee ja kirjoittaa oluiden tiedostoon, etsii ja lajittelee oluita.
 * @author Iina
 * @version 19.4.2021
 *
 */
public class Oluet {
    
    private static final int MAX_LKM = 5;
    private int lkm = 0;
    private Olut[] alkiot = new Olut[MAX_LKM];
    private String tiedNimi = "olutrekisteri/oluet.dat";
    private boolean muutettu = false;
        
    
    /**
     * Lisätään olut tietorakenteeseen
     * @param olut lisättävä olut
     * @example
     * <pre name="test">
     *  Oluet oluet = new Oluet();
     *  Olut olut1 = new Olut(), olut2 = new Olut();
     *  oluet.getLkm() === 0;
     *  oluet.lisaa(olut1); oluet.getLkm() === 1;
     *  oluet.lisaa(olut2); oluet.getLkm() === 2;
     *  oluet.lisaa(olut1); oluet.getLkm() === 3;
     *  oluet.anna(0) === olut1;
     *  oluet.anna(1) === olut2;
     *  oluet.anna(2) === olut1;
     *  oluet.anna(1) == olut1 === false;
     *  oluet.anna(1) == olut2 === true;
     *  oluet.anna(3) === olut1; #THROWS IndexOutOfBoundsException
     *  oluet.lisaa(olut1); oluet.getLkm() === 4;
     *  oluet.lisaa(olut1); oluet.getLkm() === 5;
     *  oluet.lisaa(olut1); oluet.getLkm() === 6;
     *  oluet.lisaa(olut1); oluet.getLkm() === 7;
     *  oluet.lisaa(olut1); oluet.getLkm() === 8;
     *  oluet.anna(oluet.getLkm()-1) === olut1;
     * </pre>
     */
    public void lisaa(Olut olut) {
        if (lkm >= alkiot.length) {
            Olut[] alkiotuusi = new Olut[alkiot.length*2];
            for (int i = 0; i < alkiot.length; i++) {
                alkiotuusi[i] = alkiot[i];
            }
            alkiot = alkiotuusi;
        }
        this.alkiot[this.lkm] = olut;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Korvaa oluen tietorakenteessa, etsii samalla id:llä olevan oluen,
     * jos ei löydy, lisää uutena
     * @param olut viite lisättävään olueen
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Oluet oluet = new Oluet();
     * Olut o1 = new Olut(), o2 = new Olut();
     * o1.rekisteroi(); o2.rekisteroi();
     * oluet.getLkm() === 0;
     * oluet.korvaaTaiLisaa(o1); oluet.getLkm() === 1;
     * oluet.korvaaTaiLisaa(o2); oluet.getLkm() === 2;
     * Olut o3 = o1.clone();
     * o3.aseta(3, "sour ale");
     * oluet.korvaaTaiLisaa(o3);
     * oluet.getLkm() === 2;
     * oluet.anna(0).equals(o3) === true;
     * oluet.anna(1).equals(o2) === true;
     * </pre>
     */
    public void korvaaTaiLisaa(Olut olut) {
        int id = olut.getOlutId();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getOlutId() == id) {
                alkiot[i] = olut;
                muutettu = true;
                return;
            }
        }
        lisaa(olut);
    }
    
    /**
     * Palauttaa rekisterissä olevien oluiden lukumäärän
     * @return oluiden lukumäärä
     */
    public int getLkm() {
        return this.lkm;
    }
    
    /**
     * Palauttaa viitteen paikassa i olevaan olueen
     * @param i monennenko oluen viite halutaan
     * @return viite olueen paikassa i
     * @throws IndexOutOfBoundsException jos i ei sallitulla alueella
     */
    public Olut anna(int i) {
        if (i < 0 || this.lkm <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return alkiot[i];
    }
    
    /**
     * Luetaan oluiden tiedostosta
     * @throws SailoException jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * Oluet oluet = new Oluet();
     * Olut o1 = new Olut(), o2 = new Olut();
     * o1.taytaEsimTiedoilla();
     * o2.taytaEsimTiedoilla();
     * o1.rekisteroi(); o2.rekisteroi();
     * String tiedNimi = "testi/oluet.dat";
     * oluet.setTiedNimi(tiedNimi);
     * File ftied = new File(tiedNimi);
     * File dir = new File("testi");
     * dir.mkdir();
     * ftied.delete();
     * oluet.lueTiedostosta(); #THROWS SailoException
     * oluet.lisaa(o1);
     * oluet.lisaa(o2);
     * oluet.tallenna();
     * oluet = new Oluet();
     * oluet.setTiedNimi(tiedNimi);
     * oluet.lueTiedostosta();
     * oluet.getLkm() === 2;
     * oluet.anna(0).getNimi().equals(o1.getNimi()) === true;
     * oluet.anna(1).getNimi().equals(o2.getNimi()) === true;
     * ftied.delete();
     * dir.delete();
     * </pre>
     */
    public void lueTiedostosta() throws SailoException {
        File ftied = new File(tiedNimi);
        try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
            while (fi.hasNext()) {
                String s = "";
                s = fi.nextLine();
                Olut olut = new Olut();
                olut.parse(s);
                lisaa(olut);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Ei saa luettua tiedostoa " + tiedNimi);
        }
    }
    
    /**
     * Tallentaa oluet tiedostoon
     * @throws SailoException jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        File ftied = new File(tiedNimi);
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (int i = 0; i < getLkm(); i++) {
                Olut olut = anna(i);
                fo.println(olut.toString());
            }
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getAbsolutePath() + " ei aukea");
        }
        muutettu = false;
    }
    
    /**
     * Palauttaa oluiden hakemiston nimen
     * @return oluiden hakemisto
     */
    public String getTiedNimi() {
        return this.tiedNimi;
    }
    
    /**
     * Asettaa oluiden hakemiston nimen
     * @param tiedosto asetettava tiedoston nimi
     */
    public void setTiedNimi(String tiedosto) {
        this.tiedNimi = tiedosto;
    }
    
    /**
     * Palauttaa hakuehtoa vastaavien oluiden viitteet
     * @param hakuehto hakuehto jolla haetaan
     * @param k etsittävän kentän indeksi
     * @return löytyneet oluet
     * @example
     * <pre name="test">
     * #import java.util.*;
     * Oluet oluet = new Oluet();
     * Olut o1 = new Olut(); o1.parse("1|Cloudberry Saison|Pyynikin Brewing Company|saison|5.5|32.0");
     * Olut o2 = new Olut(); o2.parse("2|Red|Teerenpeli|red ale|5.0|30.0");
     * Olut o3 = new Olut(); o3.parse("3|Candy Beer Saga vol.1|Panimoyhtiö X|sour ale|5.5|42.0");
     * Olut o4 = new Olut(); o4.parse("4|Hullu Jussi|Teerenpeli|porter|5.0|0.0");
     * Olut o5 = new Olut(); o5.parse("5|Pohjoiskaarre|Teerenpeli|pale ale|4.2|32.0");
     * oluet.lisaa(o1); oluet.lisaa(o2); oluet.lisaa(o3); oluet.lisaa(o4); oluet.lisaa(o5);
     * List<Olut> loytyneet = (List<Olut>)oluet.etsi("*c*", 1);
     * loytyneet.size() === 2;
     * loytyneet.get(0).equals(o3) === true;
     * loytyneet.get(1).equals(o1) === true;
     * loytyneet =  (List<Olut>)oluet.etsi("*ale*", 3);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == o5 === true;
     * loytyneet.get(1) == o2 === true;
     * loytyneet.get(2) == o3 === true;
     * loytyneet =  (List<Olut>)oluet.etsi("", -1);
     * loytyneet.size() === 5;
     * </pre>
     */
    public Collection<Olut> etsi(String hakuehto, int k) {
        List<Olut> loytyneet = new ArrayList<Olut>();
        for (int i = 0; i < this.getLkm(); i++) { 
            if (WildChars.onkoSamat(alkiot[i].anna(k), hakuehto)) loytyneet.add(alkiot[i]);            
        }
        Collections.sort(loytyneet, new Olut.Vertailija(k));
        return loytyneet;
    }
    
    /**
     * Poistaa halutun oluen tietorakenteesta
     * @param i poistettavan oluen id
     * @return 1 jos poistaminen onnistuu, muuten 0
     * @example
     * <pre name="test">
     * Oluet oluet = new Oluet();
     * Olut o1 = new Olut(), o2 = new Olut(), o3 = new Olut();
     * o1.rekisteroi(); o2.rekisteroi(); o3.rekisteroi(); 
     * int id1 = o1.getOlutId();
     * oluet.lisaa(o1); oluet.lisaa(o2); oluet.lisaa(o3);
     * oluet.poista(id1+1) === 1;
     * oluet.getLkm() === 2;
     * oluet.poista(id1) === 1; 
     * oluet.getLkm() === 1;
     * oluet.poista(id1+3) === 0;
     * oluet.getLkm() === 1;
     * </pre>
     */
    public int poista(int i) {
        int id = etsiId(i);
        if (id < 0) return 0;
        lkm--;
        for(int j = id; j < lkm; j++) {
            alkiot[j] = alkiot[j+1];
        }
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }
    
    /**
     * Etsii oluen id:n perusteella
     * @param id etsittävä id
     * @return oluen indeksi, -1 jos ei löydy
     * @example
     * <pre name="test">
     * Oluet oluet = new Oluet();
     * Olut o1 = new Olut(), o2 = new Olut(), o3 = new Olut();
     * o1.rekisteroi(); o2.rekisteroi(); o3.rekisteroi();
     * oluet.lisaa(o1); oluet.lisaa(o2); oluet.lisaa(o3);
     * int id1 = o1.getOlutId();
     * oluet.etsiId(id1) === 0;
     * oluet.etsiId(id1 + 1) === 1;
     * oluet.etsiId(id1 + 2) === 2;
     * </pre>
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getOlutId() == id) return i;
        }
        return -1;
    }
     
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
         Oluet oluet = new Oluet();
         
         Olut olut1 = new Olut();
         Olut olut2 = new Olut();
         
         olut1.rekisteroi();
         olut1.taytaEsimTiedoilla();
         olut2.rekisteroi();
         olut2.taytaEsimTiedoilla();        
         
         oluet.lisaa(olut1);
         oluet.lisaa(olut2);
                    
        System.out.println("======================= Oluet testi=======================");
        
        for (int i = 0; i < oluet.getLkm(); i++) {
            Olut olut = oluet.anna(i);
            System.out.println("Olut paikassa " + i);
            olut.tulosta(System.out);
        }        
         
    }
}
