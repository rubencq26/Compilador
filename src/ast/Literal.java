package ast;

import java.util.Objects;

public class Literal {
    public String nombre;
    public boolean estaNegado;

    public Literal(String nombre, boolean estaNegado){
        this.nombre = nombre;
        this.estaNegado = estaNegado;
    }

    @Override
    public String toString() {
        return(estaNegado ? "~" : "") + nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return estaNegado == literal.estaNegado && Objects.equals(nombre, literal.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, estaNegado);
    }

}
