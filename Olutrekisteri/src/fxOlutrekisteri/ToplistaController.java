package fxOlutrekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * @author Iina
 * @version 19.4.2021
 * Luokka parhaiden oluiden näyttämiselle
 */
public class ToplistaController implements ModalControllerInterface<String>, Initializable {
    
    @FXML private StringGrid<String[]> tableParhaat;
    
    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String oletus) {
        alusta(oletus);       
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
     // TODO Auto-generated method stub
        
    }
    
    //=========================================================================
    
    /**
     * Näyttää rekisterin parhaat oluet dialogissa
     * @param parhaat merkkijono parhaista oluista
     */
    public static void naytaParhaat(String parhaat) {
        ModalController.showModal(ToplistaController.class.getResource("ToplistaView.fxml"), "Toplista", null, parhaat);
    }
    
    /**
     * Alustaa dialogin näyttämään parhaat oluet
     * @param oletus merkkijono parhaista oluista
     */
    public void alusta(String oletus) {
        StringBuilder sb = new StringBuilder(oletus);
        for (int i = 0; i < sb.length(); i++) {
            String rivi = Mjonot.erota(sb, ',', null);
            String nimi = rivi.substring(0, rivi.lastIndexOf(' '));
            String arvosana = rivi.substring(rivi.lastIndexOf(' ')+1);
            tableParhaat.add(nimi, arvosana);
        }
    }
    
}
