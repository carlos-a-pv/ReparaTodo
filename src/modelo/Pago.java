package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Pago {
    public String idPago;
    public float monto;
    public LocalDate fechaPago;
    public String metodoPago;
}
