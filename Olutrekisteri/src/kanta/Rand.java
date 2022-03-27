package kanta;

/**
 * @author Iina
 * @version 14.4.2021
 *
 */
public class Rand {
    
    
    /**
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }
    
    /**
     * Arvotaan satunnainen desimaaliluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static double randD(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return n;
    }
    
    /**
     * Tarkastaa sisältääkö annettu merkkijono numeron
     * @param s annettu merkkijono
     * @return null jos on, muuten virheilmoituksen
     */
    public static String onkoNumero(String s) {
        if (!s.matches("[0-9]+\\.?[0-9]*$")) return "Pitää olla numero";
        return null;
    }

}
