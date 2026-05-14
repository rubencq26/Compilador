package ast;

import java.util.List;

public class NodoOr extends NodoBinario{
    public NodoOr(NodoAST izq, NodoAST der) {
        super(izq, der);
    }

    @Override
    public NodoAST optimizar() {
        // p v T = T
        if((izq instanceof NodoBoolean && ((NodoBoolean) izq).valor == true) || (der instanceof NodoBoolean && ((NodoBoolean) der).valor == true)){
            return new NodoBoolean(true);
        }

        // p v F = p
        if(der instanceof NodoBoolean && ((NodoBoolean) der).valor == false){
            return izq.optimizar();
        }

        // F v p = p
        if(izq instanceof NodoBoolean && ((NodoBoolean) izq).valor == false){
            return der.optimizar();
        }

        // p v ~p = T
        if((izq instanceof NodoNot && ((NodoNot)izq).contenido.equals(der)) || (der instanceof NodoNot && ((NodoNot)der).contenido.equals(izq))){
            return new NodoBoolean(true);
        }

        // (p ^ q) v p = p
        if(izq instanceof NodoAnd && (((NodoAnd)izq).izq.equals(der) || ((NodoAnd)izq).der.equals(der))){
            return der.optimizar();
        }

        // p v (p ^ q) = p
        if(der instanceof NodoAnd && (((NodoAnd)der).izq.equals(izq) || ((NodoAnd)der).der.equals(izq))){
            return izq.optimizar();
        }



        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        NodoAST izqFNC = izq.convertirFNC();
        NodoAST derFNC = der.convertirFNC();

        //Distributiva 1: p v (q ^ r) = (p v q) ^ (p v r)
        if(derFNC instanceof NodoAnd) {
            NodoAnd and = (NodoAnd) derFNC;
            NodoAST or1 = new NodoOr(izqFNC, and.izq).convertirFNC();
            NodoAST or2 = new NodoOr(izqFNC, and.der).convertirFNC();
            NodoAST nuevoAnd = new NodoAnd(or1, or2);
            return nuevoAnd.convertirFNC();
        }

        // Distributiva 2: (q ^ r) v p = (q v p) ^ (r v p)
        if (izqFNC instanceof NodoAnd) {
            NodoAnd and = (NodoAnd) izqFNC;
            NodoAST or1 = new NodoOr(and.izq, derFNC).convertirFNC();
            NodoAST or2 = new NodoOr(and.der, derFNC).convertirFNC();
            NodoAST nuevoAnd = new NodoAnd(or1, or2);
            return nuevoAnd.convertirFNC();
        }

        this.izq = izqFNC;
        this.der = derFNC;
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
    public void extraerLiterales(Clausula c) { izq.extraerLiterales(c); der.extraerLiterales(c); }
}
