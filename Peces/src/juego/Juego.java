package juego;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import pantalla.Pantalla;

public class Juego extends Thread {	
	private Pantalla pantalla;
	private List<Pez> peces;
	
	public Juego() {
		peces = new ArrayList<Pez>();
		pantalla = new Pantalla(this);
		
		int numPeces = 1;
		try {
			numPeces = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad de peces"));
		} catch(Exception e) {
			
		}
		
		for(int i=0; i<numPeces; i++) {
			new Pez(this);
		}
		
		start();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				int puntos = (int) (Math.random()*10)+1;
				new Pez(this, puntos);
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
