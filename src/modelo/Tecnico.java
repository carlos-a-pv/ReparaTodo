package modelo;

import lombok.Data;

@Data
public class Tecnico extends Usuario {

    public String idTecnico;
    public String nombre;
    public String Especialidad;
}
