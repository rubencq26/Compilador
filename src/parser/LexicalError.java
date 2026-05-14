package parser;

public class LexicalError extends Error {

    /**
     * Constante asignada al objeto serializable
     */
    private static final long serialVersionUID = 20170001L;

    /**
     * Car�cter origen del error l�xico
     */
    private char source;

    /**
     * Fila en la que se encuentra el car�cter err�neo
     */
    private int row;

    /**
     * Columna en la que se encuentra el car�cter err�neo
     */
    private int column;

    /**
     * Constructor
     * @param source Car�cter err�neo
     * @param row Fila en la que se encuantra
     * @param column Columna en la que se encuentra
     */
    public LexicalError(char source, int row, int column)
    {
        this.source = source;
        this.row = row;
        this.column = column;
    }

    /**
     * Mensaje de error
     */
    public String toString()
    {
        return "Error lexico: caracter "+source+" [Fila "+row+", Column "+column+"]\n";
    }
}
