package ast;

import java.util.List;
import java.util.Objects;

public class NodoID extends NodoAST{
    String nombre;
    public NodoID(String nombre){
        super();
        this.nombre = nombre;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|-- ID: " + nombre);
    }

    @Override
    public String toDot() {
        return getMiId() + " [label=\"ID: " + nombre + "\"];\n";
    }

    @Override
    public NodoAST optimizar() {
        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        return this;
    }

    @Override
    public List<Clausula> generarClausulas() {
        Clausula c = new Clausula();
        this.extraerLiterales(c);
        List<Clausula> lista = new java.util.ArrayList<>();
        lista.add(c);
        return lista;
    }

    @Override
    public void extraerLiterales(Clausula c) { c.addLiteral(new Literal(this.nombre, false)); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodoID nodoID = (NodoID) o;
        return Objects.equals(nombre, nodoID.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }

}
