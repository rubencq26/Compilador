package ast;

import java.util.List;
import java.util.Objects;

public class NodoNot extends NodoAST{
    public NodoAST contenido;
    public NodoNot(NodoAST contenido){
        super();
        this.contenido = contenido;
    }

    // En NodoNot.java
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|-- NOT");
        contenido.print(prefix + "|   ");
    }

    @Override
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMiId()).append(" [label=\"NOT\"];\n");
        sb.append(contenido.toDot());
        sb.append(getMiId()).append(" -> ").append(contenido.getMiId()).append(";\n");
        return sb.toString();
    }

    @Override
    public NodoAST optimizar() {
        // ~(~(p)) = p
        if(contenido instanceof NodoNot) {
            return ((NodoNot)contenido).contenido.optimizar();
        }



        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        NodoAST contenidoFNC = contenido.convertirFNC();

        // ~(~(p)) = p
        if(contenidoFNC instanceof NodoNot) {
            return ((NodoNot)contenidoFNC).contenido.convertirFNC();
        }

        // Morgan 1: ~(p ^ q) = ~p v ~q
        if(contenidoFNC instanceof NodoAnd) {
            NodoAnd and = (NodoAnd)contenidoFNC;
            NodoAST notIzq = new NodoNot(and.izq).convertirFNC();
            NodoAST notDer = new NodoNot(and.der).convertirFNC();
            NodoAST or = new NodoOr(notIzq, notDer).convertirFNC();
            return or.convertirFNC();
        }

        // Morgan 2: ~(p v q) = ~p ^ ~q
        if(contenidoFNC instanceof NodoOr) {
            NodoOr or = (NodoOr)contenidoFNC;
            NodoAST notIzq = new NodoNot(or.izq).convertirFNC();
            NodoAST notDer = new NodoNot(or.der).convertirFNC();
            NodoAST and = new NodoAnd(notIzq, notDer).convertirFNC();
            return and.convertirFNC();
        }

        this.contenido = contenidoFNC;
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
    public void extraerLiterales(Clausula c) { c.addLiteral(new Literal(((NodoID)contenido).nombre, true)); }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodoNot nodoNot = (NodoNot) o;
        return Objects.equals(contenido, nodoNot.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contenido);
    }


}
