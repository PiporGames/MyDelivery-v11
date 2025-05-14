package delivery;

import java.io.*;
import java.net.*;

import pcd.util.Ventana;

// Esta clase será el servidor TCP en la Sesión 9.
// Esta clase abrirá el puerto 9999 de la máquina para recibir pedidos, luego responderá con KO si el pedido es <12 o OK si >12.
public class PasarelaPago implements Runnable{
	private Ventana v; // Una ventana para mostrar la recepción de objetos
	private Socket sck; // Un socket activo donde se ejecutará la lógica de run()
	
	
	public PasarelaPago(Ventana _v, Socket _sck) {
		v = _v;
		sck = _sck;
	}
	
	public void run() {
		try {
			// Procesamos la respuesta
			ObjectInputStream entrada = new ObjectInputStream (sck.getInputStream()); // Definimos un buffer DE OBJETOS que contendrá los datos recibidos
			DatosPagoPedido dpp = (DatosPagoPedido) entrada.readObject(); // Convertimos ese buffer en un objeto que conocemos -> Un DatosPagoPedido
			BufferedWriter salida = new BufferedWriter (new OutputStreamWriter (sck.getOutputStream())); // Creamos un buffer DE TEXTO para enviarle la respuesta en texto del cliente.
			
			//Mostramos en la ventana el objeto recibido.
			synchronized (v) {v.addText(Thread.currentThread() + " >>> Pedido: " + dpp.getId() + " con precio: " + dpp.getImporte());}
			
			//Enviamos respuesta
			if (dpp.getImporte() > 12) {
				salida.write("OK\n"); // Si, continuar cocinando
				synchronized (v) {v.addText(Thread.currentThread() + " >>> Enviado OK");}
			} else {
				salida.write("KO\n"); // No, no lo cocines.
				synchronized (v) {v.addText(Thread.currentThread() + " >>> Enviado KO");}
			}
			salida.flush(); // Con esta instrucción obligamos al buffer de salida a vaciarse y enviar el mensaje inmediatamente.
			
			synchronized (v) {v.addText(Thread.currentThread() + " >>> Fin de la conexión.");}
			
			//Cerramos socket
			sck.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String args[]) {
		Ventana v =  new Ventana ("Servidor · Pasarela Pago", 0, 0);
		
		int srvPort = 9999; // Puerto local del server
		int timeout = -1; // Tiempo de espera antes de cerrar el servidor tras recibir el último paquete. (en ms, -1 indica desactivado)
		ServerSocket srvSck = null; // Definimos un socket pasivo
		Socket sck = null; // Definimos un socket activo
		boolean fin = false;
		
		// Inicializamos el socket de servidor pasivo en 9999
		try {
			srvSck = new ServerSocket(srvPort); // Inicializamos el socket pasivo
		} catch (Exception e) {
			e.printStackTrace();
		}
		v.addText("Servidor TCP de Pasarela Pago abierto en " + srvPort + "...");
		while (!fin) { 
			try {
				sck = srvSck.accept(); // Al aceptar el socket pasivo, se nos devuelve uno activo, que guardamos en "sck".
				new Thread (new PasarelaPago (v, sck)).start(); // Lanzamos un Thread con dicho socket activo
				v.addText("Se ha lanzado un nuevo Thread.");
				if (timeout != -1) srvSck.setSoTimeout(timeout); // Ajustamos el timeout
			} 
			catch (SocketTimeoutException e1) {
				break;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		v.addText("Finalizando servidor por tiempo agotado...");
		try {
			sck.close();
			srvSck.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println ("Servidor finalizado"); 
	}
	
}
