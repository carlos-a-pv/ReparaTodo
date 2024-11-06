package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Actividad {
    String idTecnico;
    String idOrden;
    LocalDate fecha;
    String descripcion;

}
