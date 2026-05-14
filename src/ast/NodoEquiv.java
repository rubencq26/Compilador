package ast;

import java.util.List;

public class NodoEquiv extends NodoBinario{
    public NodoEquiv(NodoAST izq, NodoAST der) {
        super(izq, der);
    }

    @Override
    public NodoAST optimizar() {
        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        NodoAST izqFNC = izq.convertirFNC();
        NodoAST derFNC = der.convertirFNC();

        // p <-> q = (p -> q) v (q -> p)
        NodoAST impl1 = new NodoImpl(izqFNC, derFNC);
        NodoAST impl2 = new NodoImpl(derFNC, izqFNC);

        NodoAST and = new NodoAnd(impl1.convertirFNC(), impl2.convertirFNC());

        return and.convertirFNC();
    }

    @Override
    public List<Clausula> generarClausulas() {
        return new java.util.ArrayList<>();
    }

    @Override
    public void extraerLiterales(Clausula c) { }


}
