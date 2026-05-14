package ast;

import java.util.List;

public class NodoImpl extends NodoBinario{
    public NodoImpl(NodoAST izq, NodoAST der) {
        super(izq, der);
    }

    @Override
    public NodoAST optimizar() {

         return this;

    }

    @Override
    public List<Clausula> generarClausulas() {
        return new java.util.ArrayList<>();
    }

    @Override
    public void extraerLiterales(Clausula c) { }


    @Override
    public NodoAST convertirFNC() {
        NodoAST izqFNC = izq.convertirFNC();
        NodoAST derFNC = der.convertirFNC();

        // p -> q = ~4 v q
        NodoAST not = new NodoNot(izqFNC);
        NodoAST or = new NodoOr(not.convertirFNC(), derFNC);
        return or.convertirFNC();
    }
}
