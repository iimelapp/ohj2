package fxOlutrekisteri;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import olutrekisteri.Olut;

/**
 * Luokka oluen tietojen muokkaukselle
 * @author Iina
 * @version 19.4.2021
 *
 */
public class MuokkaaOlutController implements ModalControllerInterface<Olut>, Initializable {
    @FXML private TextField textNimi;
    @FXML private TextField textValmistaja;
    @FXML private TextField textTyyli;
    @FXML private TextField textProsentit;
    @FXML private TextField textIBU;
    @FXML private Label labelNimi;
    @FXML private Label labelVirhe;
    @FXML private GridPane gridOlut;

    @FXML private void handleOK() {
        if (olutKohdalla != null && olutKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }
    
    @FXML private void handleCancel() {
        olutKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    @Override
    public Olut getResult() {
        return olutKohdalla;
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apu.ekaKentta(), Math.min(kentta, apu.getKenttia()-1));
        edits[this.kentta].requestFocus();
        
    }

    @Override
    public void setDefault(Olut oletus) {
        olutKohdalla = oletus;
        naytaOlut(olutKohdalla);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
    
    
    //=============================================================================================================
    
    private Olut olutKohdalla;
    private TextField[] edits;
    private int kentta = 1;
    private static Olut apu = new Olut();
    
    /**
     * Alustetaan dialogi
     */
    private void alusta() {
        edits = luoKentat(gridOlut);
        for (TextField edit : edits) {
            if (edit != null) edit.setOnKeyReleased(e -> kasitteleMuutosOlueen(edit));
        }
    }
    
    /**
     * Luodaan oluen kentät
     * @param grid mihin kentät luodaan
     * @return luodut kentät
     */
    public static TextField[] luoKentat(GridPane grid) {
        grid.getChildren().clear();
        TextField[] fedits = new TextField[apu.getKenttia()];
        
        for (int i = 0, k = apu.ekaKentta(); k < apu.getKenttia(); k++, i++) {
            Label label = new Label(apu.getKysymys(k));
            grid.add(label, 0, i);
            TextField edit = new TextField();
            fedits[k] = edit;
            edit.setId("e" + k);
            grid.add(edit,  1, i);
        }
        return fedits;
    }
    
    /**
     * Käsitellään muutos oluen tietoihin
     * @param edit tekstikenttä jonka sisältö muutetaan
     */
    private void kasitteleMuutosOlueen(TextField edit) {
        if (olutKohdalla == null) return;
        int k = getFieldId(edit, apu.ekaKentta());
        String s = edit.getText();
        String virhe = "";
        virhe = olutKohdalla.aseta(k, s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    /**
     * Näytetään virhe käyttöliittymässä jos syöte on virheellinen
     * @param virhe näytettävä virhe
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /**
     * Palautetaan objektin id:sta saatava luku
     * @param obj tutkittava objekti
     * @param oletus mitä palautetaan jos objektin id:ta ei voi palauttaa
     * @return objektin id
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }
    
    
    /**
     * Luodaan oluen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia
     * @param oletus olut jonka tiedot näytetään oletuksena
     * @param kentta kenttä jossa fokus kun näytetään
     * @return null jos painetaan cancel, muuten täytetty tietue
     */
    public static Olut kysyOlut(Stage modalityStage, Olut oletus, int kentta) {
        return ModalController.<Olut, MuokkaaOlutController>showModal(OlutrekisteriGUIController.class.getResource("MuokkaaOlutView.fxml"), "Olut", modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta));  

    }
    
    /**
     * Näytetään olut käyttöliittymässä
     * @param olut näytettävä olut
     */
    private void naytaOlut(Olut olut) {
        naytaOlut(edits, olut);
        if (olut == null) return;
        
    }
    
    /**
     * Asetetaan käytettävä kenttä
     * @param kentta kentän numero
     */
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }

    /**
     * Näytetään oluen tiedot textfield-komponenteissa
     * @param edits taulukko jossa tekstikenttiä
     * @param olut näytettävä olut
     */
    public static void naytaOlut(TextField[] edits, Olut olut) {
        if (olut == null) return;
        for (int i = olut.ekaKentta(); i < olut.getKenttia(); i++) {
            edits[i].setText(olut.anna(i));
        }
        
    }

}
