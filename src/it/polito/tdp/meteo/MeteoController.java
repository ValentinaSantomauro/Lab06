package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

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

		Integer m =   boxMese.getValue();
		if(m!=0) {
			String best = model.trovaSequenza(m) ;
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Mese %d: ", m));
    		sb.append("\n");
    		sb.append(String.format("%s", best));
    		txtResult.appendText(sb.toString());
			
		}
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		/***pt 1**/ 
		txtResult.clear();
		
		Integer m =   boxMese.getValue();
		if(m!=0) {
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Mese %d: ", m));
    		sb.append("\n");
    		sb.append(String.format("%s", model.getUmiditaMedia(m)));
    		txtResult.appendText(sb.toString());
			
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
					boxMese.getItems().add(mese) ;
				
	}

}
