package ast;

import java.util.List;
import java.util.Objects;

public class NodoPrograma extends NodoAST{
    public NodoAST nombre;
    public List<NodoAST> formulas;

    public NodoPrograma(NodoAST nombre, List<NodoAST> formulas) {
        super();
        this.nombre = nombre;
        this.formulas = formulas;
    }

    // En NodoPrograma.java
    @Override
    public void print(String prefix) {
        System.out.print("PROGRAMA: ");
        nombre.print(""); // El nombre suele ser un NodoID
        System.out.println("FORMULAS:");
        for (NodoAST f : formulas) {
            f.print("  ");
            System.out.println("  ----------------");
        }
    }

    @Override
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMiId()).append(" [label=\"Programa\", shape=folder, fillcolor=lightblue, style=filled];\n");

        // Nombre del sistema
        sb.append(nombre.toDot());
        sb.append(getMiId()).append(" -> ").append(nombre.getMiId()).append(" [label=\"nombre\"];\n");

        // Fórmulas
        for (NodoAST f : formulas) {
            sb.append(f.toDot());
            sb.append(getMiId()).append(" -> ").append(f.getMiId()).append(" [label=\"fórmula\"];\n");
        }
        return sb.toString();
    }

    @Override
    public NodoAST optimizar() {
        return this;
    }

    @Override
    public NodoAST convertirFNC() {
        for (int i = 0; i < formulas.size(); i++) {
            formulas.set(i, formulas.get(i).convertirFNC());
        }
        return this;
    }

    @Override
    public List<Clausula> generarClausulas() { return null; }

    @Override
    public void extraerLiterales(Clausula c) {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodoPrograma that = (NodoPrograma) o;
        return Objects.equals(nombre, that.nombre) && Objects.equals(formulas, that.formulas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, formulas);
    }
}
