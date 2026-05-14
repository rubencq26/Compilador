package ast;

import java.util.List;

public abstract class NodoAST {


    public NodoAST(){

    }

    // En NodoAST.java
    public abstract void print(String prefix);

    private static int contador = 0;
    private String miId = null;

    public String getMiId() {
        if (miId == null) {
            miId = "nodo" + (++contador);
        }
        return miId;
    }

    public abstract String toDot();

    public abstract NodoAST optimizar();

    public abstract NodoAST convertirFNC();

    public abstract List<Clausula> generarClausulas();
    public abstract void extraerLiterales(Clausula c);

    public abstract boolean equals(Object o);

    public abstract int hashCode();

}
