package modelo;

import lombok.*;

@Data
@AllArgsConstructor
public class Agente extends Usuario {

    public String idAgente;
    public String nombre;
    public String email;
    public String telefono;

}
