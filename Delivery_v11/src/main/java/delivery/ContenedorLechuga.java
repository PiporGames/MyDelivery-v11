package delivery;

import java.util.concurrent.*;

// El contenedor de lechuga es un contenedor especial que hereda del contenedor genérico pero añade su propio sistema de gestión de elementos mediante un LinkedBlockingQueue.
// La diferencia es que ahora no guardamos la cantidad de elementos en el contenedor con un int, sino que ahora guardamos elementos tontos (que no tienen un valor útil)
// para rellenar la cola.
public class ContenedorLechuga extends Contenedor {
	BlockingQueue<Object> bq;
	
	public ContenedorLechuga(String _id, int _max, int _vHeight, int _vWidth) {
		super(_id, _max, _vHeight, _vWidth); //llamamos al constructor superior
		bq = new LinkedBlockingQueue<Object>(_max); //cola linked blocking
		id = _id;
	}
	
	@Override
	public void poner() {
		Object obj = new Object(); // creamos un objeto tonto
		try {
			bq.put(obj); // El LinkedBlockingQueue se encarga automaticamente de gestionar la sincronización entre el quita y pon de la cocina y el robot.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		v.addText("Robot asociado pone item (quedan ahora " + bq.size() + ")");
	}
	
	@Override
	public void coger() {
		try {
			bq.take(); // El LinkedBlockingQueue se encarga automaticamente de gestionar la sincronización entre el quita y pon de la cocina y el robot.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		v.addText("Cocina asociada quita item (quedan ahora " + bq.size() + ")");
	}

}
