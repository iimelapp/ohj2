/**
 * 
 */
package olutrekisteri;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Iina
 * @version 24.2.2021
 *
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Poikkeuksen muodostaja jolle tuodaan parametrina poikkeuksessa käytettävä viesti
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
    
}
