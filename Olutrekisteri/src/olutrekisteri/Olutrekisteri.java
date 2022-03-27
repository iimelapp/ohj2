/**
 * 
 */
package olutrekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Huolehtii oluet- ja arviot-luokkien välisestä kommunikoinnista
 * ja välittää tietoja pyydettäessä. Lukee ja kirjoittaa rekisterin tiedostoon 
 * avustajiensa (Oluet, Arviot, Olut, Arvio) avulla.
 * @author Iina
 * @version 19.4.2021
 *
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * private Olutrekisteri rekisteri;
 * private Olut o1;
 * private Olut o2;
 * private int id1;
 * private int id2;
 * private Arvio a1;
 * private Arvio a2;
 * private Arvio a3;
 * private Arvio a4;
 * private Arvio a5;
 * 
 * public void alustaRekisteri() {
 * rekisteri = new Olutrekisteri();
 * o1 = new Olut(); o1.taytaEsimTiedoilla(); o1.rekisteroi();
 * o2 = new Olut(); o2.taytaEsimTiedoilla(); o2.rekisteroi();
 * id1 = o1.getOlutId();
 * id2 = o2.getOlutId();
 * a1 = new Arvio(id2); a1.taytaEsimTiedoilla(id2);
 * a2 = new Arvio(id1); a2.taytaEsimTiedoilla(id1);
 * a3 = new Arvio(id2); a3.taytaEsimTiedoilla(id2);
 * a4 = new Arvio(id1); a4.taytaEsimTiedoilla(id1);
 * a5 = new Arvio(id2); a5.taytaEsimTiedoilla(id2);
 * rekisteri.lisaa(o1);
 * rekisteri.lisaa(o2);
 * rekisteri.lisaa(a1);
 * rekisteri.lisaa(a2);
 * rekisteri.lisaa(a3);
 * rekisteri.lisaa(a4);
 * rekisteri.lisaa(a5);
 * }
 * </pre>
 */
public class Olutrekisteri {
    
    private Oluet oluet = new Oluet();
    private Arviot arviot = new Arviot();
       
    
    /**
     * Lisätään uusi olut
     * @param olut lisättävä olut
     * @example
     * <pre name="test">
     * Olutrekisteri rekisteri = new Olutrekisteri();
     * Olut olut1 = new Olut(); Olut olut2 = new Olut();
     * olut1.rekisteroi(); olut2.rekisteroi();
     * rekisteri.getOluita() === 0;
     * rekisteri.lisaa(olut1); rekisteri.getOluita() === 1;
     * rekisteri.lisaa(olut2); rekisteri.getOluita() === 2;
     * rekisteri.lisaa(olut1); rekisteri.getOluita() === 3;
     * rekisteri.annaOlut(0) === olut1;
     * rekisteri.annaOlut(1) === olut2;
     * rekisteri.annaOlut(2) === olut1;
     * rekisteri.annaOlut(3) === olut1; #THROWS IndexOutOfBoundsException
     * rekisteri.lisaa(olut2); rekisteri.getOluita() === 4;
     * rekisteri.lisaa(olut1); rekisteri.getOluita() === 5;
     * rekisteri.lisaa(olut2); rekisteri.getOluita() === 6;
     * rekisteri.lisaa(olut1); rekisteri.getOluita() === 7;
     * </pre>
     */
    public void lisaa(Olut olut) {
        oluet.lisaa(olut);
    }
    
    /**
     * Poistaa oluista ja arvioista oluen tiedot
     * @param olut poistettava olut
     * @return montako olutta poistettiin
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaRekisteri();
     * rekisteri.etsi("*", 0).size() === 2;
     * rekisteri.annaArviot(o1).size() === 2;
     * rekisteri.poista(o1) === 1;
     * rekisteri.etsi("*", 0).size() === 1;
     * rekisteri.annaArviot(o1).size() === 0;
     * rekisteri.annaArviot(o2).size() === 3;
     * </pre>
     */
    public int poista(Olut olut) {
        if (olut == null) return 0;
        int n = oluet.poista(olut.getOlutId());
        arviot.poistaOluenArviot(olut.getOlutId());
        return n;
    }
    
    /**
     * Poistaa yhden arvion
     * @param a poistettava arvio
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * rekisteri.annaArviot(o1).size() === 2;
     * rekisteri.poista(a2);
     * rekisteri.annaArviot(o1).size() === 1;
     * </pre>
     */
    public void poista(Arvio a) {
        arviot.poista(a);
    }
    
    /**
     * Korvaa oluen tietorakenteessa, ottaa oluen omistukseensa
     * Etsitään samalla id:llä oleva olut, jos ei löydy, niin
     * lisätään uutena oluena
     * @param olut lisättävän oluen viite
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaRekisteri();
     * rekisteri.etsi("*", 0).size() === 2;
     * rekisteri.korvaaTaiLisaa(o1);
     * rekisteri.etsi("*", 0).size() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Olut olut) {
        oluet.korvaaTaiLisaa(olut);
    }
    
    /**
     * Korvaa arvion tiedot tietorakenteessa, ottaa arvion omistukseen
     * Etsitään samalla id:lla oleva arvio, jos ei löydy niin lisätään uutena
     * @param arvio lisättävä arvio
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * rekisteri.annaArviot(o2).size() === 3;
     * rekisteri.korvaaTaiLisaa(a5);
     * rekisteri.annaArviot(o2).size() === 3;
     * Arvio a6 = new Arvio(o2.getOlutId());
     * a6.rekisteroi();
     * rekisteri.lisaa(a6);
     * rekisteri.annaArviot(o2).size() === 4;
     * </pre>
     */
    public void korvaaTaiLisaa(Arvio arvio) {
        arviot.korvaaTaiLisaa(arvio);
    }
    
    /**
     * Lisätään uusi arvio
     * @param arvio lisättävä arvio
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * rekisteri.annaArviot(o2).size() === 3;
     * rekisteri.lisaa(a5);
     * rekisteri.annaArviot(o2).size() === 4;
     * </pre>
     */
    public void lisaa(Arvio arvio) {
        arviot.lisaa(arvio);
    }
    
    /**
     * Palauttaa oluiden lukumärän
     * @return oluiden  lukumäärä
     */
    public int getOluita() {
        return oluet.getLkm();
    }
    
    /**
     * Palauttaa rekisterin i:nnen oluen
     * @param i monesko olut palautetaan
     * @return i:nnessä paikassa oleva olut
     * @throws IndexOutOfBoundsException jos i laiton indeksi
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * rekisteri.getOluita() === 2;
     * rekisteri.annaOlut(0).toString().equals(o1);
     * rekisteri.annaOlut(1).toString().equals(o2);
     * rekisteri.annaOlut(2).toString(); #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public Olut annaOlut(int i) throws IndexOutOfBoundsException {
        return oluet.anna(i);
    }
    
    /**
     * Palauttaa halutun oluen arviot
     * @param olut olut jonka arviot etsitään
     * @return lista oluen arvioista
     * @example
     * <pre name="test">
     * #import java.util.*;
     * alustaRekisteri();
     * Olut o3 = new Olut();
     * List<Arvio> loytyneet;
     * loytyneet = rekisteri.annaArviot(o3);
     * loytyneet.size() === 0;
     * loytyneet = rekisteri.annaArviot(o1);
     * loytyneet.size() === 2;
     * loytyneet.get(0) == a2 === true;
     * loytyneet.get(1) == a4 === true;
     * loytyneet = rekisteri.annaArviot(o2);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == a1 === true;
     * </pre>
     */
    public List<Arvio> annaArviot(Olut olut) {
        return arviot.annaArviot(olut.getOlutId());
    }
    
    /**
     * Palauttaa hakuehtoa vastaavien oluiden viitteet
     * @param hakuehto hakuehto jolla haetaan
     * @param k etsittävän kentän indeksi
     * @return löytyneet oluet
     * @throws SailoException jos hakeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException, SailoException
     * alustaRekisteri();
     * Olut o3 = new Olut(); o3.rekisteroi();
     * o3.aseta(1, "Gosebump");
     * rekisteri.lisaa(o3);
     * Collection<Olut> loytyneet = rekisteri.etsi("*bump*", 1);
     * loytyneet.size() === 1;
     * </pre>
     */
    public Collection<Olut> etsi(String hakuehto, int k) throws SailoException {
        return oluet.etsi(hakuehto, k);
    }
    
    /**
     * Etsii parhaat arvosanat saaneet oluet
     * @return merkkijonotaulukko parhaista oluista ja niiden arvosanoista
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * Olut o3 = new Olut(), o4 = new Olut();
     * o1.parse("1|Cloudberry Saison|Pyynikin Brewing Company|saison|5.5|32.0");
     * o2.parse("2|Red|Teerenpeli|red ale|5.0|30.0");                           
     * o3.parse("3|Candy Beer Saga vol.1|Panimoyhtiö X|sour ale|5.5|42.0");     
     * o4.parse("4|Hullu Jussi|Teerenpeli|porter|5.0|0.0");                     
     * rekisteri.lisaa(o3); rekisteri.lisaa(o4);
     * a1.parse("1|2|3.8|");
     * a2.parse("2|1|4.5|");
     * a3.parse("3|2|2.5|");
     * a4.parse("4|3|4.5|");
     * a5.parse("5|4|3.0|"); 
     * String[] parhaat = rekisteri.etsiParhaat();
     * parhaat[0].equals(o3.getNimi() + " " + a4.getArvosana()) === true;
     * parhaat[1].equals(o1.getNimi() + " " + a2.getArvosana()) === true;
     * parhaat[2].equals(o2.getNimi() + " " + a1.getArvosana()) === true;
     * parhaat[3].equals(o4.getNimi() + " " + a5.getArvosana()) === true;
     * parhaat[4].equals(o2.getNimi() + " " + a3.getArvosana()) === true;
     * </pre>
     */
    public String[] etsiParhaat() {
        List<Arvio> parhaatArviot = arviot.etsiParhaat();
        String[] parhaat = new String[5];
        for (int i = 0; i < parhaatArviot.size(); i++) {
            Arvio a = parhaatArviot.get(i);
            Olut o = oluet.anna(oluet.etsiId(a.getOlutId()));  
            parhaat[i] = o.getNimi() + " " + a.getArvosana();
        }
        
        return parhaat;
    }
    
    /**
     * Luetaan rekisterin tiedostosta
     * @param nimi minkä nimisestä tiedostosta luetaan
     * @throws SailoException jos lukeminen ei onnistu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * rekisteri = new Olutrekisteri();
     * String hakemisto = "testi";
     * File dir = new File(hakemisto);
     * File ftied = new File(hakemisto + "/oluet.dat");
     * File aftied = new File(hakemisto + "/arviot.dat");
     * dir.mkdir();
     * ftied.delete();
     * aftied.delete();
     * rekisteri = new Olutrekisteri();
     * rekisteri.lueTiedostosta(hakemisto); #THROWS SailoException
     * alustaRekisteri();
     * rekisteri.setTiedostot(hakemisto);
     * rekisteri.tallenna();
     * rekisteri.lueTiedostosta(hakemisto);
     * Collection<Olut> kaikki = rekisteri.etsi("", -1);
     * kaikki.size() === 2;
     * Iterator<Olut> it = kaikki.iterator();
     * it.next().toString().equals(o1.toString());
     * it.next().toString().equals(o2.toString());
     * it.hasNext() === false;
     * List<Arvio> loytyneet = rekisteri.annaArviot(o1);
     * Iterator<Arvio> i = loytyneet.iterator();
     * i.next() === a2;
     * i.next() === a4;
     * i.hasNext() === false;
     * loytyneet = rekisteri.annaArviot(o2);
     * i = loytyneet.iterator();
     * i.next() === a1;
     * i.next() === a3;
     * i.next() === a5;
     * i.hasNext() === false;
     * rekisteri.lisaa(o2);
     * rekisteri.lisaa(a5);
     * rekisteri.tallenna();
     * ftied.delete();
     * aftied.delete();
     * dir.delete();
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        oluet = new Oluet();
        arviot = new Arviot();
        
        setTiedostot(nimi);
        oluet.lueTiedostosta();
        arviot.lueTiedostosta();
    }
    
    /**
     * Tallennetaan rekisterin tiedot tiedostoon
     * @throws SailoException jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            oluet.tallenna();
        } catch (SailoException e) {
            virhe = e.getMessage();
        }
        
        try {
            arviot.tallenna();
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        
        if (!"".equals(virhe)) throw new SailoException(virhe);
    }
    
    /**
     * Asetetaan käytettävät tiedostot
     * @param tiedosto käytettävä tiedoston nimi
     */
    public void setTiedostot(String tiedosto) {
        File dir = new File(tiedosto);
        dir.mkdirs();
        this.oluet.setTiedNimi(tiedosto + "/oluet.dat");
        this.arviot.setTiedNimi(tiedosto + "/arviot.dat");
    }

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Olutrekisteri rekisteri = new Olutrekisteri();
        
        Olut olut1 = new Olut();
        Olut olut2 = new Olut();
        Olut olut3 = new Olut();
        Olut olut4 = new Olut();
        Olut olut5 = new Olut();
        
        olut1.rekisteroi();
        olut1.taytaEsimTiedoilla();
        olut2.rekisteroi();
        olut2.taytaEsimTiedoilla();
        olut3.rekisteroi();
        olut3.taytaEsimTiedoilla();
        olut4.rekisteroi();
        olut4.taytaEsimTiedoilla();
        olut5.rekisteroi();
        olut5.taytaEsimTiedoilla();
        
        rekisteri.lisaa(olut1);
        rekisteri.lisaa(olut2);
        rekisteri.lisaa(olut3);
        rekisteri.lisaa(olut4);
        rekisteri.lisaa(olut5);
        
        Arvio a1 = new Arvio(1); a1.taytaEsimTiedoilla(1); rekisteri.lisaa(a1);
        Arvio a2 = new Arvio(4); a2.taytaEsimTiedoilla(4); rekisteri.lisaa(a2);
        Arvio a3 = new Arvio(3); a3.taytaEsimTiedoilla(3); rekisteri.lisaa(a3);
        Arvio a4 = new Arvio(2); a4.taytaEsimTiedoilla(2); rekisteri.lisaa(a4);
        Arvio a5 = new Arvio(5); a5.taytaEsimTiedoilla(5); rekisteri.lisaa(a5);
        System.out.println(rekisteri.etsiParhaat());
        
        System.out.println("========================Olutrekisterin testi======================");
        
        for (int i = 0; i < rekisteri.getOluita(); i++) {
            Olut olut = rekisteri.annaOlut(i);
            System.out.println("Olut paikassa " + i);
            olut.tulosta(System.out);
            List<Arvio> loytyneet = rekisteri.annaArviot(olut);
            for (Arvio a : loytyneet) {
                a.tulosta(System.out);
            }
        }       
                    
    }

}
