package delivery;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.core.Observable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	String idPedido;
	String direccion;			// Direcci�n del cliente
	int restaurante;			// Restaurante al que va el pedido
	Canal canal;				// Canal por el que viene el pedido (web, m�vil, etc)
	LocalDate fecha;			// Fecha del pedido
	LocalTime hora;				// Hora del pedido
	List<Producto> productos;	// Productos que conforman este pedido

	public Pedido (String _idPedido, String _direccion, int _restaurante, Canal _canal, LocalDate _fecha, LocalTime _hora, List<Producto> _productos) {
		idPedido = _idPedido;
		direccion = _direccion;
		restaurante = _restaurante;
		canal =_canal;
		fecha =_fecha;
		hora = _hora;
		productos = _productos;
	}
	
	public String getId() {
		return idPedido;
	}

	public int getRestaurante () {
		return restaurante;
	}
	
	public String getDireccion () {
		return direccion;
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	
	public Canal getCanal() {
		return canal;
	}
	
	public LocalDate getFecha () {
		return fecha;
	}
	
	public LocalTime getHora () {
		return hora;
	}
	
	public double getPrecioPedido () {
		double suma=0;
		
		List<Producto> productos = this.getProductos();
		for (Producto prod:productos) {
			suma+=prod.getPrecio();
		}
		return suma;
	}
	
	public void print () {
		System.out.print ("Pedido:    ");
		System.out.println ("Id: "+idPedido+" "+direccion+" "+restaurante+" "+canal+"   "+getPrecioPedido());
		//productos.stream().forEach (p->p.print()); // imprime cada producto
	}	
	
	public String printConRetorno () {
		return ("Id: "+idPedido+" "+direccion+" "+restaurante+" "+canal+"   "+getPrecioPedido());
	}
	
	// GENERAR PEDIDOS
	// Genera un n�mero determinado de pedidos (numPedidos) por el canal especificado (_canal)
	public static List<Pedido> generaPedidos (int _numPedidos, Canal _canal) {
		List<Pedido> listaPedidos = new ArrayList<>(_numPedidos);
		String id;
		Canal canal;
		LocalDate d;
		LocalTime t;
		String direccion;
		int restaurante;
		Random r = new Random();
		
		for (int i=0;i<_numPedidos;i++) {
			canal = _canal;
			direccion = generarDireccion();
			restaurante = r.nextInt(Config.numeroRestaurantes);
			id = canal+"_"+"R"+restaurante+"_"+Ticket.getNumero();
			d = LocalDate.now();
			t = LocalTime.now();
			Pedido p = new Pedido (id, direccion, restaurante, canal, d, t, Producto.generaProductos(Config.numeroProductos));
			listaPedidos.add(p);
		}
		return listaPedidos;
	}
	

	
	public static String generarDireccion () {
		// Genera una direcci�n postal aleatoria eligiendo una calle y n�mero de entre un conjunto de calles y n�meros
		List <String> listaDirecciones = Arrays.asList("Santa Joaquina de Vedruna", "Alfonso IX", "Ruta de la Plata", "Doctor Mara��n", "Rod�guez Mo�ino", 
				"Hornillos", "Berna", "Par�s", "Pizarro", "Dalia", "Maluquer", "Le�n Leal", "Gallegos", "Hornos", "Ceres", "Viena", "Piscis", "Sierpes", "Amor de Dios", "Camino Llano");
		List <Integer> listaNumeros  = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Random r = new Random();
		int i = r.nextInt (0, 20);
		int j = r.nextInt (0,20);
		return (listaDirecciones.get(i)+", "+listaNumeros.get(j));
	}

	// ESCRITURA Y LECTURA DE FICHERO
	public static void pedidosAFichero (List<Pedido> l, String _fichero) {
		// Recibimos una lista de pedidos
		FileOutputStream fout;
		try {
			fout = new FileOutputStream (_fichero); // Pon aqu� la ruta y nombre de tu fichero
			ObjectOutputStream o = new ObjectOutputStream (fout);
			l.forEach(p-> {
				try {
					o.writeObject (p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});	
			o.writeObject (null); // Act�a de centinela para indicar que no hay m�s
			o.close();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Pedido> pedidosDesdeFichero (String _fichero) {
		List<Pedido> l = new ArrayList<>(); // La lista de pedidos que vamos a retornar
		Pedido pedido;
		
		try {
			FileInputStream fin = new FileInputStream (_fichero); 
			ObjectInputStream o = new ObjectInputStream (fin);
			pedido = (Pedido) o.readObject();
			while (pedido!=null) {
				l.add(pedido);
				pedido = (Pedido) o.readObject();
			}
			o.close();
			fin.close();
		} catch (Exception e) {e.printStackTrace();}
		return l;
	}
	
	public static Observable<Pedido> pedidosDesdeFicheroObservable (String _fichero) {
		Observable<Pedido> obs = Observable.create(emitter -> {
			try {
				Pedido pedido;
				FileInputStream fin = new FileInputStream (_fichero); 
				ObjectInputStream o = new ObjectInputStream (fin);
				pedido = (Pedido) o.readObject();
				while (pedido!=null) {
					emitter.onNext(pedido);
					pedido = (Pedido) o.readObject();
				}
				o.close();
				fin.close();
			} catch (Exception e) {
				emitter.onError(e);
			}
			emitter.onComplete();
		});
		
		return obs;
	}
	
}