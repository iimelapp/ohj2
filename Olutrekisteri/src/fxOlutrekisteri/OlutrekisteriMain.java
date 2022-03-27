package fxOlutrekisteri;
	
import javafx.application.Application;
import javafx.stage.Stage;
import olutrekisteri.Olutrekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Iina
 * @version 22.3.2021
 * Pääohjelma olutrekisterin käynnistämiseksi
 */
public class OlutrekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("OlutrekisteriGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final OlutrekisteriGUIController rekCtrl = (OlutrekisteriGUIController)ldr.getController();

			Scene scene = new Scene(root,650,550);
			scene.getStylesheets().add(getClass().getResource("olutrekisteri.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Olutrekisteri");
			
			Olutrekisteri rekisteri = new Olutrekisteri();
			rekCtrl.setRekisteri(rekisteri);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
