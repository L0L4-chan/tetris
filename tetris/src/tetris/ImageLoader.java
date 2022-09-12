package tetris;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
// creamos clase para la carga de imagen, de forma que el try catch se programa una vez
public class ImageLoader {
	
// funcion para cargar la imagen cdesde el archivo
	// @param string localizacion de la imagen en el proyecto
	public static BufferedImage loadImage(String ruta) {
		 try {// intentamos cargar la imagen si existe y la devolvemos
	            return ImageIO.read(new File( ruta));// funcion para leer el archivo imagen

	        } catch (IOException e) {// si no existe
	            e.printStackTrace();// informacion de la excepcion
	            System.exit(1);//salimos 
	        }
	        return null;// y no se devolveria nada
	}

}
