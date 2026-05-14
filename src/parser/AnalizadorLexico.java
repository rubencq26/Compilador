package parser;

import java.io.File;
import java.io.IOException;

public class AnalizadorLexico {

    enum TipoToken {
        NAME, P_Y_C, IMPORT, COMA, DPUNTOS, FORMULAS, NOT, LPAREN, RPAREN, EQUIV, IMPL, OR, AND, TRUE, FALSE, ID, EOF
    }

    static class Token {
        TipoToken tipo;
        String valor;
        int fila, columna;

        Token(TipoToken tipo, String valor, int fila, int columna) {
            this.tipo = tipo;
            this.valor = valor;
            this.fila = fila;
            this.columna = columna;
        }

        @Override
        public String toString() {
            return String.format("(%s, %s , %d, %d)\n", tipo, valor, fila, columna);
        }

    }

    private BufferedCharStream stream;

    public AnalizadorLexico(File file) throws IOException {
        this.stream = new BufferedCharStream(file);
    }

    public Token getNextToken() {
        return tokenize();
    }

    public void close() {
        this.stream.close();
    }

    private Token tokenize() { // <--- Recibe el carácter aquí
        int finalState = -1;


        char newChar = stream.getNextChar();
        while (newChar == ' ' || newChar == '\t' || newChar == '\n' || newChar == '\r') {
            newChar = stream.getNextChar();
        }

        if (newChar == '\0') {
            return new Token(TipoToken.EOF, "", stream.getRow(), stream.getColumn());
        }

        StringBuffer lexeme = new StringBuffer();
        StringBuffer tainting = new StringBuffer();

        int state = transicion(0, newChar);
        int row = stream.getRow();
        int column = stream.getColumn();

        while (state != -1 && newChar != '\0') {
            tainting.append(newChar);
            if (esFinal(state)) {
                finalState = state;
                lexeme.append(tainting);
                tainting.delete(0, tainting.length());
            }
            newChar = stream.getNextChar();
            state = transicion(state, newChar);
        }

        if (finalState != -1) {
            // Retrocedemos el carácter que "mató" el autómata y lo que haya en tainting
            stream.retract(1 + tainting.length());
            return getToken(finalState, lexeme.toString(), row + 1, column);
        } else {
            throw new LexicalError(newChar, row, column);
        }
    }


    protected int transicion(int state, char symbol) {
        switch (state) {
            case 0:
                if (symbol == 'n') return 1; //name
                else if (symbol == ';') return 2;
                else if (symbol == 'i') return 3; //import impl
                else if (symbol == ',') return 4;
                else if (symbol == ':') return 5;
                else if (symbol == 'f') return 6; //formulas false
                else if (symbol == '~') return 7;
                else if (symbol == '(') return 8;
                else if (symbol == ')') return 9;
                else if (symbol == 'e') return 10; //equiv
                else if (symbol == '<') return 11; // <->
                else if (symbol == '-') return 12; // ->
                else if (symbol == 'o') return 13; // or
                else if (symbol == 'v') return 14;
                else if (symbol == 'a') return 15; // and
                else if (symbol == '^') return 16;
                else if (symbol == 't') return 17; // true
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z')) return 18;
                else return -1;
            case 1:
                if (symbol == 'a') return 20; //name
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 3:
                if (symbol == 'm') return 21; //import impl
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 6:
                if (symbol == 'o') return 22; // formulas
                else if (symbol == 'a') return 23; // false
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 10:
                if (symbol == 'q') return 24; //equiv
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;

            case 11:
                if (symbol == '-') return 25; // <->
                else return -1;
            case 12:
                if (symbol == '>') return 26; // fin ->
                else return -1;
            case 13:
                if (symbol == 'r') return 27; // fin or
                else return -1;
            case 15:
                if (symbol == 'n') return 28; // and
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 17:
                if (symbol == 'r') return 29; //true
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 18:
                if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 20:
                if (symbol == 'm') return 30; //name
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 21:
                if (symbol == 'p') return 31; //import impl
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 22:
                if (symbol == 'r') return 32; //formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 23:
                if (symbol == 'l') return 33; //false
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 24:
                if (symbol == 'u') return 34;  //equiv
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 25:
                if (symbol == '>') return 35; // fin <->
                else return -1;
            case 28:
                if (symbol == 'd') return 36; //fin and
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 29:
                if (symbol == 'u') return 37; //true
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 30:
                if (symbol == 'e') return 38; //fin name
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 31:
                if (symbol == 'o') return 39; //import
                else if (symbol == 'l') return 40; // fin impl
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 32:
                if (symbol == 'm') return 41; //formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 33:
                if (symbol == 's') return 42; //false
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 34:
                if (symbol == 'i') return 43; //equiv
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 37:
                if (symbol == 'e') return 44; //fin true
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 39:
                if (symbol == 'r') return 45; //import
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 41:
                if (symbol == 'u') return 46; //formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 42:
                if (symbol == 'e') return 47; //fin false
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 43:
                if (symbol == 'v') return 48; //fin equiv;
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 45:
                if (symbol == 't') return 49; //fin import
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 46:
                if (symbol == 'l') return 50; //formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 50:
                if (symbol == 'a') return 51; //formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 51:
                if (symbol == 's') return 52; //fin formulas
                else if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;
                else return -1;
            case 14:
            case 27:
            case 36:
            case 38:
            case 40:
            case 44:
            case 47:
            case 48:
            case 49:
            case 52:
                if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9'))
                    return 18;

                else return -1;
            default:
                return -1;
        }
    }

    protected boolean esFinal(int state) {
        switch (state) {
            case 2: //<P_Y_C>
            case 4: //<COMA>
            case 5: //<DPUNTOS>
            case 7: //<NOT>
            case 8: //<LPAREN>
            case 9: //<RPAREN>
            case 14: //<OR>
            case 16: //<AND>
            case 1:
            case 20:
            case 30:
            case 15:
            case 17:
            case 29:
            case 37:
            case 3:
            case 21:
            case 31:
            case 39:
            case 45:
            case 6:
            case 22:
            case 23:
            case 32:
            case 33:
            case 41:
            case 42:
            case 46:
            case 50:
            case 51:
            case 10:
            case 24:
            case 34:
            case 43:
            case 13:
            case 28:
            case 18: //<ID>
            case 26: //<IMP>
            case 27: //<OR>
            case 35: //<EQUIV>
            case 36: //<AND>
            case 38: //<NAME>
            case 40: //<IMPL>
            case 44: //<TRUE>
            case 47: //<FALSE>
            case 48: //<EQUIV>
            case 49: //<IMPORT>
            case 52: //<FORMULAS>
                return true;
            default:
                return false;
        }
    }

    private Token getToken(int state, String lexema, int row, int column) {
        switch (state) {
            case 2:
                return new Token(TipoToken.P_Y_C, lexema, row, column);
            case 4:
                return new Token(TipoToken.COMA, lexema, row, column);
            case 5:
                return new Token(TipoToken.DPUNTOS, lexema, row, column);
            case 7:
                return new Token(TipoToken.NOT, lexema, row, column);
            case 8:
                return new Token(TipoToken.LPAREN, lexema, row, column);
            case 9:
                return new Token(TipoToken.RPAREN, lexema, row, column);
            case 14:
                return new Token(TipoToken.OR, lexema, row, column);
            case 16:
                return new Token(TipoToken.AND, lexema, row, column);
            case 1:
            case 20:
            case 30:
            case 15:
            case 17:
            case 29:
            case 37:
            case 3:
            case 21:
            case 31:
            case 39:
            case 45:
            case 6:
            case 22:
            case 23:
            case 32:
            case 33:
            case 41:
            case 42:
            case 46:
            case 50:
            case 51:
            case 10:
            case 24:
            case 34:
            case 43:
            case 13:
            case 28:
            case 18:
                return new Token(TipoToken.ID, lexema, row, column);
            case 26:
                return new Token(TipoToken.IMPL, lexema, row, column);
            case 27:
                return new Token(TipoToken.OR, lexema, row, column);
            case 35:
                return new Token(TipoToken.EQUIV, lexema, row, column);
            case 36:
                return new Token(TipoToken.AND, lexema, row, column);
            case 38:
                return new Token(TipoToken.NAME, lexema, row, column);
            case 40:
                return new Token(TipoToken.IMPL, lexema, row, column);
            case 44:
                return new Token(TipoToken.TRUE, lexema, row, column);
            case 47:
                return new Token(TipoToken.FALSE, lexema, row, column);
            case 48:
                return new Token(TipoToken.EQUIV, lexema, row, column);
            case 49:
                return new Token(TipoToken.IMPORT, lexema, row, column);
            case 52:
                return new Token(TipoToken.FORMULAS, lexema, row, column);
            default:
                return null;
        }
    }

}
