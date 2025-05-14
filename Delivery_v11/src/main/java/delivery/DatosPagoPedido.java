package delivery;

import java.io.Serializable;

// Esta clase será el objeto que se enviará a través del tunel TCP entre cliente y servidor de la sesión 9.
// Copiado del documento de la sesión 9.
public class DatosPagoPedido implements Serializable {
	private static final long serialVersionUID = 1L; 
	private String id;
	private double importe;
	
	public DatosPagoPedido (String _id, double _importe) {
		id=_id;
		importe = _importe;
	}
	public String getId() {
		return id;
	}
	public double getImporte () {
		return importe;
	}
}
