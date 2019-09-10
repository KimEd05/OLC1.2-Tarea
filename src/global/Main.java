/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import analizadores.Parser;
import analizadores.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author KimEd
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        interpretar("entrada_tarea.txt");
    }
    
    private static void interpretar(String path) throws FileNotFoundException, IOException {
        Stream<String> lines = Files.lines(Paths.get(path));
        String content = lines.collect(Collectors.joining(System.lineSeparator())).replace("||", "¬¬");
        Scanner scanner = new Scanner(new StringReader(content));
        Parser parser = new Parser(scanner);
        Nodo raiz;
        try {
            parser.parse();
            raiz = parser.raiz;
            
            Ejecutor ejecutor = new Ejecutor();
            ejecutor.ejecutar(raiz);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
}
