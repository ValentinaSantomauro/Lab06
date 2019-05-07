package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	Model model;
	
	
	public void setModel(Model model) {
		this.model=model;
	}
	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		/**pt2**/ 
		txtResult.clear();

		Month mese =boxMese.getValue();
		
		if(mese!=null) {
			
			List<Citta>	best = model.trovaSequenza(mese) ;
			txtResult.appendText("Sequenza ottima per il mese "+mese.toString()+"\n");
			
			txtResult.appendText(best+"\n");
		}
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		/***pt 1**/ 
		txtResult.clear();
		
		Month m =  boxMese.getValue();
		
		for(Citta c : model.getLeCitta()) {
			Double u = model.getUmiditaMedia(m, c);
			txtResult.appendText(String.format("Città: %s, Umidità: %f; \n", c.getNome(), u));
		}
			
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		// popola la boxMese con i 12 mesi dell'anno 
				for(int mese = 1; mese <= 12 ; mese ++)
					boxMese.getItems().add(Month.of(mese)) ;
				
				
				
	}

}
