package delivery;
import grpc.Stock.RestoProductoReply;
import grpc.Stock.VentaRequest;
import grpc.StockServiceGrpc;
import grpc.StockServiceGrpc.StockServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pcd.util.ColoresConsola;
import pcd.util.Traza;
import pcd.util.Ventana;

public class Cocina {
	// Definimos los miembros de la cocina.
	// En concreto, cada cocina tendrá 1 restaurante, 3 contenedores y 3 robots.
	private Ventana v;
	private Restaurante r;
	private Contenedor cpollo;
	private Contenedor cpan;
	private Contenedor clech;
	private Robot rpan;
	private Robot rpollo;
	private Robot rlech;
	//Grpc
	ManagedChannel channel; // Define un canal de comunicación
	String ip = "localhost";
	int port = 50000;
	
	public Cocina (Restaurante _r) {
		// Creamos los contenedores y los asociamos a los robots que también definiremos.
		r=_r;
		cpan = new Contenedor(r.getNombre() + " (Pan)", 3, 200, 10);
		cpollo = new Contenedor(r.getNombre() + " (Pollo)", 1, 400, 10);
		clech = new ContenedorLechuga(r.getNombre() + " (Lechuga)", 2, 600, 10);
		rpan = new Robot(cpan);
		rpollo = new Robot(cpollo);
		rlech = new Robot(clech);
		rpan.start();
		rpollo.start();
		rlech.start();
		v =  new Ventana ("Cocina." + r.getNombre(), Config.posicionVentanaW, Config.posicionVentanaH);
		Config.confirmarEspacioNuevaVentana();
		
		// Creamos un nuevo canal de texto plano conectado a la IP y puerto especificados.
		channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
		
	}
	
	public void cocinar (Pedido p) {
		// Si el pedido contiene el id "_0", entonces cocinaremos un pollo y un pan.
		if (p.idPedido.contains("_0")) {
			cpollo.coger();
			cpan.coger();
			clech.coger();
		}
		
		// Por cada producto en el pedido.
		// Podemos intentar añadir .parallel()
		p.getProductos().stream().parallel().forEach( c -> {
			// Creación de un stub bloqueante, adecuado para llamadas unarias o de streaming del servidor.
			StockServiceBlockingStub stub = StockServiceGrpc.newBlockingStub(channel);

			// Creación de la solicitud y envío del producto a registrar y su cantidad.
			VentaRequest solicitud = VentaRequest.newBuilder().setIdProducto(c.getId()).setCantidad(c.getCantidad()).build();
			synchronized (v) {v.addText("[Cocina] >>> Enviado id:" + c.getId() + " con u:" + c.getCantidad() + " unidades");}

			// Llamada al método unario del servidor y recepción de la respuesta.
			RestoProductoReply respuesta = stub.registrarVenta(solicitud);

			// Mostrar la respuesta del servidor en la interfaz de usuario.
			synchronized (v) {v.addText("[Cocina] <<< " + respuesta.getResto() + " restantes");
							  v.addText("\n");}
		});
		
		// Traza + pequeño retardo
		Traza.traza(ColoresConsola.GREEN, 2, "Cocinando el pedido: "+p.printConRetorno());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}