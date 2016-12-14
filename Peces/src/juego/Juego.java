package juego;

import java.util.ArrayList;
import java.util.List;

import pantalla.Pantalla;

public class Juego extends Thread {	
	private Pantalla pantalla;
	private List<Pez> peces;
	private String[] nombres = {"Vitor", "Sergio", "Gallego", "Jesus", "Borja", "Sire", "Ander", 
			"Torre", "Peio", "Vaquero", "Josu", "Aingeru", "Ameyugo", "Alain", "Alex", "Paces", 
			"Ioseba", "Christian", "Lara", "Nico", "Iñigo", "Vanessa", "Txemator", "Esmeralda", 
			"Lander", "Charo", "Cositas", "Luca", "Joseba"};
	
	public Juego() {
		peces = new ArrayList<Pez>();
		pantalla = new Pantalla(this);
		List<String> nombresCogidos = new ArrayList<>();
		while(nombresCogidos.size() != nombres.length) {
			while(true) {
				int rnd = (int) (Math.random()*nombres.length);
				String nombre = nombres[rnd];
				if(!nombresCogidos.contains(nombre)) {
					new Pez(this, nombre);
					nombresCogidos.add(nombre);
					break;
				}
			}
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
