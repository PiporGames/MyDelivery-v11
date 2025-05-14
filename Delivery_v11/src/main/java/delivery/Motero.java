package delivery;
import pcd.util.Ventana;

public class Motero extends Thread{
	private Ventana v;
	private ControlMoteros cm; // Un motero pertenece a un control motero
	private TodosMoteros s; // Un motero debe esperar a los demás moteros
	
	public Motero(ControlMoteros _cm, TodosMoteros _s) {
		cm = _cm;
		s = _s;
		v =  new Ventana ("Motero de Rest."+ cm.getRestaurante().getNombre(), Config.posicionVentanaW, Config.posicionVentanaH);
		Config.confirmarEspacioNuevaVentana();	
	}
	
	
	public void run() {
		//Esperamos a los otros moteros
		if (Config.tipoSyncMoteros == 0) {
			s.estamosTodosListosSemaphore();
		} else {
			s.estamosTodosListosBarrier();
		}
		
		// Realizamos (infinitamente) la acción de procesar las comandas del restaurante.
		while (true) {
			Pedido p = cm.getPedido(); // coger un pedido
		    while (p == null) // es ese pedido nulo? o lo mismo, no hemos cogido un pedido por que no hay?
		        try { //si...
		          wait(); // dormimos el hilo
		          p = cm.getPedido(); // si alguien nos despierta, es por un buen motivo --> Debe de haber un nuevo pedido
		        } 
		    catch (InterruptedException e){;}
	
			v.addText("REPARTIENDO PEDIDO : "+ p.getId());
			try {
				Thread.sleep(500); // Simulamos que tarda 0,5 segundos en repartir
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			
			cm.regresaMotero();
		}
	}
}
