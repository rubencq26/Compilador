package parser;

import ast.Clausula;
import ast.NodoAST;
import ast.NodoPrograma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Compiler {

    public static void main(String[] args) {
        String path = (args.length == 0) ? System.getProperty("user.dir") : args[0];
        File workingdir = new File(path);

        File mainFile = new File(workingdir, "prueba.txt");

        String fileName = mainFile.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        File outputFile = new File(workingdir, fileName + ".cla");

        try {
            AnalizadorSintactico parser = new AnalizadorSintactico(mainFile);
            NodoAST tree = parser.parse();

            if (tree instanceof NodoPrograma) {
                NodoPrograma programa = (NodoPrograma) tree;

                programa.convertirFNC();


                try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

                    for (NodoAST formula : programa.formulas) {
                        List<Clausula> clausulas = formula.generarClausulas();


                        StringBuilder sb = new StringBuilder();
                        for (Clausula c : clausulas) {
                            sb.append(c.toString());
                        }
                        out.println(sb.toString());
                    }
                }
                System.out.println("Compilación exitosa. Archivo generado: " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error de lectura/escritura: " + e.getMessage());
        } catch (SintaxException e) {
            System.err.println("Error sintáctico: " + e.getMessage());
        }

    }


}