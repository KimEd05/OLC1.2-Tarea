/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

/**
 *
 * @author KimEd
 */
public class Ejecutor {
    
    public void ejecutar(Nodo raiz) {
        recorrer(raiz, null);
    }
    
    private void recorrer(Nodo raiz, Entorno ent) {
    
        switch(raiz.etiqueta) {
            case "S":
                recorrer(raiz.hijos.get(0), ent);
                break;
            case "BLOQUE":
                if(raiz.hijos.size() == 1) {
                    Entorno nuevoEntorno = new Entorno(ent);
                    recorrer(raiz.hijos.get(0), nuevoEntorno);
                }
                break;
            case "L_INSTRUCCIONES":
                if(raiz.hijos.size() == 1) {
                    recorrer(raiz.hijos.get(0), ent);
                } else {
                    recorrer(raiz.hijos.get(0), ent);
                    recorrer(raiz.hijos.get(1), ent);
                }
                break;
            case "DECLARACION":
                ejecutarDeclaracion(raiz, ent);
                break;
            case "IMPRIMIR":
                ejecutarImprimir(raiz, ent);
                break;
            case "REPETIR":
                ejecutarRepetir(raiz, ent);
                break;
        }
      
    }
    
    private Simbolo.EnumTipo obtenerTipo(String tipo) {
        switch(tipo.toLowerCase()) {
            case "int":
                return Simbolo.EnumTipo.entero;
            case "double":
                return Simbolo.EnumTipo.doble;
            case "string":
                return Simbolo.EnumTipo.cadena;
            default:
                return Simbolo.EnumTipo.error;
        }
    }
    
    private void ejecutarDeclaracion(Nodo raiz, Entorno ent) {
        Expresion resultado = resolverExpresion(raiz.hijos.get(2), ent);
        
        Simbolo.EnumTipo tipoVariable = obtenerTipo(raiz.hijos.get(0).valor);
        String id = raiz.hijos.get(1).valor;
        
        if(tipoVariable != resultado.tipo) {
            System.err.println("El tipo que se le quiere asignar a la variable '" + id + "' no es permitido. '" + tipoVariable + "' = '" + resultado.tipo + "'.");
            return;
        }
        
        Simbolo nuevo = new Simbolo(tipoVariable, resultado.valor);
        ent.insertar(id, nuevo, raiz.linea, raiz.columna);
    }
    
    private void ejecutarImprimir(Nodo raiz, Entorno ent) {
        Expresion resultado = resolverExpresion(raiz.hijos.get(0), ent);
        
        if(resultado.tipo != Simbolo.EnumTipo.error) {
            System.out.println(resultado.valor);
        }
    }
    
    private void ejecutarRepetir(Nodo raiz, Entorno ent) {
        Expresion resultado = resolverExpresion(raiz.hijos.get(0), ent);
        
        if(resultado.tipo != Simbolo.EnumTipo.entero) {
            System.err.println("El parámetro de la función repetir no es de tipo int, es de tipo '" + resultado.tipo + "'. Fila: " + raiz.linea + " Columna: " + raiz.columna + ".");
            return;
        }
        
        Nodo nodoBloque = raiz.hijos.get(1);
        
        if(nodoBloque.hijos.size() > 0) {
            int iterador = Integer.parseInt(resultado.valor.toString());
            while(iterador > 0) {
                Entorno local = new Entorno(ent);
                recorrer(nodoBloque.hijos.get(0), local);
                iterador--;
            }
        }
    }
    
    private Expresion resolverExpresion(Nodo raiz, Entorno ent) {
    
        switch(raiz.etiqueta) {
            case "ENTERO":
                return new Expresion(Simbolo.EnumTipo.entero, raiz.valor);
            case "DOBLE":
                return new Expresion(Simbolo.EnumTipo.doble, raiz.valor);
            case "CADENA":
                return new Expresion(Simbolo.EnumTipo.cadena, raiz.valor);
            case "ID":
                Simbolo sim = ent.buscar(raiz.valor, raiz.linea, raiz.columna);
                if(sim != null) {
                    return new Expresion(sim.tipo, sim.valor);
                }
                break;
        }
        return new Expresion(Simbolo.EnumTipo.error, "Error");
    }
    
}
