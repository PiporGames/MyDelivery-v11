package client;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import grpc.Stock.DameStockRequest;
import grpc.Stock.VentaRequest;
import grpc.StockServiceGrpc;
import grpc.StockServiceGrpc.StockServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import pcd.util.Ventana;

// Dame sotck cliente utiliza una conexión tipo BIDIRECCIONAL
// Solo se ejecuta una vez y pregunta por el stock de varios items dado su id, recibiendo varios objetos.
public class DameStockCliente {
	
	public static void main(String[] args) {
		Ventana v =  new Ventana ("Cliente · Dame Stock", 800, 800);
		
	    // Mostrar inicio de la operación de stream del servidor en la interfaz de usuario.
	    v.addText("##########################################################################");
	    v.addText("   DAME STOCK CLIENTE (BIDIRECCIONAL)");
	    v.addText("##########################################################################\n");
	    
	    // Definimos el canal y las propiedades de la conexión
		String ip = "localhost";
		int port = 50000;
		ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build(); // Define un canal de comunicación
	    
        // Preparar un CountDownLatch para esperar la respuesta del servidor.
        CountDownLatch latch = new CountDownLatch(1);
        
        // Creación de un stub no bloqueante, adecuado para streaming del cliente.
        StockServiceStub stub = StockServiceGrpc.newStub(channel); 
	    
        // Iniciar el stream del cliente y manejar las respuestas del servidor con un StreamObserver.
        StreamObserver<DameStockRequest> respuestaClientObserver = stub.dameStock(new StreamObserver<VentaRequest>() {
            @Override
            public void onNext(VentaRequest respuesta) {
                // Mostrar la respuesta del servidor en la interfaz de usuario.
            	synchronized(v) {v.addText("   [ Cliente <<< ]: Devuelto el producto con id:" + respuesta.getIdProducto() + " con cantidad: " + respuesta.getCantidad()); }
            }

            @Override
            public void onError(Throwable t) {
                // En caso de error, imprimir el stack trace y disminuir el latch.
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                // Al completarse la respuesta del servidor, mostrar el fin de la operación en la interfaz de usuario.
            	synchronized(v) {v.addText("   [ Cliente <<< ]: Fin de la comunicación por parte del servidor");}
                latch.countDown();
            }
        });

        // Enviar varios productos al servidor.
        // Espera un poco antes de enviar el siguiente mensaje para simular procesamiento
        List<String> ls = new LinkedList<>(); // TODO: INTRODUCIR LOS PRODUCTOS A CONOCER VALOR
        ls.add("1");
        ls.add("2");
        ls.add("3");
        ls.add("4");
        ls.add("5"); // Id erroneo a proposito.
        ls.stream().forEach(s -> {
        	respuestaClientObserver.onNext(DameStockRequest.newBuilder().setIdProducto(s).build());
        	synchronized(v) {v.addText("   [ Cliente >>> ]: Solicitado producto id:" + s);}
        	try { Thread.sleep(100); } catch (Exception ex){ ex.printStackTrace(); }
        });
        
        // Indicar al servidor que el cliente ha terminado de enviar mensajes.
        respuestaClientObserver.onCompleted();

        // Esperar hasta que el servidor haya terminado de enviar respuestas.
		try {
			latch.await(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
		synchronized(v) {v.addText("   [ Cliente ... ]: Fin de la comunicación.");}
		synchronized(v) {v.addText("\nFin del programa.");}
	    
	    return;
          
	}
	
}
