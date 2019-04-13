package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			Connection conn = DBConnect.getInstance().getConnection();
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
	
	

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		/*seleziona l'umidità in base al mese e alla città */
		
		final String sql= "SELECT * "+
				"FROM situazione "+
				"WHERE Localita= ? AND MONTH (Data) =? "
				+ " ORDER BY data ASC" ;
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			
			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet res = st.executeQuery() ;
		
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

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		/*calcola la media delle umidità in base alla selezione di città e mese*/
		
		final String sql= "SELECT AVG(Umidita) AS U "+
				"FROM situazione "+
				"WHERE Localita= ? AND MONTH (Data) =? " ;
		
		Double umidita = 0.0;
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, localita);
			st.setInt(2, mese);
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



	public List<Citta> getAllCitta() {
		String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";
		
		List<Citta> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Citta(res.getString("localita")));
			}

			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return result;
		
	}

}
