package modelo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallCenter extends Usuario {
    public String idCall;
    public String nombre;
    public String email;
    public String telefono;
    public String direccion;

    public CallCenter(String user, String password, String idCall, String nombre, String email, String telefono, String direccion) {
        super(user, password);
        this.idCall = idCall;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}
