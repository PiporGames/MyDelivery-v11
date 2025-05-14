package delivery;

// La clase TramitarPedido exitende a Thread para que así se pueda ejecutar.
// Su objetivo es lanzar los pedidos dentro del Thread en el que ha sido creado.
public class TramitarPedido extends Thread {
	private Restaurante r_;
	private Pedido p_;
	
	public TramitarPedido(Restaurante r, Pedido p) {
		r_ = r; // resturante
		p_ = p; // pedido
	}
	
	public void run() {
		r_.tramitarPedido(p_); //dado el restaurante, tramitar un pedido en el
	}

}
