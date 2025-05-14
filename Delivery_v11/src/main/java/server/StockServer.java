// Definición del paquete al que pertenece esta clase.
package server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Importaciones de bibliotecas necesarias para la ejecución del servidor gRPC y la interfaz de usuario.
import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.StockService;
import pcd.util.Ventana; // Clase para crear una interfaz de usuario simple.

// Definición de la clase principal del servidor.
public class StockServer {
	
	
	
    public static void main(String[] args) throws IOException, InterruptedException {
        // Creación de una ventana para mostrar los mensajes de log del servidor.
        Ventana v = new Ventana ("Servidor · gRPC", 400, 400);
        
        // Crear base de datos de producto
        ConcurrentMap<String, Integer> db = new ConcurrentHashMap<String, Integer>();
        
        // Construcción del servidor gRPC en el puerto 50000 y añadiendo el servicio definido por 'StockService'.
        Server server = ServerBuilder.forPort(50000).addService(new StockService(v, db)).build();

        // Añade texto a la ventana para indicar el inicio de la lista de servicios y métodos disponibles.
        v.addText("##########################################################################");
        v.addText("  SERVIDOR GRPC | LISTADO DE SERVICIOS Y MÉTODOS ");
        v.addText("##########################################################################\n");
        
        // Itera sobre todos los servicios definidos en el servidor y los muestra en la ventana.
        	// Muestra el nombre del servicio.
        	// Itera sobre todos los métodos del servicio y los muestra.
        server.getServices().forEach(s-> {
        	v.addText(" Servicio: " + s.getServiceDescriptor().getName());
        	s.getServiceDescriptor().getMethods().forEach(n -> {
        		v.addText(" Método: " + n.getFullMethodName());});
        	});

        
        // Inicia el servidor gRPC y muestra un mensaje en la ventana indicando que el servidor está en funcionamiento.
        server.start();

        v.addText("");
        v.addText("Servidor iniciado y escuchando en puerto 50000");
        v.addText("...");
        
        // Estadísticas finales de la base de datos
        Thread.sleep(11000);
        v.addText("Estadísticas finales de la base de datos:");
        db.keySet().stream().forEach(c -> { v.addText("id:" + c.toString() + " | cantidad:" + db.get(c).toString());});
        
        // El servidor permanece activo esperando conexiones entrantes.
        server.awaitTermination();
    }
      
}
