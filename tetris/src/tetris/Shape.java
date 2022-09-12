package tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Shape {
	// variables 
	 private Color color;//color
     private int x, y;//coordenadas
     private long time, lastTime; // para guardar record del momento
	 private int normal = 600, fast = 50;// velocidades
     private int delay;// variable que marcara cada cuanto se refresca	   
     private int[][] coords;//matriz de coordenadas
	 private int[][] reference;// matriz de referencias
     private int deltaX;// entero para un valor de X
     private Board board;//panel
     private boolean collision = false, moveX = false; //boleanos de choque o de en movimiento en plano X
     private int timePassedFromCollision = -1; //

	public Shape(int[][] coords, Board board, Color color) {
		this.coords = coords;//coordenadas de la figura 
        this.board = board;//panel de la figura
        this.color = color;//color de la figura
        deltaX = 0;//posicion de deltaX
        x = 4;// posicion de inicio
        y = 0;//posicion de inicio
        delay = normal;//retraso para el refresque, o para la velocidad de movimiento
        time = 0;//inicializamos
        lastTime = System.currentTimeMillis();//momento en que se crea registro de tiempo
        reference = new int[coords.length][coords[0].length];//posicion actual

        System.arraycopy(coords, 0, reference, 0, coords.length);//
	}
	long deltaTime;//auxiliar de tiempo
	
	public void update() {
		 long deltaTime;//auxiliar de tiempo
		
		 moveX = true;//en movimiento 
	        deltaTime = System.currentTimeMillis() - lastTime;//actualizamos
	        time += deltaTime;//tiempo aumenta
	        lastTime = System.currentTimeMillis();//nuevo momento de creacion de la actualizacion

	        if (collision && timePassedFromCollision > 600) {//si nos hemos chocado 
	            for (int row = 0; row < coords.length; row++) {
	                for (int col = 0; col < coords[0].length; col++) {//se dibuja la figura
	                    if (coords[row][col] != 0) {
	                        board.getBoard()[y + row][x + col] = color;
	                    }
	                }
	            }
	            checkLine();//comprobamos 
	            board.addScore();//sumamos puntos
	            board.setCurrentShape();//actualizamos como figura en el tablero
	            timePassedFromCollision = -1;//reseteamos el contador
	        }

	        // check moving horizontal
	        if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) { //si estamos en los limites

	            for (int row = 0; row < coords.length; row++) {
	                for (int col = 0; col < coords[row].length; col++) {
	                    if (coords[row][col] != 0) {
	                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
	                            moveX = false; //no hay movimiento en el eje x
	                        }

	                    }
	                }
	            }

	            if (moveX) {//de lo contrario
	                x += deltaX;//nos movemos tanto como se ha designado
	            }

	        }

	        // Check position + height(number of row) of shape
	        if (timePassedFromCollision == -1) {//
	            if (!(y + 1 + coords.length > 20)) {

	                for (int row = 0; row < coords.length; row++) {
	                    for (int col = 0; col < coords[row].length; col++) {
	                        if (coords[row][col] != 0) {

	                            if (board.getBoard()[y + 1 + row][x + col] != null) {
	                                collision();
	                            }
	                        }
	                    }
	                }
	                if (time > delay) {
	                    y++;
	                    time = 0;
	                }
	            } else {
	                collision();
	            }
	        } else {
	            timePassedFromCollision += deltaTime;
	        }

	        deltaX = 0;
	    }
		
	private void collision() {
		collision = true;//si hemos chocado
        timePassedFromCollision = 0;//marcamos el tiempo como 0
	}
	
	


	private void checkLine() {//comprobamos limites del tablerr
		int size = board.getBoard().length - 1;//establecemos un valor para el tamaño

        for (int i = board.getBoard().length - 1; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] != null) {
                    count++;
                }

                board.getBoard()[size][j] = board.getBoard()[i][j];
            }
            if (count < board.getBoard()[0].length) {
                size--;
            }
        }
		
	}

	 public void render(Graphics g) {//creamos la figura

	        g.setColor(color);//damos color
	        for (int row = 0; row < coords.length; row++) {// rellenamos la figura
	            for (int col = 0; col < coords[0].length; col++) {
	                if (coords[row][col] != 0) {
	                    g.fillRect(col * 30 + x * 30, row * 30 + y * 30, Board.TILE_SIZE, Board.TILE_SIZE);
	                }
	            }
	        }

	    }
	 
	 public void rotateShape() {

	        int[][] rotatedShape = null;//creamos matriz vacia

	        rotatedShape = transposeMatrix(coords);//giramos nuestra matriz coordenadas 90 grados

	        rotatedShape = reverseRows(rotatedShape);

	        if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
	            return;
	        }

	        for (int row = 0; row < rotatedShape.length; row++) {
	            for (int col = 0; col < rotatedShape[row].length; col++) {
	                if (rotatedShape[row][col] != 0) {
	                    if (board.getBoard()[y + row][x + col] != null) {
	                        return;
	                    }
	                }
	            }
	        }
	        coords = rotatedShape;//actualizamos las posicion de la fuigura
	    } 

	private int[][] reverseRows(int[][] matrix) {
		int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];

            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }

        return matrix;
	}


	private int[][] transposeMatrix(int[][] matrix) {
		int[][] temp = new int[matrix[0].length][matrix.length];//creamos una matriz del tamaño de la pieza tumbada
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];//copiamos
            }
        }
        return temp;//devolvemos nueva matriz de la figura
	}
	// cambios de velocidad de movimiento es el refresh
	 public void speedUp() {//aumentamos
	        delay = fast;
	    }

	    public void speedDown() {//disminuimos
	        delay = normal;
	    }

//getters
	
	public Color getColor() {//devuelve el color
		
		return color;
	}

	public int[][] getCoords() {//devuelve la matriz posicion
		
		return coords;
	}
	
	public int getX() {//devuelve la x
        return x;
    }

    public int getY() {//devuelve la y
        return y;
    }
	
	//seters
	
	public void setDeltaX(int deltaX) {
	        this.deltaX = deltaX;
	    }

}
