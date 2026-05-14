package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Clausula {
    public List<Literal> literales;

    public Clausula() {
        this.literales = new ArrayList<>();
    }

    public void addLiteral(Literal literal) {
        this.literales.add(literal);
    }


    @Override
    public String toString() {
        String contenido = literales.stream()
                .map(Literal::toString)
                .collect(Collectors.joining(", "));
        return "[" + contenido + "]";
    }
}