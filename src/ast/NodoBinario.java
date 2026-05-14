package ast;

import java.util.Objects;

public abstract class NodoBinario extends NodoAST{
    public NodoAST izq, der;

    public NodoBinario(NodoAST izq, NodoAST der) {
        super();
        this.izq = izq;
        this.der = der;
    }

    // En NodoBinario.java
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|-- " + this.getClass().getSimpleName());
        izq.print(prefix + "|   ");
        der.print(prefix + "|   ");
    }

    @Override
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        // Me defino usando el nombre real de la clase (NodoAnd, NodoOr, etc.)
        sb.append(getMiId()).append(" [label=\"").append(this.getClass().getSimpleName()).append("\"];\n");

        // Hijo izquierdo
        sb.append(izq.toDot());
        sb.append(getMiId()).append(" -> ").append(izq.getMiId()).append(";\n");

        // Hijo derecho
        sb.append(der.toDot());
        sb.append(getMiId()).append(" -> ").append(der.getMiId()).append(";\n");

        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodoBinario that = (NodoBinario) o;
        return Objects.equals(izq, that.izq) && Objects.equals(der, that.der);
    }

    @Override
    public int hashCode() {
        return Objects.hash(izq, der);
    }
}
