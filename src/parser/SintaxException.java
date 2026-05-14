package parser;

public class SintaxException extends Exception {
    private String msg;

    public SintaxException(AnalizadorLexico.Token token, AnalizadorLexico.TipoToken kind) {

        this.msg = "Sintax exception at row "+token.fila;
        msg += ", column "+token.columna+".\n";
        msg += "  Found "+token.valor+"\n";
        msg += "  while expecting "+ kind +".\n";
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
