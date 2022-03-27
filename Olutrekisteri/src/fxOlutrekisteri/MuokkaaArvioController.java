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
import olutrekisteri.Arvio;

/**
 * Luokka arvion tietojen muokkaukselle
 * @author Iina
 * @version 19.4.2021
 *
 */
public class MuokkaaArvioController implements ModalControllerInterface<Arvio>, Initializable {

    @FXML private GridPane gridArvio;
    @FXML private Label labelVirhe;
    @FXML private TextField textArvosana;
    @FXML private TextField textPvm;
    @FXML private TextField textKommentit;
    
    @FXML private void handleOK() {
        ModalController.closeStage(labelVirhe);
    }
    
    @FXML private void handleCancel() {
        arvioKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apu.ekaKentta(), Math.min(kentta, apu.getKenttia()-1));
        edits[this.kentta].requestFocus();
        
    }
   
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }

    @Override
    public Arvio getResult() {
        return arvioKohdalla;
    }

    @Override
    public void setDefault(Arvio arvio) {
        arvioKohdalla = arvio;
        naytaArvio(arvioKohdalla);
        
    }
    
    //===================================================================================================
    
    
    private Arvio arvioKohdalla;
    private TextField[] edits;
    private int kentta = 0;
    private static Arvio apu = new Arvio();
    
    /**
     * Alustetaan dialogi 
     */
    private void alusta() {
        edits = luoKentat(gridArvio);
        for (TextField edit : edits) {
            if (edit != null) {
                edit.setOnKeyReleased(e -> kasitteleMuutos((TextField)(e.getSource())));
            }
        }
    }
    
    /**
     * Käsitellään muutos arvioon
     * @param edit kenttä jonka sisältöä muutetaan
     */
    private void kasitteleMuutos(TextField edit) {
        if (arvioKohdalla == null) return;
        int k = getFieldId(edit, apu.ekaKentta());
        String s = edit.getText();
        String virhe = "";
        virhe = arvioKohdalla.aseta(k, s);
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
     * Luodaan arvion kentät gridpaneen
     * @param grid mihin kentät luodaan
     * @return luodut tekstikentät
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
     * Luodaan arvion kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia
     * @param oletus arvio jonka tiedot näytetään oletuksena
     * @param kentta kenttä jossa fokus kun näytetään
     * @return null jos painetaan cancel, muuten täytetty tietue
     */
    public static Arvio kysyArvio(Stage modalityStage, Arvio oletus, int kentta) {
        return ModalController.<Arvio, MuokkaaArvioController>showModal(OlutrekisteriGUIController.class.getResource("MuokkaaArvioView.fxml"), "Arvio", modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta));  

    }

    /**
     * Asetetaan käytettävä kenttä
     * @param kentta kentän numero
     */
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    /**
     * Näyttää arvion käyttöliittymässä
     * @param arvio näytettävä arvio
     */
    private void naytaArvio(Arvio arvio) {
        naytaArvio(edits, arvio);
        if (arvio == null) return;
        
    }

    /**
     * Näytetään oluen tiedot textfield-komponenteissa
     * @param edits taulukko jossa tekstikenttiä
     * @param arvio näytettävä olut
     */
    public static void naytaArvio(TextField[] edits, Arvio arvio) {
        if (arvio == null) return;
        for (int i = arvio.ekaKentta(); i < arvio.getKenttia(); i++) {
            edits[i].setText(arvio.anna(i));
        }
        
    }   

}