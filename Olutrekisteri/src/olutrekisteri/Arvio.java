package olutrekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tietää arvion attribuutit. Osaa tarkastaa tietyn kentän syntaksin oikeellisuuden, muuttaa tolpilla
 * erotellun merkkijonon arvion tiedoiksi, antaa merkkijonona halutun kentän tiedot, ja
 * laittaa merkkijonon haluttuun kenttään
 * @author Iina
 * @version 19.4.2021
 * 
 */
public class Arvio implements Cloneable {
    private int arvioId;
    private int olutId;
    private double arvosana = 0;
    private String pvm = "";
    private String kommentit = "";
    
    private static int seuraavaNro = 1;
    
    
    /**
     * Palauttaa kenttien lukumäärän
     * @return kenttien lukumäärä
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.getKenttia() === 5;
     * arvio.ekaKentta() === 2;
     * </pre>
     */
    public int getKenttia() {
        return 5;
    }
    
    /**
     * Palauttaa ensimmäisen kentän indeksin joka kannattaa kysyä
     * @return ensimmäisen kentän indeksi
     */
    public int ekaKentta() {
        return 2;
    }
    
    /**
     * Oletusmuodostaja
     */
    public Arvio() {
        //
    }

    /**
     * Alustetaan tietyn oluen arvio
     * @param olutId oluen viitenumero
     */
    public Arvio(int olutId) {
        this.olutId = olutId;
    }
    
    /**
     * Täytetään arvion tiedot esimerkkitiedoilla
     * @param id viite olueen jonka arviosta on kyse
     */
    public void taytaEsimTiedoilla(int id) {
        this.olutId = id;
        arvosana = kanta.Rand.randD(0, 5);
        pvm = "24.12.2020";
        kommentit = "hyvää";
    }
    
    /**
     * Tulostetaan arvion tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%.2f", arvosana) + " " + pvm + " " + kommentit);
    }
    
    /**
     * Tulostetaan arvion tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Asetetaan arviolle seuraava tunnusnumero
     * @return arvion id
     * @example
     * <pre name="test">
     * Arvio a1 = new Arvio();
     * a1.getArvioId() === 0;
     * a1.rekisteroi();
     * Arvio a2 = new Arvio();
     * a2.rekisteroi();
     * int n1 = a1.getArvioId();
     * int n2 = a2.getArvioId();
     * n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        arvioId = seuraavaNro;
        seuraavaNro++;
        return arvioId;
    }
    
    /**
     * Palautetaan arvion id
     * @return arvion id
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.parse("16|11|3.80|31.3.2021|hyvää");
     * arvio.getArvioId() === 16;
     * arvio.getOlutId() === 11;
     * arvio.getArvosana() ~~~ 3.80;
     * </pre>
     */
    public int getArvioId() {
        return arvioId;
    }
    
    /**
     * Palautetaan minkä oluen arviosta on kyse
     * @return oluen id
     */
    public int getOlutId() {
        return olutId;
    }
    
    /**
     * Palauttaa oluelle annetun arvosanan
     * @return annettu arvosana
     */
    public double getArvosana() {
        return this.arvosana;
    }
    
    
    /**
     * Asettaa arviolle IDn ja varmistaa että seuraava id on aina suurempi kuin
     * tähän mennessä suurin
     * @param n asetettava id
     */
    private void setArvioId(int n) {
        arvioId = n;
        if (arvioId >= seuraavaNro) seuraavaNro = arvioId+1;
    }
    
    /**
     * Selvitetään arvion tiedot |-merkillä erotellusta merkkijonosta
     * @param rivi josta arvion tiedot otetaan
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.parse("    16|11|  3.80|31.3.2021| hyvää");
     * arvio.toString().startsWith("16|11|3.80|") === true;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setArvioId(Mjonot.erota(sb, '|', getArvioId()));
        olutId = Mjonot.erota(sb, '|', olutId);
        arvosana = Mjonot.erota(sb, '|', arvosana);
        pvm = Mjonot.erota(sb, '|', pvm);
        kommentit = Mjonot.erota(sb, '|', kommentit);
    }
    
    /**
     * Palauttaa haluttua kenttää vastaavan kysymyksen
     * @param k monennenko kentän kysymys halutaan 
     * @return kenttää vastaava kysymys
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.getKysymys(0) === "arvioId";
     * arvio.getKysymys(1) === "olutId";
     * arvio.getKysymys(2) === "arvosana";
     * arvio.getKysymys(-1) === "";
     * </pre>
     */
    public String getKysymys(int k) {
        switch (k) {
        case 0 : return "arvioId";
        case 1 : return "olutId";
        case 2 : return "arvosana";
        case 3 : return "päivämäärä";
        case 4 : return "kommentit";
        default : return "";
        }
    }
    
    /**
     * Antaa k:nnen kentän sisällön merkkijonona
     * @param k monesko kenttä halutaan
     * @return kentän sisältö merkkijonona
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.parse("    16|11|  3.80|31.3.2021| hyvää");
     * arvio.anna(0) === "16";
     * arvio.anna(1) === "11";
     * arvio.anna(2) === "3.8";
     * arvio.anna(-1) === "";
     * </pre>
     */
    public String anna(int k) {
        switch (k) {
        case 0 : return "" + arvioId;
        case 1 : return "" + olutId;
        case 2 : return "" + arvosana;
        case 3 : return "" + pvm;
        case 4 : return "" + kommentit;
        default : return "";
        }
    }
    
    /**
     * Asettaa k:nnen kentän arvoksi parametrina tuodun merkkijonon
     * @param k monenteenko kenttään asetetaan 
     * @param jono asetettava merkkijono
     * @return null jos asettaminen onnistuu
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.aseta(0, "1") === null;
     * arvio.aseta(2, "kissa") === "Pitää olla numero";
     * arvio.aseta(2, "3.5") === null;
     * </pre>
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuilder sb = new StringBuilder(tjono);
        switch (k) {
        case 0 : 
            setArvioId(Mjonot.erota(sb, '|', getOlutId()));
            return null;
        case 1 : 
            olutId = Mjonot.erota(sb, '|', olutId);
            return null;
        case 2 : 
            String virhe = kanta.Rand.onkoNumero(tjono);
            if (virhe != null) return virhe;
            arvosana = Mjonot.erotaDouble(sb, arvosana);
            return null;
        case 3 : 
            pvm = tjono;
            return null;
        case 4 : 
            kommentit = tjono;
            return null;
        default : 
            return "";
        }
    }
    
    /**
     * Palauttaa arvion tiedot tolpillaeroteltuna merkkijonona
     * @return arvion tiedot merkkijonona
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.taytaEsimTiedoilla(1);
     * arvio.toString() =R= ".*0|1|.*|24.12.2020|hyvää";
     * </pre>
     */
    @Override
    public String toString() {
        return getArvioId() + "|" + olutId + "|" + String.format("%.2f", arvosana).replace(',', '.') + "|" + pvm + "|" + kommentit;
    }
    
    /**
     * Tutkii onko arvion tiedot samat kuin parametrina tuodun arvion tiedot
     * @param obj arvio johon verrataan
     * @return true jos samat, muuten false
     * @example
     * <pre name="test">
     * Arvio a1 = new Arvio(), a2 = new Arvio(), a3 = new Arvio();
     * a1.parse("16|11|3.80|31.3.2021|hyvää");
     * a2.parse("16|11|3.80|31.3.2021|hyvää");
     * a3.parse("16|11|3.80|31.1.2021|hyvää");
     * a1.equals(a2) === true;
     * a1.equals(a3) === false;
     * a2.equals(a1) === true;
     * a2.equals(a3) === false;
     * a3.equals(null) === false;
     * </pre>
     */
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }

    
    @Override
    public int hashCode() {
        return arvioId;
    }
    
    /**
     * Luodaan identtinen klooni arviosta
     * @return kloonattu arvio
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Arvio arvio = new Arvio();
     * arvio.parse("16|11|3.80|31.3.2021|hyvää");
     * Object kopio = arvio.clone();
     * kopio.toString() === arvio.toString();
     * arvio.parse("16|11|3.80|12.4.2021|hyvää");
     * kopio.toString().equals(arvio.toString()) === false;
     * </pre>
     */
    @Override
    public Arvio clone() throws CloneNotSupportedException {
        Arvio uusi;
        uusi = (Arvio) super.clone();
        return uusi;
    }
    

    /**
     * Palauttaa k:nnen kentän sisällön merkkijonona
     * @param k monennenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona
     * @example
     * <pre name="test">
     * Arvio arvio = new Arvio();
     * arvio.parse("16|11|3.80|12.4.2021|hyvää");
     * arvio.getAvain(0) === "16";
     * arvio.getAvain(1) === "11";
     * arvio.getAvain(2) === "3.8";
     * arvio.getAvain(10) === "";
     * </pre>
     */
    public String getAvain(int k) {
        switch (k) {
        case 0: return "" + arvioId;
        case 1: return "" + olutId;
        case 2: return "" + arvosana;
        case 3: return "" + pvm;
        case 4: return "" + kommentit;
        default: return "";
        }
    }
    
    /**
     * @author Iina
     * @version 16.4.2021
     * Vertailija-luokka Arvioille
     */
    public static class Vertailija implements Comparator<Arvio> {
        private int k;
        
        /**
         * Luodaan uusi vertailija
         * @param k monennenko kentän sisältöä vertaillaan
         */
        public Vertailija(int k) {
            this.k = k;
        }
        
        /**
         * Vertaa kahta arviota keskenään
         * @param a1 1. verrattava arvio
         * @param a2 2. verrattava arvio
         * @return <0 jos a1 < a2, ==0 jos a1 === a2, muuten >0
         * @example
         * <pre name="test">
         * #PACKAGEIMPORT
         * #import olutrekisteri.Arvio.Vertailija;
         * Arvio a1 = new Arvio(); a1.parse("1|5|4.3|30.3.2021|");
         * Arvio a2 = new Arvio(); a2.parse("2|6|3.50|21.3.2021|");
         * Arvio a3 = new Arvio(); a3.parse("3|11|3.80|31.3.2021|");
         * Vertailija v1 = new Vertailija(0);
         * v1.compare(a1, a2) < 0 === true;
         * v1.compare(a3, a1) > 0 === true;
         * v1.compare(a2, a3) < 0 === true;
         * Vertailija v2 = new Vertailija(2);
         * v2.compare(a1, a2) > 0 === true;
         * v2.compare(a2, a3) < 0 === true;
         * v2.compare(a3, a1) < 0 === true;
         * </pre>
         */
        @Override
        public int compare(Arvio a1, Arvio a2) {
            return a1.getAvain(k).compareToIgnoreCase(a2.getAvain(k));
        }
        
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Arvio a1 = new Arvio(); Arvio a2 = new Arvio();
        a1.rekisteroi();
        a2.rekisteroi();
        
        System.out.println(a1.getArvioId());
        a1.taytaEsimTiedoilla(1);
        a1.tulosta(System.out);
        
        System.out.println(a2.getArvioId());
        a2.taytaEsimTiedoilla(1);
        a2.tulosta(System.out);
    }   
    
}
