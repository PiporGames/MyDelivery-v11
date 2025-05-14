package pcd.util;

public class MainPrueba {

	public static void main(String[] args) {
		// Esta clase muestra algunos ejemplos de uso de la clase 
		Traza.setNivel (2); // Establecemos el nivel de traza en 2. Mensajes con número de nivel >2 no se verán en pantalla.
		Traza.traza(ColoresConsola.RED, 1, "Este mensaje es de nivel 1. Se ve en rojo. ");
		Traza.traza(ColoresConsola.RED, 3, "Este mensaje no se verá.");
		Traza.traza(ColoresConsola.GREEN_BACKGROUND, 2, "Este mensaje es de nivel 2. Se ve en con fondo verde.");
		Traza.traza(1, "Este mensaje es de nivel 1. Se ve normal.");
		Traza.traza(ColoresConsola.BLACK_UNDERLINED, 1, "Este mensaje se ve subrayado.");
		
		Ventana v = new Ventana ("Título de la ventana", 100,200);
		v.addText("Este texto se ve dentro de una ventana situada en las coordenadas 100,200");
	}
}
