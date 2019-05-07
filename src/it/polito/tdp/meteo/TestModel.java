package it.polito.tdp.meteo;

import java.time.*;



public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
	//	System.out.println(m.getUmiditaMedia(12));
		LocalDate lDate = LocalDate.now();
		
	
		System.out.println(m.trovaSequenza(lDate.getMonth()));
		
	//	System.out.println(m.trovaSequenza());
	}

}
