package juego;

import java.util.ArrayList;
import java.util.List;

import pantalla.Pantalla;

public class Juego extends Thread {	
	private Pantalla pantalla;
	private List<Pez> peces;
	private String[] nombres = {"Vitor", "Christian", "Sergio", "Gallego", "Jesus", "Borja", "Sire", "Ander", "Torre", "Peio", "Vaquero", "Josu", "Aingeru", "Ameyugo", "Alain", "Alex", "Paces", "Ioseba", "Lara", "Nico", "Iñigo", "Vanessa", "Txemator", "Esmeralda", "Lander"};
	
	public Juego() {
		peces = new ArrayList<Pez>();
		pantalla = new Pantalla(this);
		for(int i=0; i<nombres.length; i++) {
			new Pez(this, nombres[i]);
		}
		
		start();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep((long) Math.random()*2000+2000);
			} catch(InterruptedException e) {
				
			}
		}
	}
	
	public static void main(String[] args) {
		new Juego();
	}
	
	public List<Pez> getPeces() {
		return peces;
	}
	
	public Pantalla getPantalla() {
		return pantalla;
	}
}
