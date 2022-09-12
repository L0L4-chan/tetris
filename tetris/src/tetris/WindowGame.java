package tetris;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;



public class WindowGame implements KeyListener{// clase que va a ser la ventana donde se produzca el juego

	private Board board= new Board();// tablero
	private JFrame window;//frame
	public static final int WIDTH = 475, HEIGHT = 650; // altura y anchura
	private Title title;// 
	
	public WindowGame() {
		
		window = new JFrame("TETRIS");//Inicializamos y ponemos nombre al marco
		window.setSize(WIDTH, HEIGHT);// damos dimension
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// establecemos la operacion de cierre
		window.setResizable(false);// impedimos que puedan modificar lel tamaño
		window.setLocationRelativeTo(null);// la pantalla aparecera en el medio de la pantalla
		
		board= new Board();// inicializamos el tablero o panel
		window.add(board);
		
		window.addKeyListener (this);// añadimos el objeto oyente
		window.setVisible(true);// hacemos visible
		window.setVisible(false);
		
		title = new Title(this);//creamos panel de titulo que muestra las teclas del juego
 		window.add(title);
		window.setVisible(true);
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_SPACE) {// si es la barra espaciadora
					window.remove(title); //elimina el panel que pop up, en este caso title
			        startTetris();// activamos el juego
			        }
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void startTetris() {
	
		 window.addKeyListener(board);
		 window.addMouseMotionListener(board);//añadimos quien recogera el movimiento del raton
	     window.addMouseListener(board);//añadimos quien recogera los eventos provocados por el raton
	     window.repaint();
	    
	    board.startGame();// comenzamos el juego
	    // window.revalidate();// refresh
		
	}
}
