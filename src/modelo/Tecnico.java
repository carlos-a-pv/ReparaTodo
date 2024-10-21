package modelo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tecnico extends Usuario {

    public String idTecnico;
    public String nombre;
    public String Especialidad;

    public Tecnico( String idTecnico, String nombre, String especialidad, String user, String password) {
        super(user, password);
        this.idTecnico = idTecnico;
        this.nombre = nombre;
        Especialidad = especialidad;
    }
}
