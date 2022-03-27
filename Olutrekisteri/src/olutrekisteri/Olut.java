package olutrekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tietää oluen attribuutit. Osaa tarkistaa tietyn kentän syntaksin, muuttaa tolpilla erotellun merkkijonon oluen tiedoiksi, 
 * antaa merkkijonona halutun kentän tiedot ja laittaa merkkijonon haluttuun kenttään.
 * @author Iina
 * @version 19.4.2021
 *
 */
public class Olut implements Cloneable{
    
    private int olutId;
    private String nimi = "";
    private String valmistaja = "";
    private String tyylilaji = "";
    private double alkoholiprosentit = 0;
    private double IBU = 0;
    
    private static int seuraavaId = 1;
    
    
    
    /**
     * Palauttaa oluen kenttien lukumäärän
     * @return  kenttien lukumäärä
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.getKenttia() === 6;
     * olut.ekaKentta() === 1
     * </pre>
     */
    public int getKenttia() {
        return 6;
    }
    
    /**
     * Palauttaa ensimmäisen kentän joka kannattaa kysyä
     * @return ensimmäisen kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }
    
    /**
     * Tulostetaan oluen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%02d", olutId) + " " + nimi + " " + valmistaja);
        out.println(" " + tyylilaji + " Prosentteja: " + alkoholiprosentit + " IBU: " + IBU);
    }
    
    /**
     * Tulostetaan oluen tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Asetetaan oluelle seuraava tunnusnumero
     * @return oluen id
     * @example
     * <pre name="test">
     *  Olut olut1 = new Olut();
     *  olut1.getOlutId() === 0;
     *  olut1.rekisteroi();
     *  Olut olut2 = new Olut();
     *  olut2.rekisteroi();
     *  int n1 = olut1.getOlutId();
     *  int n2 = olut2.getOlutId();
     *  n1 === n2-1; 
     * </pre>
     */
    public int rekisteroi() {
        this.olutId = seuraavaId;
        seuraavaId++;
        return this.olutId;
    }
    
    /**
     * Täytetään oluen tiedot esimerkkitiedoilla
     */
    public void taytaEsimTiedoilla() {
        nimi = "Cloudberry Saison " + kanta.Rand.rand(1000, 9999);
        valmistaja = "Pyynikin Brewing Company";
        tyylilaji = "saison";
        alkoholiprosentit = 5;
        IBU = 32;
    }
    
    /**
     * Palauttaa oluen tiedot tolpilla eroteltuna merkkijonona
     * @return oluen tiedot merkkijonona
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.taytaEsimTiedoilla();
     * olut.toString() =R= ".*|Cloudberry Saison.*|Pyyniking Brewing Company|.*";
     * </pre>
     */
    @Override
    public String toString() {
        return getOlutId() + "|" + nimi + "|" + valmistaja + "|" + tyylilaji + "|" + alkoholiprosentit + "|" + IBU;
    }
    
    /**
     * Selvitetään oluen tiedot |-merkillä erotellusta merkkijonosta
     * Pitää huolen että seuraavaID on isompi kuin olutID
     * @param rivi josta oluen tiedot otetaan
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.parse("     4  |    Cloudberry Saison   |  Pyynikin Brewing Company");
     * olut.getOlutId() === 4;
     * olut.toString().startsWith("4|Cloudberry Saison|Pyynikin Brewing Company|") === true;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setOlutId(Mjonot.erota(sb, '|', olutId));
        nimi = Mjonot.erota(sb, '|', nimi);
        valmistaja = Mjonot.erota(sb, '|', valmistaja);
        tyylilaji = Mjonot.erota(sb, '|', tyylilaji);
        alkoholiprosentit = Mjonot.erota(sb, '|', alkoholiprosentit);
        IBU = Mjonot.erota(sb, '|', IBU);
    }
   
    /**
     * Tehdään identtinen klooni oluesta
     * @return kloonattu olut
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     * Olut olut = new Olut();
     * olut.parse("     4  |    Cloudberry Saison   |  Pyynikin Brewing Company");
     * Olut kopio = olut.clone();
     * kopio.toString() === olut.toString();
     * olut.parse("1|Cloudberry Saison|Pyynikin Brewing Company|saison|8.5|32.0");
     * kopio.toString().equals(olut.toString()) === false;
     * </pre>
     */
    @Override
    public Olut clone() throws CloneNotSupportedException {
        Olut uusi;
        uusi = (Olut) super.clone();
        return uusi;
    }
    
    /**
     * Palauttaa oluen idn
     * @return oluen id
     */
    public int getOlutId() {
        return olutId;
    }
    
    /**
     * Palautetaan oluen nimi
     * @return oluen nimi
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.taytaEsimTiedoilla();
     * olut.getNimi() =R= "Cloudberry Saison .*";
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }
    
    /**
     * Asettaa oluelle id:n ja varmistaa että seuraava numero
     * on suurempi kuin tähän mennessä suurin
     * @param n asetettava id
     */
    private void setOlutId(int n) {
        olutId = n;
        if (olutId >= seuraavaId) seuraavaId = olutId + 1;
    }
    
    /**
     * Palauttaa haluttua kenttää vastaavan kysymyksen
     * @param k monennenko kentän kysymys halutaan 
     * @return kenttää vastaava kysymys
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.getKysymys(1) === "nimi";
     * olut.getKysymys(2) === "valmistaja";
     * olut.getKysymys(3) === "tyylilaji";
     * olut.getKysymys(10) === "";
     * </pre>
     */
    public String getKysymys(int k) {
        switch (k) {
        case 0 : return "OlutId";
        case 1 : return "nimi";
        case 2 : return "valmistaja";
        case 3 : return "tyylilaji";
        case 4 : return "alkoholiprosentit";
        case 5 : return "IBU";
        default : return "";
        }
    }
    
    /**
     * Antaa k:nnen kentän sisällön merkkijonona
     * @param k monesko kenttä halutaan
     * @return kentän sisältö merkkijonona
     * @example
     * <pre name="test">
     * Olut olut = new Olut();
     * olut.parse("     4  |    Cloudberry Saison   |  Pyynikin Brewing Company");
     * olut.anna(0) === "4";
     * olut.anna(1) === "Cloudberry Saison";
     * olut.anna(2) === "Pyynikin Brewing Company";
     * olut.anna(3) === "";
     * olut.anna(10) === "";
     * </pre>
     */
    public String anna(int k) {
        switch (k) {
        case 0 : return "" + olutId;
        case 1 : return "" + nimi;
        case 2 : return "" + valmistaja;
        case 3 : return "" + tyylilaji;
        case 4 : return "" + alkoholiprosentit;
        case 5 : return "" + IBU;
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
     * Olut o1 = new Olut();
     * o1.aseta(1, "Cloudberry Saison") === null;
     * o1.aseta(3, "saison") === null;
     * o1.aseta(4, "kissa") === "Pitää olla numero";
     * o1.aseta(4, "5.5") === null;
     * </pre>
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuilder sb = new StringBuilder(tjono);
        switch (k) {
        case 0 : 
            setOlutId(Mjonot.erota(sb, '|', getOlutId()));
            return null;
        case 1 :
            nimi = tjono;
            return null;
        case 2 : 
            valmistaja = tjono;
            return null;
        case 3 :
            tyylilaji = tjono;
            return null;
        case 4 : 
           String virhe = kanta.Rand.onkoNumero(tjono);
           if (virhe != null) return virhe;
           alkoholiprosentit = Mjonot.erota(sb, '|', alkoholiprosentit);
           return null;
        case 5 :
            virhe = kanta.Rand.onkoNumero(tjono);
            if (virhe != null) return virhe;
            IBU = Mjonot.erota(sb, '|', IBU);
            return null;
        default : 
            return null;
        
        }
        
    }
    
    /**
     * Antaa k:nnen kentän sisällön merkkijonona
     * @param k monnennenko kentän sisltö annetaan
     * @return kentän sisältö merkkijonona
     * @example
     * <pre name="test">
     * Olut o1 = new Olut();
     * o1.parse("1|Cloudberry Saison|Pyynikin Brewing Company|saison|8.5|32.0");
     * o1.getAvain(0) === "1";
     * o1.getAvain(1) === "Cloudberry Saison";
     * o1.getAvain(4) === "8.5";
     * o1.getAvain(10) === "";
     * </pre>
     */
    public String getAvain(int k) {
        switch (k) {
        case 0: return "" + olutId;
        case 1: return "" + nimi;
        case 2: return "" + valmistaja;
        case 3: return "" + tyylilaji;
        case 4: return "" + alkoholiprosentit;
        case 5: return "" + IBU;
        default: return "";
        }
        
    }
    

    /**
     * @author Iina
     * @version 9.4.2021
     * Luokka joka osaa verrata kahta olutta keskenään
     */
    public static class Vertailija implements Comparator<Olut> {
        private int k;
        
        /**
         * Alustetaan vertailija vertailemaan tietyn kentän perusteella
         * @param k minkä kentän mukaan vertaillaan
         */
        public Vertailija(int k) {
            this.k = k;
        }
        
        /**
         * Vertaa kahta olutta keskenään
         * @param o1 1. verrattava olut
         * @param o2 2. verrattava olut
         * @return <0 jos o1 < o2, ==0 jos o1 === o2, muuten >0
         * @example
         * <pre name="test">
         * #PACKAGEIMPORT
         * Olut o1 = new Olut(); o1.parse("1|Cloudberry Saison 2508|Pyynikin Brewing Company|saison|8.5|32.0");
         * Olut o2 = new Olut(); o2.parse("2|Red|Teerenpeli|red ale|5.0|30.0");
         * Olut o3 = new Olut(); o3.parse("3|Candy Beer Saga vol.1|Panimoyhtiö X|sour ale|5.5|42.0");
         * Vertailija v1 = new Vertailija(0);
         * v1.compare(o1, o2) < 0 === true;
         * v1.compare(o3, o1) > 0 === true;
         * v1.compare(o2, o3) < 0 === true;
         * Vertailija v2 = new Vertailija(1);
         * v2.compare(o1, o2) < 0 === true;
         * v2.compare(o2, o3) > 0 === true;
         * v2.compare(o1, o3) > 0 === true;
         * </pre>
         */
        @Override
        public int compare(Olut o1, Olut o2) {
            return o1.getAvain(k).compareTo(o2.getAvain(k));
        }
        
    }
    
    /**
     * Testipääohjelma oluelle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Olut olut1 = new Olut();
        Olut olut2 = new Olut();
        
        olut1.rekisteroi();
        olut2.rekisteroi();
        
        olut1.tulosta(System.out);
        olut1.taytaEsimTiedoilla();
        olut1.tulosta(System.out);
        
        olut2.tulosta(System.out);
        olut2.taytaEsimTiedoilla();
        olut2.tulosta(System.out);
    
    }

}
