package pantalla;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

import juego.Juego;
import juego.Pez;

public class Pantalla extends JFrame implements KeyListener, Runnable {
	private static final long serialVersionUID = 1468893441392296103L;
	
	public final int WIDTH = 600;
	public final int HEIGHT = 600;
	
	private Juego juego;
	private BufferedImage bf;
	
	public Pantalla(Juego juego) {
		this.juego = juego;
		bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        
        addKeyListener(this);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		dispose();
        		System.exit(0);
        	}
        });
        
       	(new Thread(this)).start();
	}
	
	public void paint(Graphics g) {
		Graphics2D bff = (Graphics2D) bf.getGraphics();
		
		bff.setColor(Color.BLUE.brighter());
		bff.fillRect(0, 0, WIDTH, HEIGHT);	
		
		try {
			for(Pez pez : juego.getPeces()) {
				pez.pintar(bff);
			}
		} catch(ConcurrentModificationException e) {
			
		}		
		
		g.drawImage(bf, 0, 0, null);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				repaint();
				Thread.sleep(20);
			} catch(InterruptedException e) {
				
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
}
