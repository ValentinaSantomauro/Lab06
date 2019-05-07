package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	

	public List<Rilevamento> getRilevamentiLocalitaMese(Month mese, Citta citta) {
		
		/*seleziona l'umidità in base al mese e alla città */
		
		 String sql= "SELECT Localita,Data,Umidita FROM situazione WHERE MONTH(Data)=? AND Localita=? ";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			
			
			st.setString(2, citta.getNome());
			st.setInt(1, mese.getValue());
			ResultSet rs= st.executeQuery();
		
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return rilevamenti;
		
	}

	 
	public List<Citta> getAllCitta() {
		String sql = "SELECT DISTINCT Localita FROM situazione ORDER BY Localita";
		
		List<Citta> result = new ArrayList<Citta>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Citta(res.getString("Localita")));
			}

			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return result;
		
	}



	public Double getUmiditaMedia(Month m, Citta c) {
/*calcola la media delle umidità in base alla selezione di città e mese*/
		
		final String sql= "SELECT AVG(Umidita) AS U "+
				"FROM situazione "+
				"WHERE Localita= ? "+
				"AND MONTH (Data) =? " ;
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, c.getNome());
			st.setInt(2, m.getValue());
			ResultSet rs = st.executeQuery();
		
			rs.next(); // posiziona sulla prima (ed unica) riga

			Double u = rs.getDouble("U");

			conn.close();
			
			return u;
			

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}

}
