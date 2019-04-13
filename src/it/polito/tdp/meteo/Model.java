package it.polito.tdp.meteo;

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

	
	public String getUmiditaMedia(int mese) {
		
		/*
		 * 
		Citta milano = new Citta("Milano");
		Citta torino = new Citta("Torino");
		Citta genova = new Citta("Genova");

		//Questo metodo mi ritorna una lista di rilevamenti contenenti tutti i rilevamenti in un certo mese per una certa città 
		milano.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		torino.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		genova.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		
		
		String mediaTo = Double.toString(this.calcolaUmiditaMedia(rilTo));
		String mediaMi = Double.toString(this.calcolaUmiditaMedia(rilMi));
		String mediaGen = Double.toString(this.calcolaUmiditaMedia(rilGen));
		 * 
		 */
		
		String result="";
		for(Citta c: citta) {
			result += ( c.getNome() + " "+ dao.getAvgRilevamentiLocalitaMese(mese, c.getNome())+ "\n") ; 
		}
		
		return result; 
	}

	/*public double calcolaUmiditaMedia(List <Rilevamento> ril) {
		int somma = 0;

		for(Rilevamento r : ril) {
			somma += r.getUmidita();
		}
		
		return somma/ril.size();
	}*/
	
	
	
	List <Citta> best;
	double costo_best; 
	
	public String trovaSequenza(int mese) {
		//avvio della ricorsione
		
		best = null;
		costo_best=0.0;
		
		List <Citta> parziale= new ArrayList<Citta>();
		
		cerca(parziale, 0);
		
		String nomiCitta ="";
		
		for(Citta c: best) {
			nomiCitta+= c.getNome()+"\n";
		}
		return nomiCitta;
		
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
			if(best == null || costo > punteggioSoluzione(best))
		//???
				best = new ArrayList<>(parziale);
			
		}
		
		//caso normale: generatore di sottoproblemi
		else {
			
			for(Citta c : citta) {
			if(!this.controllaParziale(parziale))
				return;
			//altrimenti ha passato i controlli dei giorni precedenti
			else {
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

	private boolean controllaParziale(List<Citta> parziale) {
		//tutte le citta almeno una volta in 15 gg
		
		if(parziale.size() == NUMERO_GIORNI_TOTALI) {
			for ( Citta sc1 : parziale) {
				for(Citta sc2 : citta) {
					//se il nome della citta compare in parziale incremento il suo contatore
					if(sc2.getNome().equals(sc1.getNome())) {
						sc2.increaseCounter();	
					}	
				}	
			}
			//verifico che per tutte le città il contatore sia maggiore o uguale a 3 e minore di 6
			for(int i=0 ; i< parziale.size(); i++) {
				if((parziale.get(i).getNome()).equals((parziale.get(i+1).getNome()).equals(parziale.get(i+2).getNome())))
				{//ci sono tre giorni consecutivi 
					return true;
					}
				}
			
			for(Citta c : citta) {
				//NON BASTA mettere >=3 : devono essere tre giorni consecutivi! 
				if(c.getCounter()>=3 || c.getCounter()<=6)
					return true; 
			}
		}
		
		return false;
	}
	
}
