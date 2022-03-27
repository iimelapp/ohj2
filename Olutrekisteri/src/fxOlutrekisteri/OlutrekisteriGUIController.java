package fxOlutrekisteri;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import olutrekisteri.Arvio;
import olutrekisteri.Olut;
import olutrekisteri.Olutrekisteri;
import olutrekisteri.SailoException;
import static fxOlutrekisteri.MuokkaaOlutController.*;

/**
 * Luokka käyttöliittymän tapahtumien käsittelemiseksi
 * @author Iina
 * @version 19.4.2021
 *
 */
public class OlutrekisteriGUIController implements Initializable{
    
    @FXML private ListChooser<Olut> chooserOluet;
    @FXML private ScrollPane panelOlut;
    @FXML private StringGrid<Arvio> tableArviot;
    @FXML private GridPane gridOlut;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private TextField hakuehto;
    @FXML private Label labelVirhe;
    
       
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta(); 
    }

    @FXML private void handleApua() {
        apua();
    }

    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    @FXML private void handleLisaaOlut() {
        lisaaOlut();
    }
    
    @FXML private void handleLisaaArvio() {
        lisaaArvio();
    }

    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }

    @FXML private void handleMuokkaaOlut() {
        muokkaaOlut(kentta);
    }
    
    @FXML private void handleMuokkaaArvio() {
        muokkaaArvio();
    }

    @FXML private void handlePoistaOlut() {
        poistaOlut();
    }
    
    @FXML private void handlePoistaArvio() {
        poistaArvio();
    }

    @FXML private void handleTallenna() {
        tallenna();
    }

    @FXML private void handleTietoja() {
        ModalController.showModal(OlutrekisteriGUIController.class.getResource("TietojaView.fxml"), "Tietoja", null, ""); 
    }

    @FXML private void handleTop() {
        naytaParhaat();
        
    }
    
    
    /*=======================================================================================*/
    


    private Olutrekisteri rekisteri;
    private Olut olutKohdalla;
    private TextField[] edits;
    private int kentta = 1;
    private static Olut apuolut = new Olut();
    
    /**
     * Tekee tarvittavat alustukset
     */
    private void alusta() {
        chooserOluet.clear();
        chooserOluet.addSelectionListener(e -> naytaOlut());
        cbKentat.clear();
        for (int k = apuolut.ekaKentta(); k < apuolut.getKenttia(); k++) {
            cbKentat.add(apuolut.getKysymys(k), null);
        }
        cbKentat.getSelectionModel().select(0);
        edits = MuokkaaOlutController.luoKentat(gridOlut);
        
        for (TextField edit : edits) {
            if (edit != null) {
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> {if (e.getClickCount() > 1) 
                    muokkaaOlut(getFieldId(e.getSource(), 0));
                });
                
                edit.focusedProperty().addListener((a, o, n) -> kentta = getFieldId(edit, kentta));
            }
        }
        tableArviot.setOnMouseClicked(e -> { if (e.getClickCount() > 1) muokkaaArvio(); } ); 
    }
    
    /**
     * Aukaisee ohjeet ohjelman käyttöön
     */
    private void apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2021k/ht/iimelapp");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * Poistaa oluen
     */
    private void poistaOlut() {
        Olut olut = olutKohdalla;
        if (olut == null) return;
        if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko olut: " + olut.getNimi(), "Kyllä", "Ei")) return;
        rekisteri.poista(olut);
        int index = chooserOluet.getSelectedIndex();
        hae(0);
        chooserOluet.setSelectedIndex(index);
    }
    
    /**
     * Poistaa arvion
     */
    private void poistaArvio() {
        int rivi = tableArviot.getRowNr();
        if (rivi < 0) return;
        Arvio arvio = tableArviot.getObject();
        if (arvio == null) return;
        rekisteri.poista(arvio);
        naytaArviot(olutKohdalla);
        int arvioita = tableArviot.getItems().size();
        if (rivi >= arvioita) rivi = arvioita - 1;
        tableArviot.getFocusModel().focus(rivi);
        tableArviot.getSelectionModel().select(rivi);
    }
    
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @return virheen jos lukeminen ei onnistu
     */
    private String lueTiedosto() {
        try {
            rekisteri.lueTiedostosta("olutrekisteri");
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage();
            if (virhe != null) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
    /**
     * Tallentaa tehdyt muutokset
     */
    private String tallenna() {
        try {
            rekisteri.tallenna();
            Dialogs.showMessageDialog("Tallennetaan");
            return null;
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia " + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Asetetaan käytettävä olutrekisteri
     * @param rekisteri olutrekisteri jota käytetään
     */
    public void setRekisteri(Olutrekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaOlut();
        lueTiedosto();
    }
    
    /**
     * Hakee halutun oluen tiedot     
     * @param i oluen id, joka aktivoidaan haun jälkeen, jos 0, aktivoidaan nykyinen olut
     */
    private void hae(int i) {
        int id = i;
        if (id == 0) {
            Olut olut = olutKohdalla;
            if (olut != null) id = olut.getOlutId();
        }
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apuolut.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        
        chooserOluet.clear();
        
        int index = 0; 
        Collection<Olut> oluet;
        try {
            oluet = rekisteri.etsi(ehto, k);
            int j = 0;
            for (Olut o : oluet) {
                if (o.getOlutId() == id) index = j;
                chooserOluet.add(o.getNimi(), o);
                j++;
            }
        } catch (SailoException e) {
            naytaVirhe("Hakemisessa ongelmia " + e.getMessage());
        }
        chooserOluet.setSelectedIndex(index);
    }
    
    /**
     * Näyttää virheen käyttöliittymässä
     * @param virhe näytettävä virhe
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /**
     * Lisää uuden oluen
     */
    private void lisaaOlut() {
            Olut uusi = new Olut();
            uusi = MuokkaaOlutController.kysyOlut(null, uusi, 1);
            if (uusi == null) return;
            uusi.rekisteroi();
            rekisteri.lisaa(uusi);
            hae(uusi.getOlutId());       
    }
    
    /**
     * Näyttää listasta valitun oluen tiedot
     */
    private void naytaOlut() {
        olutKohdalla = chooserOluet.getSelectedObject();
        if (olutKohdalla == null) return;       
        MuokkaaOlutController.naytaOlut(edits, olutKohdalla);
        naytaArviot(olutKohdalla);
        
    }
    
    /**
     * Muokkaa oluen tietoja
     * @param k monennenko kentän tietoja muokataan
     */
    private void muokkaaOlut(int k) {
        if (olutKohdalla == null) return;

        try {
            Olut olut;
            olut = MuokkaaOlutController.kysyOlut(null, olutKohdalla.clone(), k);
            if (olut == null) return;
            rekisteri.korvaaTaiLisaa(olut);
            hae(olut.getOlutId());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }        

    }
    
    /**
     * Näyttää tietyn oluen arviot taulukossa
     * @param olut olut jonka arviot näytetään
     */
    private void naytaArviot(Olut olut) {
        tableArviot.clear();
        if (olut == null) return;
        List<Arvio> arviot = rekisteri.annaArviot(olut);
        if (arviot.size() == 0) return;
        for (Arvio a : arviot) {
            naytaArvio(a);
        }
    }
    
    /**
     * Näyttää arvion taulukossa
     * @param a näytettävä arvio
     */
    private void naytaArvio(Arvio a) {
        int kenttia = a.getKenttia();
        String[] rivi = new String[kenttia-a.ekaKentta()];
        for (int i = 0, k = a.ekaKentta(); k < kenttia; i++, k++) {
            rivi[i] = a.anna(k);
        }
        tableArviot.add(a, rivi);
    }
    
    
    /**
     * Lisää uuden arvion oluelle
     */
    private void lisaaArvio() {
        if (olutKohdalla == null) return;
        Arvio a = new Arvio(olutKohdalla.getOlutId());
        a = MuokkaaArvioController.kysyArvio(null, a, 0);
        if (a == null) return;
        a.rekisteroi();
        rekisteri.lisaa(a);
        naytaArviot(olutKohdalla);
    }
    
    
    /**
     * Muokkaa tiettyä arviota
     */
    private void muokkaaArvio() {
        int r = tableArviot.getRowNr();
        if (r < 0) return;
        Arvio a = tableArviot.getObject();
        if (a == null) return;
        int k = tableArviot.getColumnNr()+a.ekaKentta();
        try {
            a = MuokkaaArvioController.kysyArvio(null, a.clone(), k);
            if (a == null) return;
            rekisteri.korvaaTaiLisaa(a);
            naytaArviot(olutKohdalla);
            tableArviot.selectRow(r);
        } catch (CloneNotSupportedException e) {
            e.getMessage();
        }
        
    }
    
    /**
     * Näyttää rekisterin parhaat arvosanat saaneet oluet dialogissa
     */
    private void naytaParhaat() {
        String[] parhaat = rekisteri.etsiParhaat();
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < parhaat.length; i++) {
            sb.append(parhaat[i] + ", ");
        }
        ToplistaController.naytaParhaat(sb.toString());
    }
    
}
