package olutrekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Huolehtii varsinaisen rekisterin ylläpidosta, osaa lisätä ja poistaa arvioita,
 * lukee ja kirjoittaa arvioiden tiedostoon, etsii ja lajittelee arvioita
 * @author Iina
 * @version 19.4.2021
 *
 */
public class Arviot implements Iterable<Arvio> {
    private Collection<Arvio> alkiot = new ArrayList<Arvio>();
    private String tiedNimi = "olutrekisteri/arviot.dat";
    private boolean muutettu = false;
    
    
    /**
     * Lisätään arvio tietorakenteeseen
     * @param a lisättävä arvio
     */
    public void lisaa(Arvio a) {
        alkiot.add(a);
        muutettu = true;
    }
    
    /**
     * Korvaa arvion tiedot tietorakenteessa, ottaa arvion omistukseen
     * Etsitään samalla id:lla oleva arvio, jos ei löydy niin lisätään uutena
     * @param a lisättävä arvio
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * #PACKAGEIMPORT
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(), a2 = new Arvio();
     * a1.rekisteroi(); a2.rekisteroi();
     * arviot.getLkm() === 0;
     * arviot.korvaaTaiLisaa(a1); arviot.getLkm() === 1;
     * arviot.korvaaTaiLisaa(a2); arviot.getLkm() === 2;
     * Arvio a3 = a1.clone();
     * a3.aseta(2, "3.2");
     * Iterator<Arvio> i = arviot.iterator();
     * i.next() === a1;
     * arviot.korvaaTaiLisaa(a3); arviot.getLkm() === 2;
     * i = arviot.iterator();
     * Arvio a = i.next();
     * a === a3;
     * a == a1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Arvio a) {
        int id = a.getArvioId();
        for (int i = 0; i < getLkm(); i++) {
            if (((ArrayList<Arvio>) alkiot).get(i).getArvioId() == id) {
                ((ArrayList<Arvio>) alkiot).set(i, a);
                muutettu = true;
                return;
            }
        }
        lisaa(a);
    }
    
    /**
     * Etsitään oluen arviot
     * @param olutId minkä oluen arvioita etsitään
     * @return lista löytyneistä
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(2); arviot.lisaa(a1);
     * Arvio a2 = new Arvio(1); arviot.lisaa(a2);
     * Arvio a3 = new Arvio(2); arviot.lisaa(a3);
     * Arvio a4 = new Arvio(1); arviot.lisaa(a4);
     * Arvio a5 = new Arvio(2); arviot.lisaa(a5);
     * Arvio a6 = new Arvio(5); arviot.lisaa(a6);
     * 
     * List<Arvio> loytyneet;
     * loytyneet = arviot.annaArviot(3);
     * loytyneet.size() === 0;
     * loytyneet = arviot.annaArviot(1);
     * loytyneet.size() === 2;
     * loytyneet.get(0) == a2 === true;
     * loytyneet.get(1) == a4 === true;
     * loytyneet = arviot.annaArviot(5);
     * loytyneet.size() === 1;
     * loytyneet.get(0) == a6 === true;
     * </pre>
     */
    public List<Arvio> annaArviot(int olutId) {
        List<Arvio> loydetyt = new ArrayList<Arvio>();
        for(Arvio a : alkiot) {
            if (a.getOlutId() == olutId) loydetyt.add(a);
        }
        return loydetyt;
    }
    
    /**
     * Palauttaa rekisterissä olevien arvioiden lukumäärän
     * @return arvioiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Palauttaa oluiden hakemiston nimen
     * @return oluiden hakemisto
     */
    public String getTiedNimi() {
        return this.tiedNimi;
    }

    /**
     * Luetaan arvioiden tiedostosta
     * @throws SailoException jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(); a1.taytaEsimTiedoilla(2); 
     * Arvio a2 = new Arvio(); a2.taytaEsimTiedoilla(1); 
     * Arvio a3 = new Arvio(); a3.taytaEsimTiedoilla(2); 
     * Arvio a4 = new Arvio(); a4.taytaEsimTiedoilla(1); 
     * Arvio a5 = new Arvio(); a5.taytaEsimTiedoilla(2); 
     * String tiedNimi = "testi/arviot.dat";
     * arviot.setTiedNimi(tiedNimi);
     * File ftied = new File(tiedNimi);
     * File dir = new File("testi");
     * dir.mkdir();
     * ftied.delete();
     * arviot.lueTiedostosta(); #THROWS SailoException
     * arviot.lisaa(a1);
     * arviot.lisaa(a2);
     * arviot.lisaa(a3);
     * arviot.lisaa(a4);
     * arviot.lisaa(a5);
     * arviot.tallenna();
     * arviot = new Arviot();
     * arviot.setTiedNimi(tiedNimi);
     * arviot.lueTiedostosta();
     * Iterator<Arvio> i = arviot.iterator();
     * i.next().toString() === a1.toString();
     * i.next().toString() === a2.toString();
     * i.next().toString() === a3.toString();
     * i.next().toString() === a4.toString();
     * i.next().toString() === a5.toString();
     * i.hasNext() === false;
     * </pre>
     */
    public void lueTiedostosta() throws SailoException {
        File ftied = new File(tiedNimi);
        try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
            while (fi.hasNext()) {
                String s = "";
                s = fi.nextLine();
                Arvio arvio = new Arvio();
                arvio.parse(s);
                lisaa(arvio);
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
            for (Arvio a : alkiot) {
                fo.println(a.toString());
            }
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getAbsolutePath() + " ei aukea");
        }
        
        muutettu = false;
    }
    
    /**
     * Poistaa valitun arvion
     * @param arvio poistettava arvio
     * @return true jos poistaminen onnistui
     * @example
     * <pre name="test">
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(); a1.taytaEsimTiedoilla(2);
     * Arvio a2 = new Arvio(); a2.taytaEsimTiedoilla(1);
     * Arvio a3 = new Arvio(); a3.taytaEsimTiedoilla(2);
     * Arvio a4 = new Arvio(); a4.taytaEsimTiedoilla(1);
     * Arvio a5 = new Arvio(); a5.taytaEsimTiedoilla(2);
     * arviot.lisaa(a1);
     * arviot.lisaa(a2);
     * arviot.lisaa(a3);
     * arviot.lisaa(a4);
     * arviot.poista(a5) === false; arviot.getLkm() === 4;
     * arviot.poista(a1) === true; arviot.getLkm() === 3;
     * List<Arvio> a = arviot.annaArviot(2);
     * a.size() === 1;
     * </pre>
     */
    public boolean poista(Arvio arvio) {
        boolean palautus = alkiot.remove(arvio);
        if (palautus) muutettu = true;
        return palautus;
    }
    
    /**
     * Poistaa halutun oluen kaikki arviot
     * @param id oluen id, jolta arviot poistetaan
     * @return montako arviot poistettiin
     * @example
     * <pre name="test">
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(); a1.taytaEsimTiedoilla(2);
     * Arvio a2 = new Arvio(); a2.taytaEsimTiedoilla(1);
     * Arvio a3 = new Arvio(); a3.taytaEsimTiedoilla(2);
     * Arvio a4 = new Arvio(); a4.taytaEsimTiedoilla(1);
     * Arvio a5 = new Arvio(); a5.taytaEsimTiedoilla(2);
     * arviot.lisaa(a1);
     * arviot.lisaa(a2);
     * arviot.lisaa(a3);
     * arviot.lisaa(a4);
     * arviot.lisaa(a5);
     * arviot.poistaOluenArviot(2) === 3; arviot.getLkm() === 2;
     * arviot.poistaOluenArviot(3) === 0; arviot.getLkm() === 2;
     * List<Arvio> a = arviot.annaArviot(2);
     * a.size() === 0;
     * a = arviot.annaArviot(1);
     * a.get(0) === a2;
     * a.get(1) === a4;
     * </pre>
     */
    public int poistaOluenArviot(int id) {
        int n = 0;
        for (Iterator<Arvio> it = alkiot.iterator(); it.hasNext();) {
            Arvio a = it.next();
            if (a.getOlutId() == id) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    

    /**
     * Iteraattori arvioiden läpikäymiseen
     * @return arvioiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(2); arviot.lisaa(a1);
     * Arvio a2 = new Arvio(1); arviot.lisaa(a2);
     * Arvio a3 = new Arvio(2); arviot.lisaa(a3);
     * Arvio a4 = new Arvio(1); arviot.lisaa(a4);
     * Arvio a5 = new Arvio(2); arviot.lisaa(a5);
     * 
     * Iterator<Arvio> i2 = arviot.iterator();
     * i2.next() === a1;
     * i2.next() === a2;
     * i2.next() === a3;
     * i2.next() === a4;
     * i2.next() === a5;
     * i2.next() === a2; #THROWS NoSuchElementException
     * 
     * int n = 0;
     * int id[] = {2, 1, 2, 1, 2};
     * 
     * for (Arvio a : arviot) {
     *  a.getOlutId() === id[n];
     *  n++;
     *  }
     *  
     *  n === 5;
     * </pre>
     */
    @Override
    public Iterator<Arvio> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Asettaa oluiden hakemiston nimen
     * @param tiedosto asetettava tiedoston nimi
     */
    public void setTiedNimi(String tiedosto) {
        this.tiedNimi = tiedosto;
    }
    
    /**
     * Palauttaa listan viidestä parhaasta arviosta arvosanan perusteella
     * @return lista parhaista arvioista
     * @example
     * <pre name="test">
     * Arviot arviot = new Arviot();
     * Arvio a1 = new Arvio(), a2 = new Arvio(), a3 = new Arvio(), a4 = new Arvio(), a5 = new Arvio();
     * a1.parse("1|2|3.8|");
     * a2.parse("2|1|4.5|");
     * a3.parse("3|2|2.5|");
     * a4.parse("4|3|4.5|");
     * a5.parse("5|4|3.0|");
     * arviot.lisaa(a1); arviot.lisaa(a2); arviot.lisaa(a3); arviot.lisaa(a4); arviot.lisaa(a5);
     * List<Arvio> parhaat = arviot.etsiParhaat();
     * parhaat.size() === 5;
     * Iterator<Arvio> i = parhaat.iterator();
     * i.next() === a4;
     * i.next() === a2;
     * i.next() === a1;
     * i.next() === a5;
     * i.next() === a3;
     * i.hasNext() === false;
     * </pre>
     */
    public List<Arvio> etsiParhaat() {
        List<Arvio> loytyneet = new ArrayList<Arvio>(alkiot);
        Collections.sort(loytyneet, new Arvio.Vertailija(2));
        List<Arvio> parhaat = new ArrayList<Arvio>();
        for (int i = loytyneet.size()-1; i >= loytyneet.size()-5; i--) {
            parhaat.add(loytyneet.get(i));
        }

        return parhaat;
    }
    

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Arviot arviot = new Arviot(); 
        
        try { 
            arviot.lueTiedostosta();
        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }
        
        Arvio arvio = new Arvio();
        arvio.rekisteroi();
        arvio.taytaEsimTiedoilla(2);      
        Arvio arvio2 = new Arvio();
        arvio2.rekisteroi();
        arvio2.taytaEsimTiedoilla(2);       
        
        arviot.lisaa(arvio);
        arviot.lisaa(arvio2);
        
        System.out.println("=============================Arviot testi=================================");
        
        List<Arvio> arviot2 = arviot.annaArviot(2);
        
        for (Arvio a : arviot2) {
            System.out.println(a.getArvioId() + " ");
            a.tulosta(System.out);
        }     
        
        try {
            arviot.tallenna();
        } catch (SailoException e) {
            e.printStackTrace();
        }
        
    }
}
