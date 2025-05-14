package delivery;

import java.util.concurrent.*;

// Sincroniza todos los moteros siguiendo la misma regla (esperar unos a otros) pero con un semaforo y un barrier.
public class TodosMoteros {
	private int moteros;
	private int listos = 0;
	private Semaphore mutex;
	private CyclicBarrier barrier;
	
	public TodosMoteros(int _cuantos) {
		moteros = _cuantos;
		mutex = new Semaphore(1); // semáforo de un solo motero
		barrier = new CyclicBarrier(moteros); // barrera en el que entran hasta "moteros" moteros
	}
	
    synchronized public void estamosTodosListosSemaphore() {
		try {
			mutex.acquire(); //adquirimos un ticket del semáforo, si no hay, esperamos
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	listos++; // un motero más ahora está listo
		mutex.release(); //dejamos el semáforo
		if (listos == moteros) { // si hay tantos moteros listos
			notifyAll(); // notificamos a todos los moteros
		} else {
			try {
				wait(); // de lo contrario, me duermo (a la espera de que todos lleguemos)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    
    public void estamosTodosListosBarrier() {
    	try {
			barrier.await(); // motero que llega, motero que se bloquea. Cuando seamos todos los moteros, romperemos la barrera y nos desbloquearemos todos.
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
    }
}
