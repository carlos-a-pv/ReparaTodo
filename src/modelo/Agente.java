package modelo;

import lombok.*;

@Getter
@Setter
public class Agente extends Usuario {

    public String idAgente;
    public String nombre;
    public String email;
    public String telefono;

    public Agente(String idAgente, String nombre, String email, String telefono, String user, String password) {
        super(user, password);
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
}
