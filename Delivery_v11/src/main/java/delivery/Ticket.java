package delivery;
public class Ticket {
	static int numero = 0;
	
	public static int getNumero() {
		return numero++;
	}
	
	public static void incNumero () {
		numero++;
	}
}
