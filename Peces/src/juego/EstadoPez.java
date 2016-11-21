package juego;

import java.awt.Color;

public enum EstadoPez {
	NADANDO(Color.WHITE),
	ESCAPANDO(Color.RED),
	CAZANDO(Color.GREEN),
	MUERTO(Color.BLACK);
	
	private Color color;
	
	private EstadoPez(Color color) {
		this.color = color;
	}
	
	public double obtenerVelocidad() {
		switch(this) {
		case NADANDO:
			return Math.random()*2+2; //2;4
		case ESCAPANDO:
		case CAZANDO:
			return Math.random()*3+3; //3;6
		case MUERTO:
			return 0;
		default:
			return 4;
		}
	}
	
	public Color getColor() {
		return color;
	}
}
