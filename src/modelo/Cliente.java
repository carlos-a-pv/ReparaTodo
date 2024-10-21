package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cliente extends Usuario  {

    public String idCliente;
    public String nombre;
    public String email;
    public String telefono;
    public String direccion;


}
