package delivery;

import java.util.List;
import java.util.concurrent.Callable;

// Esta clase nos será útil para encontrar de forma asíncrona el pedido con el mayor precio de todos.
public class CallablePedido implements Callable<Double> {
	private List<Pedido> lp; // Lista de pedidos a procesar
	
	public CallablePedido(List<Pedido> _lp) {
		lp = _lp;
	}
	
	@Override
	public Double call() throws Exception {
		// Este bloque calcula la suma de los precios de todos los productos de un pedido.
		// Luego, si el total es mejor que el precio más alto, se sobreescribe. Así solo quedará el pedido con el precio total más alto.
		Double best = 0.0;
		for (int i = 0; i < lp.size(); i++) {
			Double total = 0.0;
			for(int j = 0; j < lp.get(i).productos.size(); j++) {
				total += lp.get(i).productos.get(j).precio;
			}
			
			if (total > best) { best = total; }
		}
		
		// Devuelve el mejor
		return best;
	}

}
