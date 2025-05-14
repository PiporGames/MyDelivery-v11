package delivery;
import java.util.LinkedList;

import bank.Bank;

public class CadenaRestaurantes   {
	LinkedList <Restaurante> lista=new LinkedList<>();
	int numRestaurantes;
	static Bank b = new Bank (Config.numeroRestaurantes,0);	// todos los restaurantes comparten el mismo banco, pero cada uno tiene su cuenta
	
	public CadenaRestaurantes (int _numRestaurantes) {
		numRestaurantes = _numRestaurantes;
	}
	
	public Bank getBank () {
		return b;
	}
	
	public void crearRestaurantes () {
		for (int i=0;i<numRestaurantes;i++) {	
			Restaurante r = new Restaurante (b.getAccount(i),			// su posición en el array de cuentas será su cuenta
											""+i, 						// el nombre del Restaurante
											Config.numeroMoteros);  	// número de moteros del restaurante. Por defecto todos los restaurantes con el mismo número de moteros
											
			lista.add(r);
		}
	}
	
	public LinkedList<Restaurante> getRestaurantes () {
		return lista;
	}
}
