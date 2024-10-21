package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente extends Usuario  {

    public String idCliente;
    public String nombre;
    public String email;
    public String telefono;
    public String direccion;




    public Cliente(String idCliente, String nombre, String email, String telefono, String direccion, String user, String password) {
        super(user, password); // Llama al constructor de Usuario
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}
