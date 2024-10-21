package resources;

/**
 * Clase nodo aplicando Generics
 *
 *
 *
 * **/


public class NodoCola<T> {

    private NodoCola<T> siguienteNodo;
    private T valorNodo;


    /**
     * Constructor de la clase Nodo
     */
    public NodoCola(T valorNodo) {
        this.valorNodo = valorNodo;
    }


    /**
     * Constructor de la clase Nodo
     * @param dato Elemento que se guarda en el Nodo
     * @param siguiente Enlace al siguiente Nodo
     */
    public NodoCola(T dato, NodoCola<T> siguiente) {
        super();
        this.valorNodo = dato;
        this.siguienteNodo = siguiente;
    }


    //Metodos get y set de la clase Nodo

    public NodoCola<T> getSiguienteNodo() {
        return siguienteNodo;
    }


    public void setSiguienteNodo(NodoCola<T> siguienteNodo) {
        this.siguienteNodo = siguienteNodo;
    }


    public T getValorNodo() {
        return valorNodo;
    }


    public void setValorNodo(T valorNodo) {
        this.valorNodo = valorNodo;
    }
}
