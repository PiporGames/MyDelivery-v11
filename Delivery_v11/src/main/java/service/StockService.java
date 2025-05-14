// Define el paquete al que pertenece esta clase.
package service;
// Importa las dependencias necesarias.
import io.grpc.stub.StreamObserver;
import pcd.util.*;
import grpc.StockServiceGrpc.StockServiceImplBase;
import grpc.Stock.VentaRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import grpc.Stock.DameStockRequest;
import grpc.Stock.ProductosReply;
import grpc.Stock.RestoProductoReply;
import grpc.Stock.StockRequest;


// Clase que extiende de StockServiceImplBase, que es la clase base generada por gRPC para servir los servicios.
public class StockService extends StockServiceImplBase {
    
    // Instancia de Ventana para la interfaz de usuario, usada para mostrar mensajes.
    Ventana v;
    // Map para guardar los productos (id)-stock
    ConcurrentMap<String, Integer> db = null;
    
    // Constructor que recibe una instancia de Ventana para utilizar en la clase.
    public StockService (Ventana v_, ConcurrentMap<String, Integer> db_) {
        v = v_;
        db = db_;
    }

    
    
    // Método unario. Recibe una solicitud y responde con un único mensaje.
    // No hace falta usar synchronized para sincronizar la E/S pues utilizamos un ConcurrentMap, que permite acceso concurrente.
    @Override
    public void registrarVenta(VentaRequest solicitud, StreamObserver<RestoProductoReply> respuestaObserver) {

        // Muestra el mensaje recibido del cliente.
    	synchronized(v) {v.addText("[ Servidor <<< ]: Cliente ha solicitado registrar venta de id:" + solicitud.getIdProducto()); }  
        
        
        // Comprobamos que el producto existe
        String mensaje = "";
        if (db.containsKey(solicitud.getIdProducto())) {
        	//Existe producto. Eliminar n stock.
        	db.put(solicitud.getIdProducto(), db.get(solicitud.getIdProducto()) - solicitud.getCantidad());
        	mensaje = "Reduciendo el producto id:" + solicitud.getIdProducto() + " en " + solicitud.getCantidad() + " unidades.";
        } else {
        	//No existe el producto. Registrar, poner a 100 el stock, eliminar n stock.
        	db.put(solicitud.getIdProducto(), (100 - solicitud.getCantidad()));
        	mensaje = "El producto id:" + solicitud.getIdProducto() + " no existe. Registrando y reduciendo la cantidad en " + solicitud.getCantidad() + " unidades.";
        }
        
        synchronized(v) { v.addText("[ Servidor <<< ]: " + mensaje); }
        
        // Procesa la solicitud y crea una respuesta.
        RestoProductoReply respuesta = RestoProductoReply.newBuilder().setResto(db.get(solicitud.getIdProducto())).build();
        
        // Envía la respuesta al cliente.
        respuestaObserver.onNext(respuesta);
        
        // Notifica que la respuesta ha sido completamente enviada.
        respuestaObserver.onCompleted();
        
        // Muestra la respuesta enviada al cliente en la interfaz de usuario.
        synchronized(v) {v.addText("[ Servidor >>> ]: Enviado producto id:" + solicitud.getIdProducto() + " con cantidad " + db.get(solicitud.getIdProducto()));
        			     v.addText("[ Servidor ... ]: Fin de servicio (registrarVenta).\n");}
    }
    



	// Método de streaming del servidor. Responde al cliente con una secuencia de mensajes.
    @Override
    public void bajoStock(StockRequest solicitud, StreamObserver<ProductosReply> respuestaServerObserver) {
    	
        // Muestra el mensaje recibido del cliente
    	synchronized(v) {v.addText("[ Servidor <<< ]: Cliente ha solicitado enviar productos con umbral <=" + solicitud.getUmbral());}
        
        
        // Buscamos en la base de datos los pedidos con el umbral dado
        List<String> ls = new LinkedList<>();
        for (String s : db.keySet()) {
        	if (db.get(s) <= solicitud.getUmbral()) ls.add(s);
        }
        
        //Envía varios Idproductos al cliente
        ls.stream().forEach( s -> {
        	ProductosReply response = ProductosReply.newBuilder().setIdProducto(s).build();
        	respuestaServerObserver.onNext(response);
        	synchronized(v) {v.addText("[ Servidor >>> ]: Enviado producto con id:" + s);}
        });
        
        // Una vez enviados todos los caracteres, completa la comunicación.
        respuestaServerObserver.onCompleted();
        synchronized(v) {v.addText("[ Servidor ... ]: Fin de servicio (bajoStock).\n");}
    }
    
	// Método bidireccional del servidor. Responde al cliente con una secuencia de mensajes.
    // Devuelve un StreamObserver<DameStockRequest> para el cliente, ya que el cliente quiere un DameStockRequest (id solo) para enviarle peticiones por ahí al server.
    // Acepta como parámetro un StreamObserver<VentaRequest> por que es lo que el servidor quiere para enviarle mensajes al cliente, un VentaRequest (id y cantidad).
    @Override
    public StreamObserver<DameStockRequest> dameStock(StreamObserver<VentaRequest> respuestaServerObserver) {
    	
        // Retorna un observador de la solicitud
    	return new StreamObserver<DameStockRequest>() {

			@Override
			public void onNext(DameStockRequest value) {
				//Notificar en la ventana del mensaje nuevo
				synchronized(v) {v.addText("[ Servidor <<< ]: Recibido producto con id:" + value.getIdProducto());}
				//Construir una respuesta
				int cant;
				if (db.containsKey(value.getIdProducto())) { // Si el id existe en la base de datos
					cant = db.get(value.getIdProducto()); // Obtener cantidad
				} else {
					cant = 0; // De lo contrario, interpretar que la cantidad es 0.
				}
				VentaRequest response = VentaRequest.newBuilder().setIdProducto(value.getIdProducto()).setCantidad(cant).build();
				//Enviar la respuesta
				respuestaServerObserver.onNext(response);
				//Notificar del envio de la respuesta
				synchronized(v) {v.addText("[ Servidor >>> ]: Enviado producto con id:" + value.getIdProducto() + " con cantidad: " + cant);}		
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onCompleted() {
				synchronized(v) {v.addText("[ Servidor ... ]: Fin del servicio (dameStock).\n");}
				respuestaServerObserver.onCompleted();
			}
    		
    	};
    } 
}
