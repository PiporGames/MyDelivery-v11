package delivery;
public class Config {
	public static int posicionVentanaW = 0;		// Guarda el valor del eje X de la última ventana colocada
	public static int posicionVentanaH = 0;		// Guarda el valor del eje Y de la última ventana colacada
	
	public final static int modoTraza = 2;				// nivel de profundidad de la traza
	public final static int numeroRestaurantes = 7;		// n�mero de restaurantes a crear
	public final static int numeroPedidos = 5;			// n�mero de pedidos por canal a crear
	public final static int numeroMoteros = 2;			// n�mero de moteros por restaurante a crear
	public final static int numeroProductos = 3; 		// l�mite de cantidad de productos a crear en pedido
	public final static int maximoIdProducto = 5; 		// n�mero m�ximo de id de producto
	public final static int maximoPrecioProducto = 5;	// precio m�ximo de cada producto. 
	public final static int tipoLanzamiento = 3; 		// 0-> Lanzamiento de pedidos con Threads, 1-> Lanzamiento de pedidos con el Executor, 2-> Lanzamiento de pedidos con Stream paralelo, 3-> Lanzamiento de pedidos con Observable
	public final static int tipoSyncMoteros = 1; 		// 0-> Los moteros se sincronizan con un Semaphore, 1-> Los moteros se sincronizan con un CyclicBarrier

	synchronized public static void confirmarEspacioNuevaVentana() {
		if (Config.posicionVentanaW < 1650) {
			Config.posicionVentanaW+=275;
		} else {
			Config.posicionVentanaH+=275;
			Config.posicionVentanaW = 0;
		}
	}

}
