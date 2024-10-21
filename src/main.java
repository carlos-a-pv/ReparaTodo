import controlador.ModelFactoryController;
import modelo.*;
import modelo.enums.Estado;

import java.time.LocalDate;

public class main {

    public static void main(String[] args) {

        Taller taller =new Taller();
        ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
        Orden orden = new Orden(
                "01",
                new Cliente("1006688988","jhojan","email","tel","calle","jhojan","123"),
                new Pago("01",1000, LocalDate.now(),"efectivo"),
                new Producto("01","Celular","HUAWEI","MATE PRO","OCTA CORE 2.3GHZ - 6GB RAM - 256GB ROM"),
                LocalDate.now(),
                Estado.RECEPCIONADA,
                "Presenta ruptura de pantalla"
        );
        modelFactoryController.crearOrden(orden);

        boolean prueba = modelFactoryController.cambiarEstadoOrden("01",Estado.EN_REPARACION);
        System.out.println(prueba);
    }
}
