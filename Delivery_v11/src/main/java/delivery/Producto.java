package delivery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producto implements Serializable {
	String idProducto;
	double precio;
	int cantidad;
	
	private static final long serialVersionUID = 1L;
	
	public Producto (String _idProducto, double _precio, int _cantidad) {
		idProducto = _idProducto;
		precio = _precio;
		cantidad = _cantidad;
	}
	
	public Producto (String _idProducto, int _cantidad) {
		idProducto = _idProducto;
		cantidad = _cantidad;
	}
	
	public double getPrecio () {
		return precio;
	}
	
	public int getCantidad () {
		return cantidad;
	}
	
	public String getId () {
		return idProducto;
	}
	
	public void print() {
		System.out.println ("Id: "+idProducto);
		System.out.println ("Cantidad:"+cantidad);
		System.out.println ("Precio: "+precio);
	}
	
	// GENERAR PRODUCTOS
	// Puedes generar tus productos como quieras, pero aquí tienes una forma de hacerlo
	// que te ayudará a ver si tu programa funciona bien o no.
	
	public static List<Producto> generaProductos (int numProductos) {
		List <Producto> l = new ArrayList<>(numProductos);
		Random r = new Random();
		int cantidad;
		String id;
		double precio;
		
		for (int i=0;i<numProductos;i++) {
			cantidad = 1+r.nextInt (Config.numeroProductos);
			id = ""+r.nextInt (Config.maximoIdProducto);
			// precio = Config.maximoPrecioProducto;              // con precio fijo. Mejor para trazas
			precio = 1+r.nextDouble(Config.maximoPrecioProducto); // con precio aleatorio hasta el máximo del precio configurado
			Producto p = new Producto (id, precio, cantidad);
			l.add(p);
		}
		return l;
	}
}
