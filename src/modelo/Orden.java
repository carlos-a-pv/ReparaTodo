package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import modelo.enums.Estado;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Orden {

    public String idOrden;
    public Cliente cliente;
    public Pago pago;
    public Producto producto;
    public LocalDate fechaCreacion;
    public Estado estado;
    public String descripcionAveria;

}
