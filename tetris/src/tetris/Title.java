package tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel{

	/**
	 * generado automaticamente para eliminar low warning
	 */
	private static final long serialVersionUID = 8256710284819109855L;
    //variables
	private BufferedImage instructions;// Lectura de archivo con las instrucciones
	private WindowGame window;// ventana donde se produce 
	private BufferedImage[] playButton = new BufferedImage[2];// lectura de otras imagenes, los botones entrada del juego
	private Timer timer;// temporizador para accionar eventos
	
	//constructor
	//@param ventana de juego
	
	public Title(WindowGame window) {
		 instructions = ImageLoader.loadImage("src/imagenes/arrow.png");// cargamos imagen desde direccion 
			timer = new Timer(1000/60, new ActionListener(){// activamos el temporizador con
     // sobreescribimos directamente la funcion action performed
				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();// provocando una actualizacion de lo mostrado por pantalla
				}
				
			});
			timer.start();// ponemos el temporizador en marcha
			this.window = window; // inicializamos variable con el parametro
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);// llamamos a la funcion del padre
		
		g.setColor(Color.BLACK);// elegimos color
		g.fillRect(0, 0, WindowGame.WIDTH, WindowGame.HEIGHT);//llenamos el area de juego(rectangulo)
		// cargamos la imagen con las instrucciones
		g.drawImage(instructions, WindowGame.WIDTH/2 - instructions.getWidth()/2,
				30 - instructions.getHeight()/2 + 150, null);
		// nuevo color para el texto
        g.setColor(Color.WHITE);
        //escribimos el texto
		g.drawString("Pulsa la barra espaciadora para comenzar", 150, WindowGame.HEIGHT / 2 + 100);
		
		
	}	

	
// funciones autogeneradas para solventar low warning
	public BufferedImage[] getPlayButton() {
		return playButton;
	}

	public void setPlayButton(BufferedImage[] playButton) {
		this.playButton = playButton;
	}

}
