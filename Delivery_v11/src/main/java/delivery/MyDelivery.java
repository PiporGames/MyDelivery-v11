package delivery;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pcd.util.ColoresConsola;
import pcd.util.Traza;

public class MyDelivery {
	
	// Suprimimos los errores del IDE relacionados con código en desuso
	@SuppressWarnings("unused")
	
	public MyDelivery () {
		// para facilitar las trazas
		Traza.setNivel(Config.modoTraza);
		Traza.traza(ColoresConsola.GREEN_BOLD_BRIGHT, 0, "    ...     ..      ..                         ....                           ..    .       _                                                    _                                 \r\n"
				+ "  x*8888x.:*8888: -\"888:     ..            .xH888888Hx.                 x .d88\"    @88>    u                                    ..              u                oe          oe    \r\n"
				+ " X   48888X `8888H  8888    @L           .H8888888888888:                5888R     %8P    88Nu.   u.                .u    .    @L              88Nu.   u.      .@88        .@88    \r\n"
				+ "X8x.  8888X  8888X  !888>  9888i   .dL   888*\"\"\"?\"\"*88888X        .u     '888R      .    '88888.o888c      .u     .d88B :@8c  9888i   .dL     '88888.o888c ==*88888    ==*88888    \r\n"
				+ "X8888 X8888  88888   \"*8%- `Y888k:*888. 'f     d8x.   ^%88k    ud8888.    888R    .@88u   ^8888  8888   ud8888.  =\"8888f8888r `Y888k:*888.     ^8888  8888    88888       88888    \r\n"
				+ "'*888!X8888> X8888  xH8>     888E  888I '>    <88888X   '?8  :888'8888.   888R   ''888E`   8888  8888 :888'8888.   4888>'88\"    888E  888I      8888  8888    88888       88888    \r\n"
				+ "  `?8 `8888  X888X X888>     888E  888I  `:..:`888888>    8> d888 '88%\"   888R     888E    8888  8888 d888 '88%\"   4888> '      888E  888I      8888  8888    88888       88888    \r\n"
				+ "  -^  '888\"  X888  8888>     888E  888I         `\"*88     X  8888.+\"      888R     888E    8888  8888 8888.+\"      4888>        888E  888I      8888  8888    88888       88888    \r\n"
				+ "   dx '88~x. !88~  8888>     888E  888I    .xHHhx..\"      !  8888L        888R     888E   .8888b.888P 8888L       .d888L .+     888E  888I     .8888b.888P    88888       88888    \r\n"
				+ " .8888Xf.888x:!    X888X.:  x888N><888'   X88888888hx. ..!   '8888c. .+  .888B .   888&    ^Y8888*\"\"  '8888c. .+  ^\"8888*\"     x888N><888'      ^Y8888*\"\"     88888       88888    \r\n"
				+ ":\"\"888\":~\"888\"     `888*\"    \"88\"  888   !   \"*888888888\"     \"88888%    ^*888%    R888\"     `Y\"       \"88888%       \"Y\"        \"88\"  888         `Y\"         88888       88888    \r\n"
				+ "    \"~'    \"~        \"\"            88F          ^\"***\"`         \"YP'       \"%       \"\"                   \"YP'                         88F                  '**%%%%%%** '**%%%%%%** \r\n"
				+ "                                  98\"                                                                                                98\"                                           \r\n"
				+ "                                ./\"                                                                                                ./\"                                             \r\n"
				+ "                               ~`                                                                                                 ~`                                               ");
		Traza.traza(ColoresConsola.PURPLE_BACKGROUND, 0, "Por José Manuel de Torres Domínguez\n\n");
		
		Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "CARGANDO RESTAURANTES...");
		// Creando los restaurantes
		CadenaRestaurantes cadenaRestaurantes = null;
		cadenaRestaurantes = new CadenaRestaurantes (Config.numeroRestaurantes);
		cadenaRestaurantes.crearRestaurantes();

		// CARGAR PEDIDOS DE FICHERO
		// Aquí hacemos una distinción.
		// 		Los lanzamientos tipo 0, 1 y 2 se ejecutarán con todos las auditurias y extras
		// 		Los lanzamientos tipo 3 (Observables) no ejecutarán las auditorias y extras, ya que no se hace uso de la lista LP mencionada a continuación.
		//
		// Para probar todas las auditorias y extras, por favor, utiliza un tipo de lanzamiento que no sea el tipo 3.
		
		// Obtenemos una lista de pedidos (para los tipos 1, 2 y 3)
		List<Pedido> lp;
		lp = new LinkedList<>();
		
		if (Config.tipoLanzamiento == 3) {
			Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "LANZAMIENTO CON OBSERVERS...");
			
			// Obtenemos el Observable con los pedidos
			Observable<Pedido> obs = Pedido.pedidosDesdeFicheroObservable("C:\\Users\\pipor\\OneDrive - Universidad de Extremadura\\Clases\\Programación Concurrente y Dirigida\\PROYECTO/pedidos7.bin");
			
			// Definimos y cargamos la lista de restaurantes 
			long initialTime = new Date().getTime();
			LinkedList<Restaurante> listaRestaurantes = cadenaRestaurantes.getRestaurantes();
			
			// Lanzamos cada pedido dentro de un Thread (Schedulers.computation()) con la ayuda de un flatmap.
			obs.flatMap( i -> Observable.just(i).subscribeOn(Schedulers.newThread()).doOnNext( p -> listaRestaurantes.get(p.getRestaurante()).tramitarPedido(p))).subscribe();
			
			Traza.traza(ColoresConsola.RED_UNDERLINED, 0, "Las auditorías y demás extras no se ejecutarán con el Lanzamiento con Observers");
			
			Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "PROGRAMA FINALIZADO");
			
		} else {
			// Cargamos la lista de pedidos
			lp = Pedido.pedidosDesdeFichero ("C:\\Users\\pipor\\OneDrive - Universidad de Extremadura\\Clases\\Programación Concurrente y Dirigida\\PROYECTO/pedidos7.bin"); // Pon aqu� la ruta y nombre de tu fichero 
			
			// También puedes crear tus propios pedidos usando el método generaPedidos de la clase Pedido. 
			// En la clase Pedido también tienes un método para volcar esos pedidos a un fichero.
			
			
			// LANZAR PEDIDOS
			
			// Definimos y cargamos la lista de restaurantes y la lista de TramitarPedido (Threads).
			long initialTime = new Date().getTime();
			LinkedList<Restaurante> listaRestaurantes = cadenaRestaurantes.getRestaurantes();
			List<TramitarPedido> pedidosTramitados = new LinkedList<TramitarPedido>();
			
			if (Config.tipoLanzamiento == 0) {
				// Lanzamiento secuencial desde el main
				Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "LANZAMIENTO SECUENCIAL CON THREADS...");
				
				for (Pedido p:lp) {
					TramitarPedido tp = new TramitarPedido(listaRestaurantes.get(p.getRestaurante()), p);
					tp.start();
					pedidosTramitados.add(tp);
				}
			} else if (Config.tipoLanzamiento == 1) {
				// Lanzamiento por Executor
				Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "LANZAMIENTO CON EXECUTOR...");
				
				ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
				for (Pedido p:lp) {
					TramitarPedido tp = new TramitarPedido(listaRestaurantes.get(p.getRestaurante()), p);
					executor.execute(tp);
					pedidosTramitados.add(tp);
				}
			} else {
				// Lanzamiento por Stream paralelo
				Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "LANZAMIENTO CON STREAMS...");
				
				lp.stream().parallel().forEach(p->listaRestaurantes.get(p.getRestaurante()).tramitarPedido(p));
			}
			
			// ESPERAR PEDIDOS
			Traza.traza(ColoresConsola.BLUE_BOLD_BRIGHT, 0, "ESPERANDO FINALIZACIÓN DE LA SIMULACIÓN...");
			for (TramitarPedido tp:pedidosTramitados) {
				try {
					tp.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Traza.traza(ColoresConsola.GREEN_BOLD_BRIGHT, 0, "FINALIZADO\n\n");
			
			
			// AUDITORÍAS
			Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "AUDITORÍAS");
			
			listaRestaurantes.stream().parallel().forEach( r-> System.out.println ("Auditoría Restaurante "+r.getNombre()+" "+r.getBalance()));
			
			System.out.println ("\nAuditoría Cadena: "+ cadenaRestaurantes.getBank().audit(0, Config.numeroRestaurantes));
			
			System.out.println ("Tiempo total invertido en la tramitación: "+(new Date().getTime() - initialTime));
			
			// EXTRAS
			Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "\n\nEXTRAS");
			
			// OBTENER EL PEDIDO MÁS CARO
			// En principio no utilizamos Future<T> pues este se utiliza con los Executors, que no usamos aquí.
			CallablePedido cp = new CallablePedido(lp);
			Double res = -1.0;
			try {
				res = cp.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nPedido más caro: ");
			Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, res.toString());
		
			
			// OBTENER LISTA DE PEDIDOS POR ENCIMA DE 12EUR
			Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nLista de Pedidos > 12 EUR:");
			List<Pedido> lpCopy = new LinkedList<Pedido>();
			List<Pedido> lmayor = new LinkedList<Pedido>();
			lpCopy.addAll(lp);
			PrecioRecursivo pc = new PrecioRecursivo(lp, lmayor, 12.0, 10);
			
			ForkJoinPool pool = new ForkJoinPool();
			pool.execute(pc);
			pool.shutdown();
			try {
				pool.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Pedido p : lmayor) Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, p.getId());		
			
			
			// OBTENER LISTA PRECIOS MENOR DE 7eur (Stream)
			Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nLista de pedidos con precio menor a 7 euros (Stream):");
			lp.stream().parallel().filter(p->p.getPrecioPedido() < 7).forEach(p-> Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, p.getId() + " " + p.getPrecioPedido()));
			
			// OBTENER PEDIDO MÁS CARO (Stream)
			Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nPrecio del pedido más caro (Stream):");
			//		lp.stream()
			//			.map(p-> p.getPrecioPedido())
			//			.sorted((a,b)-> { 
			//				if (a < b) {
			//					return -1;
			//				} else if (a > b) {
			//					return 1;
			//				} else { return 0; }} )
			//			.forEach(p-> System.out.println(p));
			lp.stream().parallel().map(p-> p.getPrecioPedido()).reduce((a,b) -> a > b ? a : b).ifPresent(p-> Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, p.toString()));
			
			// ENCONTRAR DIRECCIÓN CONCRETA (Stream)
			if (lp.stream().parallel().anyMatch(p-> p.getDireccion().equals("Avenida de la Universidad"))) { Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, "\nEncontrado"); }
			if (lp.stream().parallel().anyMatch(p-> p.getDireccion().equals("Berna, 11"))) { Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, "\nEncontrado"); }
		
			// Cargar observable a partir de una lista.
			Observable<Pedido> source = Observable.fromIterable(lp);
			
			// SUMA DEL PRECIO DE TODOS LOS PEDIDOS (Observable)
			// Definimos el primer observador según las exigencias de la práctica
			Observer<Pedido> ob1 = new Observer<Pedido>() {
				private double sum;
	
				@Override
				public void onComplete() {
					Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, Thread.currentThread() + " -> Suma del precio de todos los pedidos: " + sum + " EUR");
				}
	
				@Override
				public void onError(@NonNull Throwable arg0) {
					arg0.printStackTrace();
					
				}
	
				@Override
				public void onNext(@NonNull Pedido arg0) {
					double val = arg0.getPrecioPedido();
					sum += val;
					System.out.println(Thread.currentThread() + " -> Se ha observado el pedido " + arg0.getId() + " con valor " + val + " (total: " + sum + ")");
				}
	
				@Override
				public void onSubscribe(@NonNull Disposable arg0) {
					sum = 0;
					Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nSuma del precio de todos los pedidos (Observable):");
				}
			};
			source.subscribeOn(Schedulers.computation()).subscribe(ob1);
			
			
			// LISTA DE PEDIDOS CON PRECIO MAYOR A 12 (Observable)
			// Definimos el segundo observable.
			System.out.println();
			Observer<Pedido> ob2 = new Observer<Pedido>() {
	
				@Override
				public void onComplete() {}
	
				@Override
				public void onError(@NonNull Throwable arg0) {
					arg0.printStackTrace();			
				}
	
				@Override
				public void onNext(@NonNull Pedido arg0) {
					if (arg0.getPrecioPedido() > 12) {
						System.out.print(Thread.currentThread() + " -> ");
						Traza.traza(ColoresConsola.GREEN_BACKGROUND, 0, arg0.getId() + " es un pedido con precio > 12 EUR");
					}
				}
	
				@Override
				public void onSubscribe(@NonNull Disposable arg0) {
					Traza.traza(ColoresConsola.PURPLE_BOLD, 0, "\nSLista de pedidos con precio > 12 EUR (Observable):");
				}
			};
			source.subscribeOn(Schedulers.computation()).subscribe(ob2);
		}
	}
	public static void main(String[] args) {
		new MyDelivery();
		Traza.traza(ColoresConsola.YELLOW_BOLD, 0, "PROGRAMA PRINCIPAL (MAIN) FINALIZADO");
		return;
	}
}
