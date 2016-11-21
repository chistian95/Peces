package juego;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ConcurrentModificationException;

import mates.Vector2D;
import pantalla.Pintable;

public class Pez extends Thread implements Pintable {
	private Juego juego;
	
	private EstadoPez estado;
	private int anchoPez;
	private int altoPez;
	
	private int puntos;
	private String nombre;
	private int alfa;
	private double vida;
	private double velocidad;
	
	private Vector2D posicion;
	private Vector2D destino;
	
	private Pez target;
	
	public Pez(Juego juego) {
		this(juego, 1, null);
	}
	
	public Pez(Juego juego, String nombre) {
		this(juego, 1, nombre);
	}
	
	public Pez(Juego juego, int puntos, String nombre) {
		this.juego = juego;
		this.nombre = nombre;
		
		estado = EstadoPez.NADANDO;
		anchoPez = 10;
		altoPez = 10;
		
		this.puntos = puntos;
		alfa = 255;
		vida = 100;
		velocidad = estado.obtenerVelocidad();
		
		posicion = new Vector2D(Math.random()*juego.getPantalla().WIDTH, Math.random()*juego.getPantalla().HEIGHT);
		buscarDestino();
		start();
		
		juego.getPeces().add(this);
	}
	
	
	
	private void moverPez() {
		curar();
		
		switch(estado) {
		case ESCAPANDO:
		case NADANDO:
			moverNadando();
			break;
		case CAZANDO:
			moverCazando();
			break;
		case MUERTO:
			return;
		}	
		
		Vector2D direccion = destino.sub(posicion).normalize();
		posicion = posicion.add((direccion.scale(velocidad)));
		
		double x = limiteAncho(posicion.getX());
		double y = limiteAlto(posicion.getY());
		posicion.setLocation(x, y);
		
		buscarPeces();
	}
	
	private void moverNadando() {
		moverDestino();
		Rectangle rec = obtenerHitbox();
		if(rec.contains(destino.getX(), destino.getY())) {
			buscarDestino();
			velocidad = estado.obtenerVelocidad();
			return;
		}
	}
	
	private void moverCazando() {
		if(target == null) {
			return;
		}
		destino = target.posicion;
		Rectangle recThis = obtenerHitbox();
		Rectangle recTarget = target.obtenerHitbox();
		if(recThis.intersects(recTarget)) {
			atacar(target);
			
			double random = Math.random()*100;
			if(random < 33) {
				target.atacar(this);
			}
		}
	}
	
	private void buscarPeces() {
		try {
			for(Pez pez : juego.getPeces()) {
				if(pez == this || pez.estado.equals(EstadoPez.MUERTO)) {
					continue;
				}
				
				Point posPez = new Point((int) pez.posicion.getX(), (int) pez.posicion.getY());
				Point posThis = new Point((int) this.posicion.getX(), (int) this.posicion.getY());
				if(posPez.distance(posThis) > this.obtenerScope()) {
					continue;
				}
				
				if(puedeCazar(pez)) {
					this.estado = EstadoPez.CAZANDO;
					this.target = pez;
				} else {					
					this.estado = EstadoPez.ESCAPANDO;
				}
				return;
			}
		} catch(ConcurrentModificationException e) {
			
		}
		this.estado = EstadoPez.NADANDO;
	}
	
	private boolean puedeCazar(Pez pez) {
		double mordidasPez = this.vida / pez.puntos;
		double mordidasThis = pez.vida / this.puntos;
		
		return mordidasThis <= mordidasPez;
	}
	
	@Override
	public void pintar(Graphics2D g) {
		Rectangle rec = obtenerHitbox();
		
		g.setColor(estado.getColor());
		if(estado.equals(EstadoPez.MUERTO)) {
			g.setColor(new Color(0, 0, 0, alfa));
		}
		g.fillRect((int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight());
		
		if(!estado.equals(EstadoPez.MUERTO)) {
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1));
			g.drawRect((int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight());
			
			if(nombre == null) {
				int barraX = (int) rec.getX();
				int barraY = (int) (rec.getY() - rec.getHeight() * 0.2);
				int anchoBarra = (int) (rec.getWidth() * (vida / 100));
				int altoBarra = (int) (rec.getHeight() * 0.1);
				
				g.setColor(Color.GREEN);
				g.fillRect(barraX, barraY, anchoBarra, altoBarra);
			}
			
			
			g.setColor(estado.getColor());
			g.drawLine((int) posicion.getX(), (int) posicion.getY(), (int) destino.getX(), (int) destino.getY());
		}	
		
		if(nombre != null) {
			g.setColor(Color.WHITE);
			g.drawString(nombre, (int) rec.getX(), (int) (rec.getY() - rec.getHeight() * 0.2));
		}
	}
	
	private void atacar(Pez pez) {
		if(pez == null || pez.estado.equals(EstadoPez.MUERTO)) {
			return;
		}
		
		pez.vida -= puntos;
		
		double random = Math.random() * 100;
		if(random < 10) {
			pez.vida -= puntos;
		}
		
		pez.vida = pez.vida < 0 ? 0 : pez.vida;
		if(pez.vida <= 0) {
			pez.estado = EstadoPez.MUERTO;
			
			vida += pez.puntos;
			vida = vida > 100 ? 100 : vida;
			
			puntos++;
		}
	}
	
	private void curar() {
		double random = Math.random() * 100;
		if(random < 0.1 && vida < 100) {
			vida++;
		}
	}
	
	private Rectangle obtenerHitbox() {
		double x = posicion.getX() - (obtenerAncho() / 2);
		double y = posicion.getY() - (obtenerAlto() / 2);
		Rectangle rec = new Rectangle((int) x, (int) y, (int) obtenerAncho(), (int) obtenerAlto());
		
		return rec;
	}
	
	private double obtenerAncho() {
		return anchoPez + (anchoPez * puntos) / 25;
	}
	
	private double obtenerAlto() {
		return altoPez + (altoPez * puntos) / 25;
	}
	
	private double obtenerScope() {
		return obtenerAncho() * 10;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if(estado.equals(EstadoPez.MUERTO)) {
					break;
				}
				moverPez();
				Thread.sleep(20);
			} catch(InterruptedException e) {
				
			}
		}
		
		if(nombre != null) {
			return;
		}
		
		for(int c = 0; c < 250; c++) {
			try {
				alfa = (int) (255 - 255 * (c  / 250.0));
				Thread.sleep(10);
			} catch(InterruptedException e) {
				
			}
		}
		
		juego.getPeces().remove(this);
	}
	
	private void buscarDestino() {
		double x = posicion.getX() + Math.random()*400-200;
		double y = posicion.getY() + Math.random()*400-200;
		
		x = limiteAncho(x);
		y = limiteAlto(y);
		
		destino = new Vector2D(x, y);
	}
	
	private void moverDestino() {
		double x = destino.getX()+Math.random()*10-5;
		double y = destino.getY()+Math.random()*10-5;
		
		x = limiteAncho(x);
		y = limiteAlto(y);
		
		destino.setLocation(x, y);
	}
	
	private double limiteAncho(double dx) {
		dx = dx < 0 ? 0 : dx;
		dx = dx >= juego.getPantalla().WIDTH ? juego.getPantalla().WIDTH - 1 : dx;
		
		return dx;
	}
	
	private double limiteAlto(double dy) {
		dy = dy < 0 ? 0 : dy;
		dy = dy >= juego.getPantalla().HEIGHT ? juego.getPantalla().HEIGHT - 1 : dy;
		
		return dy;
	}
}
