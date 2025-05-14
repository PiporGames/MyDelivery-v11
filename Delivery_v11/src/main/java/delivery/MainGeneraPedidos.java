package delivery;

import java.util.LinkedList;
import java.util.List;

public class MainGeneraPedidos {
	static int total = 0;

	public static void main(String[] args) {
		List<Pedido> lp;
		lp = new LinkedList<>();
		lp = Pedido.generaPedidos(10, Canal.Web);
		lp.addAll(Pedido.generaPedidos(10, Canal.CC));
		lp.addAll(Pedido.generaPedidos(10,Canal.Mobile));
		Pedido.pedidosAFichero(lp, "C:/users/fsfig/pedidos4.bin");
		List <Pedido> lp2 = Pedido.pedidosDesdeFichero("C:/users/fsfig/pedidos4.bin");
		for (Pedido p:lp2) {
			p.print();
			total+=p.getPrecioPedido();
		}
		System.out.println ("total: "+total);
	}

}
