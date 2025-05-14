package delivery;
import java.util.concurrent.locks.*;

import pcd.util.Ventana;

// Clase genérica que permite almacenar un tipo de elemento.
// Utilizamos Locks para sincronizar el poner y coger objetos del robot y la cocina, respectivamente.
public class Contenedor {
	protected static int posicionVentana = 0;
	private Lock monitor = new ReentrantLock(); // monitor (lock)
	private Condition lleno = monitor.newCondition(); // condición de si está lleno
	private Condition vacio = monitor.newCondition(); // condición de si está vacio
	private int max = 0; // máximos items
	private int items = 0; // items actualmente en el contenedor
	protected String id;
	protected Ventana v;
	
	public Contenedor(String _id, int _max, int _vHeight, int _vWidth) {
		id = _id;
		max = _max;
		v =  new Ventana ("Contenedor de Rest." + id, _vWidth + posicionVentana, _vHeight);
		v.setVisible(false); //Desactivamos visibilidad de la ventana (ya no la necesitamos más)
	}
	
	public void poner() {
		monitor.lock(); // conseguimos el bloqueo
		try {
			while (items == max) { lleno.await(); } // dormimos el hilo mientras esté lleno
			items++; // si salimos del sueño, sumamos un item más
			v.addText("Robot asociado pone item (quedan ahora " + items + ")");
			vacio.signal(); // indicamos a los que están bloqueados por vacío que ahora hay (por lo menos) un item
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			monitor.unlock(); // dejamos el bloqueo
		}
	}
	
	public void coger() {
		monitor.lock(); // conseguimos el bloqueo
		try {
			while (items == 0) { vacio.await(); } // Si no hay items, nos dormimos
			items--; // Si somos despertados, cogemos un item (si nos han despertado, sabemos que es por un buen motivo --> Alguien debe de haber puesto un item)
			v.addText("Cocina asociada quita item (quedan ahora " + items + ")");
			lleno.signal(); // Avisamos a los que están bloqueados en lleno que ahora hay un hueco libre para poner algo
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			monitor.unlock(); // dejamos el bloqueo
		}
	}

}
