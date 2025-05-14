package delivery;

// La clase robot pone items en el contenedor asociado de forma infinita.
public class Robot extends Thread {
	private Contenedor cont;
	
	public Robot(Contenedor _cont) {
		cont = _cont;
	}
	
	public void run() {
		while (true) {
			if (cont != null) cont.poner();
		}
	}
}
