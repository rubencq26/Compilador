package ast;

import java.util.List;
import java.util.Objects;

public class NodoBoolean extends NodoAST{
    boolean valor;
    public NodoBoolean(boolean valor){
        super();
        this.valor = valor;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|-- BOOLEAN: " + valor);
    }

    @Override
    public String toDot() {
        return getMiId() + " [label=\"BOOL: " + valor + "\"];\n";
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
    public void extraerLiterales(Clausula c) {
        c.addLiteral(new Literal(String.valueOf(this.valor), false));
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodoBoolean that = (NodoBoolean) o;
        return valor == that.valor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
