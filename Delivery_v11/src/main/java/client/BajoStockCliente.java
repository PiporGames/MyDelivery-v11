package client;

import grpc.Stock.StockRequest;
import grpc.StockServiceGrpc;
import grpc.StockServiceGrpc.StockServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pcd.util.Ventana;

// Bajo stock cliente utiliza una conexión tipo SERVER-STREAM
// Solo se ejecuta una vez y pregunta por el stock bajo dado un umbral, recibiendo varios objetos.
public class BajoStockCliente {
	
	public static void main(String[] args) {
		Ventana v = new Ventana ("Cliente · Bajo Stock", 600, 600);
		
	    // Mostrar inicio de la operación de stream del servidor en la interfaz de usuario.
	    v.addText("##########################################################################");
	    v.addText("   BAJO STOCK CLIENTE (SERVER-STREAM)");
	    v.addText("##########################################################################\n");
	    
	    // Definimos el canal y las propiedades de la conexión
		String ip = "localhost";
		int port = 50000;
		ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build(); // Define un canal de comunicación
	    
	    // Creación de un stub bloqueante, adecuado para llamadas unarias o de streaming del servidor.
	    StockServiceBlockingStub stub = StockServiceGrpc.newBlockingStub(channel);
	    
	    // Creación de la solicitud con umbral X:
	    int umb = 82;
	    StockRequest s = StockRequest.newBuilder().setUmbral(umb).build();
	    v.addText("   [ Cliente >>> ]: Solicitado lista de productos con stock <= " + umb);
	    
	    // Llamada al método de stream del servidor y manejo de cada respuesta recibida.
	    stub.bajoStock(s).forEachRemaining( response -> {
	    	v.addText("   [ Cliente <<< ]: Recibido producto con id:" + response.getIdProducto());
	    });

	    v.addText("   [Cliente ... ]: Fin de la comunicación.");
	    v.addText("\nFin del programa.");
	    
	    return;
	}
	
}
