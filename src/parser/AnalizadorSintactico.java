package parser;

import ast.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorSintactico {
    private AnalizadorLexico.Token nextToken;
    File file;
    private final AnalizadorLexico lexer;


    public AnalizadorSintactico(File file) throws IOException {
        this.file = file;
        this.lexer = new AnalizadorLexico(file);
    }

    public NodoAST parse() throws SintaxException {

        nextToken = lexer.getNextToken();

        return teoria();
    }

    private void match(AnalizadorLexico.TipoToken kind) throws SintaxException{
        if(nextToken.tipo == kind){
            nextToken = lexer.getNextToken();
        }else{
            throw new SintaxException(nextToken, kind);
        }
    }

     private NodoAST teoria()  {
        try {
            NodoAST nombre = nombre();
            importaciones();
            List<NodoAST> formulas = seccionFormulas();
            match(AnalizadorLexico.TipoToken.EOF);
            return new NodoPrograma(nombre,formulas);
        }catch (SintaxException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private NodoAST nombre() throws SintaxException {
        match(AnalizadorLexico.TipoToken.NAME);
        NodoAST nombre = identificador();
        match(AnalizadorLexico.TipoToken.P_Y_C);
        return nombre;
    }

    private void importaciones() throws SintaxException {
        if (nextToken.tipo == AnalizadorLexico.TipoToken.IMPORT){
            importacion();
            importaciones();
        }
    }

    private void importacion() throws SintaxException {
        match(AnalizadorLexico.TipoToken.IMPORT);
        listaIDs();
        match(AnalizadorLexico.TipoToken.P_Y_C);
    }

    private void listaIDs() throws SintaxException {
        identificador();
        restoIDs();
    }

    private void restoIDs() throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.COMA) {
            match(AnalizadorLexico.TipoToken.COMA);
            listaIDs();
        }
    }

    private List<NodoAST> seccionFormulas() throws SintaxException {
        match(AnalizadorLexico.TipoToken.FORMULAS);
        match(AnalizadorLexico.TipoToken.DPUNTOS);
        return listaFBF();
    }

    private List<NodoAST> listaFBF() throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.NOT
        || nextToken.tipo == AnalizadorLexico.TipoToken.LPAREN
        || nextToken.tipo == AnalizadorLexico.TipoToken.TRUE
        || nextToken.tipo == AnalizadorLexico.TipoToken.FALSE
        || nextToken.tipo == AnalizadorLexico.TipoToken.ID) {
            List<NodoAST> listaNodos = new ArrayList<>();
            NodoAST nodo = fbf();
            listaNodos.add(nodo);
            match(AnalizadorLexico.TipoToken.P_Y_C);
            listaNodos.addAll(listaFBF());
            return listaNodos;
        }
        return new ArrayList<>();
    }

    private NodoAST fbf() throws SintaxException {
        return equiv();
    }

    private NodoAST equiv() throws SintaxException {
        NodoAST izq = impl();
        return equiv_R(izq);
    }

    private NodoAST equiv_R(NodoAST izq) throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.EQUIV) {
            int fila = nextToken.fila;
            int columna = nextToken.columna;
            opEquiv();

            NodoAST der = equiv();
            return new NodoEquiv(izq, der).optimizar();
        }
        return izq;
    }

    private void opEquiv() throws SintaxException {
        match(AnalizadorLexico.TipoToken.EQUIV);
    }

    private NodoAST impl() throws SintaxException {
        NodoAST izq = disy();
        return impl_R(izq);
    }

    private NodoAST impl_R(NodoAST izq) throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.IMPL) {
            int fila = nextToken.fila;
            int columna = nextToken.columna;
            opImpl();
            NodoAST der = impl();
            return new NodoImpl(izq, der).optimizar();
        }
        return izq;
    }


    private void opImpl() throws SintaxException {
        match(AnalizadorLexico.TipoToken.IMPL);
    }


    private NodoAST disy() throws SintaxException {
        NodoAST izq = conj();
        return disy_R(izq);
    }

    private NodoAST disy_R(NodoAST izq) throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.OR) {
            opDisy();
            NodoAST der = disy();
            return new NodoOr(izq, der).optimizar();
        }
        return izq;
    }

    private void opDisy() throws SintaxException {
        match(AnalizadorLexico.TipoToken.OR);
    }

    private NodoAST conj() throws SintaxException {
        NodoAST izq = literal();
        return conj_R(izq);
    }

    private NodoAST conj_R(NodoAST izq) throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.AND) {
            opConj();
            NodoAST der = conj();
            return new NodoAnd(izq, der).optimizar();
        }
        return izq;
    }

    private void opConj() throws SintaxException {
        match(AnalizadorLexico.TipoToken.AND);
    }

    private NodoAST literal() throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.NOT) {
            match(AnalizadorLexico.TipoToken.NOT);
            NodoAST conenido = literal();
            return new NodoNot(conenido).optimizar();
        }else if(nextToken.tipo == AnalizadorLexico.TipoToken.LPAREN) {
            match(AnalizadorLexico.TipoToken.LPAREN);
            NodoAST nodo = fbf();
            match(AnalizadorLexico.TipoToken.RPAREN);
            return nodo;
        }else{
            return proposicion();
        }
    }

    private NodoAST proposicion() throws SintaxException {
        if(nextToken.tipo == AnalizadorLexico.TipoToken.TRUE) {
            NodoAST nodo = new NodoBoolean(true);
            match(AnalizadorLexico.TipoToken.TRUE);
            return nodo;
        }else if(nextToken.tipo == AnalizadorLexico.TipoToken.FALSE) {
            NodoAST nodo = new NodoBoolean(false);
            match(AnalizadorLexico.TipoToken.FALSE);
            return nodo;
        }else{
            return identificador();
        }
    }

    private NodoAST identificador() throws SintaxException {
        NodoAST nodo = new NodoID(nextToken.valor);
        match(AnalizadorLexico.TipoToken.ID);
        return nodo;
    }

}
