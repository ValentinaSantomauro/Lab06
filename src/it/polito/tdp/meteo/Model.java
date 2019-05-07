package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;



public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	MeteoDAO dao;
	List <Citta> citta;
	
	public Model() {
	
		dao= new MeteoDAO();
		this.citta= dao.getAllCitta();
	}

	
	public Double getUmiditaMedia(Month m, Citta c) {
		
		
		return dao.getUmiditaMedia(m,c); 
	}

	
	
	List <Citta> best; //in cui ogni volta salvo la migliore
	
	
	public List <Citta> trovaSequenza(Month mese) {
		//avvio della ricorsione
		List <Citta> parziale= new ArrayList<Citta>();
		this.best = null;
		MeteoDAO dao2 = new MeteoDAO();
		// carica dentro ciascuna delle leCitta la lista dei rilevamenti nel mese
				// considerato (e solo quello)
				
				for (Citta c :citta) {
					c.setRilevamenti(dao2.getRilevamentiLocalitaMese(mese, c));
				}
		//--- costo---
				
		cerca(parziale, 0);
		
		return best;
		
	}
	
	
	private void cerca(List <Citta> parziale, int L) {

		//caso terminale
		if(L == NUMERO_GIORNI_TOTALI) {
			//al 15esimo giorno calcolo il costo della soluzione trovata
			Double costo = this.punteggioSoluzione(parziale);
		
			/*
			 * se non è ancora presente una lista di città migliori --> best
			 * oppure
			 * il costo della soluzione parziale trovata è minore del costo migliore
			 * 
			 */
			if(best == null || costo < punteggioSoluzione(best))
				best = new ArrayList<Citta>(parziale);
			
		}
		
		//caso normale: generatore di sottoproblemi
		else {
			
			for(Citta c : citta) {
			if(this.controllaParziale(c,parziale))
			{
			//altrimenti ha passato i controlli dei giorni precedenti
				parziale.add(c);
				cerca(parziale, L+1);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	}

	private Double punteggioSoluzione(List<Citta> soluzioneCandidata) {
		double score = 0.0;
		//prendo l'umidità del giorno 
		for(int gg=1 ; gg<= NUMERO_GIORNI_TOTALI; gg++) {
			Citta c = soluzioneCandidata.get(gg-1);
			double umid = c.getRilevamenti().get(gg-1).getUmidita();
			score+=umid;
		}
		
		//aggiungo 100 se il gioro successivo c'è un altra citta
		for(int gg=2; gg<= NUMERO_GIORNI_TOTALI; gg++) {
			if(!(soluzioneCandidata.get(gg-1).equals(soluzioneCandidata.get(gg-2))) ){
				score+=COST;
			}
		}
		
		return score;
	}

	private boolean controllaParziale(Citta prova, List<Citta> parziale) {
		//tutte le citta almeno una volta in 15 gg
		int conta=0;
		for(Citta precedente : parziale) {
			//chiediamo se la citta precedente è la stessa citta che sto inserendo, incremento il conteggio
			if(precedente.equals(prova))
				conta++;
		}
		if(conta>= NUMERO_GIORNI_CITTA_MAX)
			return false;
		//verifico i gg minimi
		//il primo gg della sequenza posso mettere quello che voglio
		if(parziale.size()==0)
		return true;
		
		if(parziale.size()==1 || parziale.size()==2) {
			//siamo nel secondo o terzo gg, non posso cambiare: devo rimanere almeno tre gg
			return parziale.get(parziale.size()-1).equals(prova);
			//se quello che cè in posizione -1 è uguale ritorna true e l'aggiunta è valida
			//verifica che in equals il controllo sia fatto sul nome
		}
		
		if(parziale.get(parziale.size()-1).equals(prova))
			return true; //posso sempre rimanere dopo 3 gg
		//se cambio citt: lo posso fare? verifico la citta precedente nella lista e quella due volte precedente
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size() -3)) ) {
			return true;
		}
		return false;
	}


	public List<Citta> getLeCitta() {
		
		return citta;
	}
	
}
