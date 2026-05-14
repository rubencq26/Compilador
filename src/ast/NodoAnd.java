package ast;

import java.util.List;

public class NodoAnd extends NodoBinario{
    public NodoAnd(NodoAST izq, NodoAST der) {
        super(izq, der);
    }

    @Override
    public NodoAST optimizar() {
        // p ^ F = F
        if((izq instanceof NodoBoolean && ((NodoBoolean) izq).valor == false) || (der instanceof NodoBoolean && ((NodoBoolean) der).valor == false)){
            return new NodoBoolean(false);
        }

        // p ^ T = T
        if(der instanceof NodoBoolean && ((NodoBoolean) der).valor == true){
            return izq.optimizar();
        }

        //T ^ p = p
        if(izq instanceof NodoBoolean && ((NodoBoolean) izq).valor == true){
            return der.optimizar();
        }

        // p ^ ~p = F
        if((izq instanceof NodoNot && ((NodoNot)izq).contenido.equals(der)) || (der instanceof NodoNot && ((NodoNot)der).contenido.equals(izq))){
            return new NodoBoolean(false);
        }

        // ( p v q ) ^ p = p
        if(izq instanceof NodoOr && (((NodoOr)izq).izq.equals(der)  || ((NodoOr)izq).der.equals(der))){
            return der.optimizar();
        }

        //p ^ ( p v q ) = p
        if(der instanceof NodoOr && (((NodoOr)der).izq.equals(izq)  || ((NodoOr)der).der.equals(izq))){
            return izq.optimizar();
        }


        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        this.izq = izq.convertirFNC();
        this.der = der.convertirFNC();
        return this;
    }

    @Override
    public List<Clausula> generarClausulas() {
        List<Clausula> lista = new java.util.ArrayList<>();
        lista.addAll(izq.generarClausulas());
        lista.addAll(der.generarClausulas());
        return lista;
    }

    @Override
    public void extraerLiterales(Clausula c) { /* No hace nada */ }


}
