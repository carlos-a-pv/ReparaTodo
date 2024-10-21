package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Producto {

    public String idProducto;
    public String tipo;
    public String marca;
    public String modelo;
    public String descripcion;

}
