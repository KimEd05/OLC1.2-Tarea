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

/**
 *
 * @author KimEd
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        interpretar("entrada.txt");
    }
    
    private static void interpretar(String path) throws FileNotFoundException {
        File archivoSeleccionado = new File(path);
        Scanner scanner = new Scanner(new FileReader(archivoSeleccionado));
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
