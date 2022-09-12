package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{//  tablero
	
	/**
	 * creada para solventar un low warning
	 */
	private static final long serialVersionUID = -8230435433408484255L;
	
	private BufferedImage pause, refresh;// cargador de imagen
	// dimensiones de la zona de juego
	public static final int BOARD_WIDTH =10;
	public static final int BOARD_HEIGHT =20;
	public static final int TILE_SIZE = 30;// medida del lado de las casillas en las que se divide el panel 
	private Timer gameloop;//temporizador para rellamada
	private int FPS = 60; //
	private int delay = 1000/FPS;// cada cuanto se llamma al gameloop
	private Color[][] board = new Color[BOARD_WIDTH][BOARD_HEIGHT];	//zona de juego
	private Shape[] shapes = new Shape[7];// array con las formas que puede tomar cada pieza
	private static Shape currentShape, nextShape; // variables para la pieza en juego y la pieza que viene
	// variables de los MouseEvent
	private int mouseX, mouseY;// posicion del raton coordenadas x e y
	private boolean leftClick= false, gamePaused= false, gameOver = false;// booleanos de evento
	private Rectangle stopBounds, refreshBounds; // para la accion de desaparicion de las lineas completas
	private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
	        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    private Random random = new Random();
	// timer que revisara las entradas de evento
	private Timer buttonLapse = new Timer(300, new ActionListener() {
		 @Override
	       public void actionPerformed(ActionEvent e) {
	           buttonLapse.stop();
	        }
	   });
	private int score; // entero que almacena los puntos
    
	
	
	
	public Board() {
		
		pause = ImageLoader.loadImage("src/imagenes/Pause.png");// creamos imagen subiendo un archivo de imagen
        refresh = ImageLoader.loadImage("src/imagenes/refresh.png");// creamos imagen subiendo un archivo de imagen
      //inicializamos posicion de inicio del raton
        mouseX = 0;
        mouseY = 0;
       // creamos el area que ocuparan las imagenes
        stopBounds = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight() / 2);
        refreshBounds = new Rectangle(350, 500 - refresh.getHeight() - 20, refresh.getWidth(),
        refresh.getHeight() + refresh.getHeight() / 2);

		
		gameloop = new Timer(delay, new GameLooper());
	   
		
		// create las piezas del juego
        shapes[0] = new Shape(new int[][]{
            {1, 1, 1, 1} // I ;
        }, this, colors[0]);

        shapes[1] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // T ;
        }, this, colors[1]);

        shapes[2] = new Shape(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L ;
        }, this, colors[2]);

        shapes[3] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // J ;
        }, this, colors[3]);

        shapes[4] = new Shape(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S ;
        }, this, colors[4]);

        shapes[5] = new Shape(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Z ;
        }, this, colors[5]);

        shapes[6] = new Shape(new int[][]{
            {1, 1},
            {1, 1}, // 0;
        }, this, colors[6]);   
       
      setNextShape();  
      setCurrentShape();  
    }
	
	public void update() {
		//si el raton esta en el tablero, y se ha pulsado el raton izquierdo y  estamos en pausa, ni el juego se ha acabado
		
	     if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver) {
	            buttonLapse.start();//comenzamos partida
	            gamePaused = !gamePaused;// finalizamos pausa
	        }
              // si el raton esta colocado en los limites y se pulsa el boton izquierdo
	        if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
	            startGame();// comienza el juego
	        }
// si el juego esta en pausa o se ha acabado salimos
	        if (gamePaused || gameOver) {
	            return;
	        }
	        // se actualiza la pieza
	        currentShape.update();
	}
	
		

	public void paintComponent(Graphics g) {//sobreescribimoe el metodo para crear el panel.
		super.paintComponent(g);// llamamos a la funcion que estamos sobreescribiendo para no perder la funcionalidad de esta funcion
		g.setColor(Color.black);// elegimos color( )
		g.fillRect(0,0,BOARD_WIDTH*TILE_SIZE,BOARD_HEIGHT*TILE_SIZE);//llenamos todo el fondo
		
		// creamos el grid
		g.setColor(Color.green);
		
		
		for(int i =0; i<BOARD_WIDTH; i ++ ) {
			g.drawLine(i*TILE_SIZE, 0,i*TILE_SIZE ,BOARD_HEIGHT*TILE_SIZE);
			
		}
		for(int j =0; j<BOARD_HEIGHT; j ++ ) {
			g.drawLine(0, j*TILE_SIZE, BOARD_WIDTH*TILE_SIZE,j*TILE_SIZE);
		}
		for (int i = 0; i<BOARD_WIDTH;i++) {
			for (int j =0; j< BOARD_HEIGHT; j++) {
				if (board[i][j] != null) {
				g.setColor(board[i][j]);//escogemos color
				g.fillRect(j*TILE_SIZE,i*TILE_SIZE,TILE_SIZE, TILE_SIZE);//rellenamos
				}
			}
		}
		
	// cogemos el color de la proxima pieza
		g.setColor(nextShape.getColor());
		
	 for (int row = 0; row < nextShape.getCoords().length; row++) {//pintamos en la localizacion de la proxima pieza
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
              if (nextShape.getCoords()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Board.TILE_SIZE, Board.TILE_SIZE);
                }
            }
        }
        currentShape.render(g);//pintamos la figura actual
        
        //si estamos en los limites establecidos del rectangulo stopbounds
        if (stopBounds.contains(mouseX, mouseY)) {//pintamos en una posicion
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT), stopBounds.x + 3, stopBounds.y + 3, null);
        } else {
            g.drawImage(pause, stopBounds.x, stopBounds.y, null);//si no en otra
        }

        if (refreshBounds.contains(mouseX, mouseY)) {//si estamos dentro de los limites del refresh bound
            g.drawImage(refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), refreshBounds.x + 3, refreshBounds.y + 3, null);//dibujamos
        } else {
            g.drawImage(refresh, refreshBounds.x, refreshBounds.y, null);//o dibujamos
        }

        if (gamePaused) {//si estamos en pausa
            String gamePausedString = "GAME PAUSED";//texto a mostrar
            g.setColor(Color.CYAN);//color
            g.setFont(new Font("Georgia", Font.BOLD, 30));//tipo de letra
            g.drawString(gamePausedString, 35, WindowGame.HEIGHT / 2);//escribimos en posicion
        }
        if (gameOver) {//si se ha perdido
            String gameOverString = "GAME OVER";//texto a mostrar
            g.setColor(Color.CYAN);//color
            g.setFont(new Font("Georgia", Font.BOLD, 30));//tipo de letra
            g.drawString(gameOverString, 50, WindowGame.HEIGHT / 2);//escribimos en posicion
        }
        
        //si no se da ninguna de las situaciones anteriores
        g.setColor(Color.CYAN);//color

        g.setFont(new Font("Georgia", Font.BOLD, 20));//tipo de letra

        g.drawString("SCORE", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2);//marcador y su posicion
        g.drawString(score + "", WindowGame.WIDTH - 125, WindowGame.HEIGHT / 2 + 30);//puntuacion numerica y su posicion

        g.setColor(Color.CYAN);//color

        for (int i = 0; i <= HEIGHT; i++) {//enmarcamos
            g.drawLine(0, i * TILE_SIZE, WIDTH * TILE_SIZE, i * TILE_SIZE);
        }
        for (int j = 0; j <= WIDTH; j++) {//enmarcamos
            g.drawLine(j * TILE_SIZE, 0, j * TILE_SIZE, HEIGHT * 30);
        }
		
        currentShape.update();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
        mouseY = e.getY();
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
        mouseY = e.getY();
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		//sin codigo porque no es necesaria
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		//if (e.getButton() == MouseEvent.BUTTON1) {
         //   leftClick = true;
     //   }
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
           leftClick = false;
        }
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		//sin codigo porque no es necesaria
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		//sin codigo porque no es necesaria
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		//sin codigo porque no es necesaria
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		 if (e.getKeyCode() == KeyEvent.VK_UP) {//flecha arriba rota la pieza
	           currentShape.rotateShape();
	        }
	       if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// flecha a la derecha mueve en el eje x a la derecha
	           currentShape.setDeltaX(1);
	        }
	        if (e.getKeyCode() == KeyEvent.VK_LEFT) {//fecha a la izquierda mueve la pieza en el eje x a la izquierda
	            currentShape.setDeltaX(-1);
	        }
	        if (e.getKeyCode() == KeyEvent.VK_DOWN) {//fecha hacia abajo aumenta la velocidad de caida
	            currentShape.speedUp();
	       }
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {// si se suelta la flecha hacia abajo la velocidad vuelve a ser normal
            currentShape.speedDown();
        }
		
	}



	public void startGame() {
		stopGame();//funcion de reset
	    setNextShape();//creamos proxima pieza
	    setCurrentShape();//la convertimos en la actual y creamos una proxima pieza
	    gameOver = false;// declaramos el juego no perdido
	    gameloop.start();//comenzamos el timer
		
	}
	
	private void stopGame() {
		score = 0;//marcador a cero

       for (int row = 0; row < board.length; row++) {
           for (int col = 0; col < board[row].length; col++) {
                board[row][col] = null;//vaciamos el grid
          }
      }
       gameloop.stop();//se para el temporizador
		
	}

	public class GameLooper implements ActionListener {
		@Override
	public void actionPerformed(ActionEvent e) {
			 update();
	         repaint();

		}
	
	}
	

	public Color[][] getBoard() {
		
		return board;
	}

	public void addScore() {
	//	score++;
		
	}
	public void setNextShape() {//generamos proxima pieza
        int index = random.nextInt(shapes.length);//de forma random
        int colorIndex = random.nextInt(colors.length);//color random
        nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);//instanciamos
    }

	public void setCurrentShape() {//pieza pasa de ser proxima a actual
		 currentShape = nextShape;
	       setNextShape();//llamamos a la creacion de una proxima pieza
           //dibujamos en el tablero la pieza actual
	       for (int row = 0; row < currentShape.getCoords().length; row++) {//si colision con la construccion
	           for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
	               if (currentShape.getCoords()[row][col] != 0) {
	                   if (board[currentShape.getY() + row][currentShape.getX() + col] != null) {
	                       gameOver = true;//perdemos
	                    }
	               }
	           }
	       }
	}

	
}
