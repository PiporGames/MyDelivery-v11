package delivery;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import pcd.util.Ventana;

public class PedidosNoPagados {

	public static void main (String args[]) {
		Ventana v = new Ventana("Servidor · Pedidos No Pagados", 200, 200);
		
		// Definimos las propiedades del socket
		DatagramSocket socket; // El socket activo
		DatagramPacket receivePacket; // El datagrama
		byte[] receiveData = new byte[1024]; // La memoria asignada a ese datagrama
	    
		int localPort = 10000; // Puerto local del socket
		int timeout = -1; // Tiempo de espera antes de cerrar el servidor tras recibir el último paquete. (en ms, -1 indica desactivado)
		
		try {
			// Creamos el socket		
			socket = new DatagramSocket(localPort);
			// Recibimos los paquetes
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			v.addText("Servidor UDP de Pedidos No Pagados abierto en " + localPort + "...");
			
			DatosPagoPedido dpp = null;
			Boolean fin = false;
			
			while (!fin) {
				// Esperamos paquetes
				socket.receive(receivePacket);	
				// Definimos el buffer de salida
				ObjectInputStream ois = new ObjectInputStream (new ByteArrayInputStream  (receivePacket.getData()));
				try {
					// Intentamos extraer el objeto
					dpp = (DatosPagoPedido) ois.readObject();
				} catch (ClassNotFoundException e) {
				e.printStackTrace();
				}
				if (dpp == null) {
					// Si es el último objeto en recibir, salir. (El último paquete en recibir sería uno que fuese nulo)
					fin = true;
				}
				else {
					v.addText("Dato recibido del cliente: "+ dpp.getId());
					if (timeout != -1) socket.setSoTimeout(timeout); // Ajustamos el timeout
				}
			}
			
			v.addText("Finalizando servidor por tiempo agotado...");
			// Cerramos socket
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println ("Servidor finalizado"); 
	}
}