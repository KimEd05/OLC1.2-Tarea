/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import java.util.LinkedList;

/**
 *
 * @author KimEd
 */
public class Nodo {
    
    public String etiqueta;
    public String valor;
    public LinkedList<Nodo> hijos;
    public int linea;
    public int columna;

    public Nodo(String etiqueta, String valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
        this.hijos = new LinkedList<>();
        this.linea = -1;
        this.columna = -1;
    }

    public Nodo(String etiqueta, String valor, int linea, int columna) {
        this.etiqueta = etiqueta;
        this.valor = valor;
        this.hijos = new LinkedList<>();
        this.linea = linea;
        this.columna = columna;
    }
    
    public void agregarHijo(Nodo hijo) {
        this.hijos.add(hijo);
    }
    
}
