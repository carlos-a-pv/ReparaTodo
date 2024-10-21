package resources;

import modelo.Orden;
import modelo.enums.Estado;

/**
 *
 * @param <T>
 */
public class Cola<T> {

    public NodoCola<T> nodoPrimero, nodoUltimo;
    public int tamanio;


    /**
     * Agrega un elemento en la Cola
     *
     * @param dato elemento a guardar en la Cola
     */
    public void encolar(T dato) {

        NodoCola<T> nodo = new NodoCola<>(dato);

        if (estaVacia()) {
            nodoPrimero = nodoUltimo = nodo;
        } else {
            nodoUltimo.setSiguienteNodo(nodo);
            nodoUltimo = nodo;
        }

        tamanio++;
    }

    /**
     * Retorna y elimina el elemento que estï¿½ al incio de la Cola
     *
     * @return Primer elemento de la Cola
     */
    public T desencolar() {

        if (estaVacia()) {
            throw new RuntimeException("La Cola estï¿½ vacï¿½a");
        }

        T dato = nodoPrimero.getValorNodo();
        nodoPrimero = nodoPrimero.getSiguienteNodo();

        if (nodoPrimero == null) {
            nodoUltimo = null;
        }

        tamanio--;
        return dato;
    }


    /**
     * Verifica si la Cola estï¿½ vacï¿½a
     *
     * @return true si estï¿½ vacï¿½a
     */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Busca y retorna el nodo con el valor especificado al desencolar la Cola
     *
     * @param dato Valor a buscar y desencolar
     * @return Nodo con el valor especificado, o null si no se encuentra
     */
    public NodoCola<T> buscarNodoCola(T dato) {
        if (estaVacia()) {
            throw new RuntimeException("La Cola estÃ¡ vacÃ­a");
        }

        NodoCola<T> aux = nodoPrimero;
        NodoCola<T> nodoAnterior = null;

        while (aux != null && !aux.getValorNodo().equals(dato)) {
            nodoAnterior = aux;
            aux = aux.getSiguienteNodo();
        }

        if (aux != null) {
            // Se encontrÃ³ el nodo con el valor especificado
            if (nodoAnterior != null) {
                // El nodo no es el primero en la cola
                nodoAnterior.setSiguienteNodo(aux.getSiguienteNodo());
                if (nodoAnterior.getSiguienteNodo() == null) {
                    // El nodo era el Ãºltimo en la cola
                    nodoUltimo = nodoAnterior;
                }
            } else {
                // El nodo es el primero en la cola
                nodoPrimero = aux.getSiguienteNodo();
                if (nodoPrimero == null) {
                    // El nodo era el Ãºnico en la cola
                    nodoUltimo = null;
                }
            }
        }

        // Encola nuevamente el nodo desencolado
        encolar(aux != null ? aux.getValorNodo() : null);

        return aux;
    }

    public void insertarEnPosicion(T dato, int posicion) {
        // Verificar si la posiciÃ³n es vÃ¡lida
        if (posicion < 0 || posicion > tamanio) {
            throw new IllegalArgumentException("PosiciÃ³n invÃ¡lida");
        }

        // Desencolar hasta la posiciÃ³n deseada
        int i = 0;
        while (i < posicion) {
            T elementoDesencolado = desencolar();
            encolar(elementoDesencolado);
            i++;
        }

        // Encolar el nuevo dato en la posiciÃ³n deseada
        encolar(dato);

        // Desencolar y volver a encolar los elementos restantes
        i = posicion + 1;
        while (i < tamanio) {
            T elementoDesencolado = desencolar();
            encolar(elementoDesencolado);
            i++;
        }
    }


    /**
     * Borra completamente la Cola
     */
    public void borrarCola() {
        nodoPrimero = nodoUltimo = null;
        tamanio = 0;
    }

    /**
     * @return the primero
     */
    public NodoCola<T> getPrimero() {
        return nodoPrimero;
    }

    /**
     * @return the ultimo
     */
    public NodoCola<T> getUltimo() {
        return nodoUltimo;
    }

    /**
     * @return the tamano
     */
    public int getTamano() {
        return tamanio;
    }

    /**
     * Verifica si la Cola es idï¿½ntica a la actual
     *
     * @param cola Cola a comparar
     * @return True si son iguales
     */
    public boolean sonIdenticas(Cola<T> cola) {

        Cola<T> clon1 = clone();
        Cola<T> clon2 = cola.clone();

        if (clon1.getTamano() == clon2.getTamano()) {

            while (!clon1.estaVacia()) {
                if (!clon1.desencolar().equals(clon2.desencolar())) {
                    return false;
                }
            }

        } else {
            return false;
        }

        return true;
    }

    /**
     * Imprime una cola en consola
     */
    public void imprimir() {
        NodoCola<T> aux = nodoPrimero;
        while (aux != null) {
            System.out.print(aux.getValorNodo() + "\t");
            aux = aux.getSiguienteNodo();
        }
        System.out.println();
    }

    @Override
    protected Cola<T> clone() {

        Cola<T> nueva = new Cola<>();
        NodoCola<T> aux = nodoPrimero;

        while (aux != null) {
            nueva.encolar(aux.getValorNodo());
            aux = aux.getSiguienteNodo();
        }

        return nueva;
    }

	/*public Nodo<T> buscarNodo(T tarea) {
		Nodo<T> aux = nodoPrimero;

		while(aux!=null) {
			if(aux.getValorNodo().equals(dato)) {
				return aux;
			}
			aux = aux.getSiguienteNodo();
		}

		return null;
	}*/


    @Override
    public String toString() {
        return "Cola{" +
                "nodoPrimero=" + nodoPrimero +
                ", nodoUltimo=" + nodoUltimo +
                ", tamanio=" + tamanio +
                '}';
    }
    /**
     * Busca y retorna el nodo de tipo Orden con el idOrden especificado
     * @param idOrden el idOrden del objeto Orden a buscar
     * @return el objeto Orden si se encuentra, null en caso contrario
     */
    public Orden buscarPorIdOrden(String idOrden) {
        if (estaVacia()) {
            throw new RuntimeException("La Cola está vacía");
        }

        NodoCola<T> aux = nodoPrimero;

        while (aux != null) {
            T dato = aux.getValorNodo();
            if (dato instanceof Orden) { // Verifica que el objeto sea de tipo Orden
                Orden orden = (Orden) dato;
                if (orden.getIdOrden().equals(idOrden)) {
                    return orden; // Retorna la Orden si el idOrden coincide
                }
            }
            aux = aux.getSiguienteNodo();
        }

        return null; // Retorna null si no se encuentra la Orden
    }
    public boolean cambiarEstadoOrden(String idOrden, Estado nuevoEstado) {
        if (estaVacia()) {
            throw new RuntimeException("La Cola está vacía");
        }

        NodoCola<T> aux = nodoPrimero;
        boolean encontrado = false;

        while (aux != null) {
            Orden orden = (Orden) aux.getValorNodo();
            if (orden.getIdOrden().equals(idOrden)) {
                orden.setEstado(nuevoEstado); // Asumiendo que tienes un método setEstado en tu clase Orden
                encontrado = true;
                break;
            }
            aux = aux.getSiguienteNodo();
        }

        return encontrado;
    }


}


