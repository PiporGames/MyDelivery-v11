package delivery;
import java.util.*;

public class ControlMoteros {
	private int numeroMoteros;
	private Restaurante r;
	private List<Pedido> lp;
	private List<Thread> lm;
	private TodosMoteros s;
	
	public ControlMoteros (Restaurante _r, int _numeroMoteros) {
		numeroMoteros = _numeroMoteros;
		s = new TodosMoteros(numeroMoteros);
		r = _r;
		lp = new LinkedList<>();
		lm = new LinkedList<>();
		for (int i = 0; i < Config.numeroMoteros; i++) {
			Thread t = new Motero(this, s);
			t.start();
			lm.add(t);
		}
	}

	synchronized public Pedido getPedido() {
		while(lp.isEmpty()) { // si no hay pedido
			try {
				wait(); // dormimos el hilo, hasta que haya uno
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		numeroMoteros--; // entonces asignaremos a un motero, lo que decrementa esta variable
		Pedido p = lp.get(0); //quitamos el primer pedido de la lista (que vamos a procesar)
		lp.remove(0);
		return p; //devolvemos el pedido para que sea procesado
	}
	
	synchronized public void regresaMotero() {
		numeroMoteros++; // un motero regresa, sumamos a la variable
		if (numeroMoteros == 1) { // en el momento en el que haya un motero...
			notify(); // notificamos a los dormidos en moterosLibres() de que ya hay uno
		}
	}
	
	synchronized public void enviarPedido(Pedido p) {
		lp.add(p);
		notify();
	}
	
	synchronized public void moterosLibres() {
		while (numeroMoteros == 0) { // Si no hay moteros libres...
			try {
				wait(); // dormimos el hilo, hasta que nos despierten (aparezcan nuevos moteros)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Restaurante getRestaurante() {
		return r;
	}
	
}
