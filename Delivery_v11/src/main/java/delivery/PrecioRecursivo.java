package delivery;

import java.util.List;
import java.util.concurrent.*;

// Esta clase se utiliza para realizar un cálculo de la suma de todos los precios de todos los productos de todos los pedidos, de forma fraccionada con un enfoque de Divide y Venceras.
// Para ello, la clase extiende RecusriveAction para luego ser utilizado con un ForkJoinPool.
// En cada iteración, se va dividiendo el problema en dos partes con tamaños identicos (o similares) y se pide resolverlo con invokeAll().
public class PrecioRecursivo extends RecursiveAction {
	private static final long serialVersionUID = 1L;
	List<Pedido> lp; // La lista a procesar
	List<Pedido> lm; // La lista de resultados
	Double precioCorte; // Precio de corte para la lista de resultados
	int pedidosCorte;
	
	public PrecioRecursivo(List<Pedido> _lp, List<Pedido> _lm, Double _precioCorte, int _pedidosCorte) {
		lp = _lp;
		lm = _lm;
		precioCorte = _precioCorte;
		pedidosCorte = _pedidosCorte;
	}
	
	public void compute() {
		if (lp.size() <= pedidosCorte) { // ¿Es caso trivial?
			// Si, resolver
			
			for (Pedido pdd:lp) { // Este bloque calcula la suma del precio de todos los productos de un pedido, de todos los pedidos de la lista, y luego los vuelve a sumar.
				Double precio = 0.0;
				for (Producto pdt : pdd.productos) {
					precio += pdt.precio;
				}

				// Si el precio total supera el de corte estipulado, añadir a la lista de resultados
				if (precio > precioCorte) {
					lm.add(pdd);
				}
			}			
			
		} else { // No, dividir
			List<Pedido> lista1 = lp.subList(0, lp.size() / 2);
			List<Pedido> lista2 = lp.subList((lp.size() / 2) + 1, lp.size());			
			PrecioRecursivo task1 = new PrecioRecursivo(lista1, lm, precioCorte, pedidosCorte);
			PrecioRecursivo task2 = new PrecioRecursivo(lista2, lm, precioCorte, pedidosCorte);
			if (lista2.isEmpty()) {
				invokeAll(task1);
			} else {
				invokeAll(task1, task2);
			}
		}
	}
	
}
