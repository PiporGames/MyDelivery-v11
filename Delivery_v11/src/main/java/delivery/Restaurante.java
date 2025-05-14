package delivery;

import java.io.*;
import java.net.*;

import bank.*;
import pcd.util.ColoresConsola;
import pcd.util.Traza;

public class Restaurante {
	private String nombre;					// nombre del restaurante
	private Account account;				// cuenta bancaria para registrar la recaudaci�n
	private Cocina cocina; 					// la cocina de este restaurante
	private ControlMoteros controlMoteros;  // los moteros de este restaurante


	public Restaurante (Account _ac, String _nombre, int _numeroMoteros) {
		account = _ac;
		nombre = _nombre;
		controlMoteros = new ControlMoteros (this, Config.numeroMoteros); // ajustamos un control motero por restaurante
		cocina = new Cocina (this); //asignamos una cocina
		Traza.traza(ColoresConsola.GREEN_BOLD_BRIGHT, 1,"Creando restaurante: "+nombre);
	}

	public String getNombre () {
		return nombre;
	}

	public double getBalance (){
		return account.getBalance();
	}

	public Account getAccount () {
		return account;
	}

	public void tramitarPedido (Pedido _p) {
		Pedido p =_p;
		// Tramitar un pedido es:
		if (pagarPedido(_p)) { // Se ha aceptado el pago en línea?
			account.deposit(p.getPrecioPedido()); 	// a�adir la cantidad abonada a la cuenta del banco
			controlMoteros.moterosLibres(); 	    // Esperamos hasta que llegue un motero (pipor)
			cocina.cocinar(p);						// mandar el pedido a cocina
			controlMoteros.enviarPedido(p);			// una vez cocinado, mandarlo a los moteros para que uno lo coja
		} else {
			reportarPedidoNoPagado(_p);
		}
	}

	private Boolean pagarPedido(Pedido _p) {
		Boolean res;
		try {
			// Creamos el socket
			Socket s = new Socket ("localhost",9999);
			ObjectOutputStream salida = new ObjectOutputStream (s.getOutputStream());
			// Creamos el objeto a enviar
			DatosPagoPedido dpp = new DatosPagoPedido(_p.getId(), _p.getPrecioPedido());

			// enviamos el objeto
			salida.writeObject(dpp);
			salida.flush();
			// Esperamos la respuesta del servidor
			BufferedReader entrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			// Procesamos la respuesta
			if (entrada.readLine().equals("OK")) {res = true; } else {res = false;}
			// Cerramos socket
			s.close();

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void reportarPedidoNoPagado(Pedido _p) {
		try {
			// Definimos las propiedades del socket
			DatagramSocket socket;
			String srvname="localhost"; // si os conectáis a otra máquina, ponemos la IP de esa máquina
			InetAddress srvIP;
			srvIP = InetAddress.getByName(srvname);
			int srvport=10000;

			// Creamos el socket
			socket = new DatagramSocket();

			byte[] sendData  = new byte[1024];
			ByteArrayOutputStream baos;
			ObjectOutputStream oos;
			DatagramPacket sendPacket;
			DatosPagoPedido dpp = new DatosPagoPedido(_p.idPedido, _p.getPrecioPedido());
			try {
				baos = new ByteArrayOutputStream ();
				oos = new ObjectOutputStream (baos);
				oos.writeObject(dpp);
				sendData= new byte[baos.toByteArray().length];
				sendData= baos.toByteArray();
				sendPacket = new DatagramPacket(sendData, sendData.length, srvIP, srvport); 
				socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
