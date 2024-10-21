package controlador;

import modelo.Taller;

public enum TallerController {
    INSTACE;
    private final Taller model;

    TallerController(){
        model= new Taller();
    }

    public Taller getModel(){
        return model;
    }
}
