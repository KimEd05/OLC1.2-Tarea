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
            case "boolean":
                return Simbolo.EnumTipo.booleano;
            case "char":
                return Simbolo.EnumTipo.caracter;
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
            System.err.println("El tipo que se le quiere asignar a la variable '" + id + "' no es permitido. '" + tipoVariable + "' != '" + resultado.tipo + "'.");
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
        Expresion retorno = null;
        /*
            0   booleano
            1   caracter
            2   entero
            3   doble
            4   cadena
        */
        switch(raiz.etiqueta) {
            case "&&":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                if(hijoIzquierdo.tipo == Simbolo.EnumTipo.booleano && hijoDerecho.tipo == Simbolo.EnumTipo.booleano) {
                    if(Boolean.parseBoolean(hijoIzquierdo.valor.toString()) && Boolean.parseBoolean(hijoDerecho.valor.toString())) {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    }
                } else {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }
            }
                break;
            case "||":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                if(hijoIzquierdo.tipo == Simbolo.EnumTipo.booleano && hijoDerecho.tipo == Simbolo.EnumTipo.booleano) {
                    if(Boolean.parseBoolean(hijoIzquierdo.valor.toString()) || Boolean.parseBoolean(hijoDerecho.valor.toString())) {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    }
                } else {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }
            }
                break;
            case "^":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                if(hijoIzquierdo.tipo == Simbolo.EnumTipo.booleano && hijoDerecho.tipo == Simbolo.EnumTipo.booleano) {
                    if((Boolean.parseBoolean(hijoIzquierdo.valor.toString()) || Boolean.parseBoolean(hijoDerecho.valor.toString())) && !(Boolean.parseBoolean(hijoIzquierdo.valor.toString()) && Boolean.parseBoolean(hijoDerecho.valor.toString()))) {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    }
                } else {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }
            }
                break;
            case "!":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                if(hijoIzquierdo.tipo == Simbolo.EnumTipo.booleano) {
                    if(Boolean.parseBoolean(hijoIzquierdo.valor.toString())) {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    }
                } else {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }
            }
                break;
            case "==":
            {   
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int tipo[][] =  {
                                    {-1, -1, -1, -1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, -1, -1, -1, 1}
                                };
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                if(tipo[fila][columna] == 1) {
                    if(hijoIzquierdo.valor.toString().equals(hijoDerecho.valor.toString())) {
                    retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    }
                } else if(tipo[fila][columna] == -1) {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }   
            }
                break;
            case "!=":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int tipo[][] =  {
                                    {-1, -1, -1, -1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, 1, 1, 1, -1},
                                    {-1, -1, -1, -1, 1}
                                };
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                if(tipo[fila][columna] == 1) {
                    if(!hijoIzquierdo.valor.toString().equals(hijoDerecho.valor.toString())) {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                    } else {
                        retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                    }
                } else if(tipo[fila][columna] == -1) {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }  
                
            }
                break;
            case ">":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                retorno = mayor(fila, columna, hijoIzquierdo, hijoDerecho);
            }
                break;
            case "<":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                retorno = menor(fila, columna, hijoIzquierdo, hijoDerecho);
            }
                break;
            case ">=":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                retorno = mayorigual(fila, columna, hijoIzquierdo, hijoDerecho);
            }
                break;
            case "<=":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                retorno = menorigual(fila, columna, hijoIzquierdo, hijoDerecho);
            }
                break;
            case "+":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);

                int tipo[][] =  {
                                    {-1, -1, -1, -1, 4},
                                    {-1, 2, 2, 3, 4},
                                    {-1, 2, 2, 3, 4},
                                    {-1, 3, 3, 3, 4},
                                    {4, 4, 4, 4, 4}
                                };
                
                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);
                
                retorno = new Expresion(enumTipo(tipo[fila][columna]), suma(fila, columna, hijoIzquierdo, hijoDerecho));
            }
                break;
            case "-":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                
                if(raiz.hijos.size() == 1) {
                    
                } else if(raiz.hijos.size() == 2) {
                    Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                    int tipo[][] =  {
                                    {-1, -1, -1, -1, -1},
                                    {-1, 2, 2, 3, -1},
                                    {-1, 2, 2, 3, -1},
                                    {-1, 3, 3, 3, -1},
                                    {-1, -1, -1, -1, -1}
                                };
                    
                    int fila = intTipo(hijoIzquierdo.tipo);
                    int columna = intTipo(hijoDerecho.tipo);

                    retorno = new Expresion(enumTipo(tipo[fila][columna]), resta(fila, columna, hijoIzquierdo, hijoDerecho));
                }   
            }
                break;
            case "*":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                int tipo[][] =  {
                                {-1, -1, -1, -1, -1},
                                {-1, 2, 2, 3, -1},
                                {-1, 2, 2, 3, -1},
                                {-1, 3, 3, 3, -1},
                                {-1, -1, -1, -1, -1}
                            };

                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);

                retorno = new Expresion(enumTipo(tipo[fila][columna]), multiplicacion(fila, columna, hijoIzquierdo, hijoDerecho));
            }
                break;
            case "/":
            {
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);
                if(!hijoDerecho.valor.toString().toLowerCase().equals("0")) {
                    int tipo[][] =  {
                                        {-1, -1, -1, -1, -1},
                                        {-1, 2, 2, 3, -1},
                                        {-1, 2, 2, 3, -1},
                                        {-1, 3, 3, 3, -1},
                                        {-1, -1, -1, -1, -1}
                                    };

                    int fila = intTipo(hijoIzquierdo.tipo);
                    int columna = intTipo(hijoDerecho.tipo);

                    retorno = new Expresion(enumTipo(tipo[fila][columna]), division(fila, columna, hijoIzquierdo, hijoDerecho));
                } else {
                    retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
                }
            }
                break;
            case "POW":
                Expresion hijoIzquierdo = resolverExpresion(raiz.hijos.get(0), ent);
                Expresion hijoDerecho = resolverExpresion(raiz.hijos.get(1), ent);

                int tipo[][] =  {
                                    {-1, -1, -1, -1, -1},
                                    {-1, 3, 3, 3, -1},
                                    {-1, 3, 3, 3, -1},
                                    {-1, 3, 3, 3, -1},
                                    {-1, -1, -1, -1, -1}
                                };

                int fila = intTipo(hijoIzquierdo.tipo);
                int columna = intTipo(hijoDerecho.tipo);

                retorno = new Expresion(enumTipo(tipo[fila][columna]), potencia(fila, columna, hijoIzquierdo, hijoDerecho));
                break;
            case "BOOLEANO":
            {
                retorno = new Expresion(Simbolo.EnumTipo.booleano, raiz.valor);
            }
                break;
            case "CARACTER":
            {
                retorno = new Expresion(Simbolo.EnumTipo.caracter, raiz.valor.replace("\'", ""));
            }
                break;
            case "ENTERO":
            {
                retorno = new Expresion(Simbolo.EnumTipo.entero, raiz.valor);
            }
                break;
            case "DOBLE":
            {
                retorno = new Expresion(Simbolo.EnumTipo.doble, raiz.valor);
            }
                break;
            case "CADENA":
            {
                retorno = new Expresion(Simbolo.EnumTipo.cadena, raiz.valor);
            }
                break;
            case "ID":
            {
                Simbolo sim = ent.buscar(raiz.valor, raiz.linea, raiz.columna);
                if(sim != null) {
                    retorno = new Expresion(sim.tipo, sim.valor);
                }
            }
                break;
            default:
            {
                retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
            }
                break;
        }
        return retorno;
    }
    
    private int intTipo(Simbolo.EnumTipo type) {
        switch(type.toString().toLowerCase()) {
            case "booleano":
                return 0;
            case "caracter":
                return 1;
            case "entero":
                return 2;
            case "doble":
                return 3;
            case "cadena":
                return 4;
            default:
                return -1;
        }
    }
    
    private Simbolo.EnumTipo enumTipo(int type) {
        switch(type) {
            case 0:
                return Simbolo.EnumTipo.booleano;
            case 1:
                return Simbolo.EnumTipo.caracter;
            case 2:
                return Simbolo.EnumTipo.entero;
            case 3:
                return Simbolo.EnumTipo.doble;
            case 4:
                return Simbolo.EnumTipo.cadena;
            default:
                return Simbolo.EnumTipo.error;
        }
    }
    
    private Expresion menorigual(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Expresion retorno = null;
        int tipo[][] =  {
                            {-1, -1, -1, -1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, -1, -1, -1, -1}
                        };

        if(tipo[fila][columna] == 1) {
            switch(fila) {
                case 1:
                    switch(columna) {
                        case 1:
                            if(hijoIzquierdo.valor.toString().charAt(0) <= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(hijoIzquierdo.valor.toString().charAt(0) <= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(hijoIzquierdo.valor.toString().charAt(0) <= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch(columna) {
                        case 1:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) <= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) <= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) <= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 3:
                    switch(columna) {
                        case 1:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) <= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) <= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) <= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
            }

        } else if(tipo[fila][columna] == -1) {
            retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
        }
        return retorno;
    }
    
    private Expresion mayorigual(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Expresion retorno = null;
        int tipo[][] =  {
                            {-1, -1, -1, -1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, -1, -1, -1, -1}
                        };

        if(tipo[fila][columna] == 1) {
            switch(fila) {
                case 1:
                    switch(columna) {
                        case 1:
                            if(hijoIzquierdo.valor.toString().charAt(0) >= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(hijoIzquierdo.valor.toString().charAt(0) >= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(hijoIzquierdo.valor.toString().charAt(0) >= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch(columna) {
                        case 1:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) >= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) >= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) >= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 3:
                    switch(columna) {
                        case 1:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) >= hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) >= Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) >= Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
            }

        } else if(tipo[fila][columna] == -1) {
            retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
        }
        return retorno;
    }
    
    private Expresion menor(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Expresion retorno = null;
        int tipo[][] =  {
                            {-1, -1, -1, -1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, -1, -1, -1, -1}
                        };

        if(tipo[fila][columna] == 1) {
            switch(fila) {
                case 1:
                    switch(columna) {
                        case 1:
                            if(hijoIzquierdo.valor.toString().charAt(0) < hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(hijoIzquierdo.valor.toString().charAt(0) < Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(hijoIzquierdo.valor.toString().charAt(0) < Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch(columna) {
                        case 1:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) < hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) < Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) < Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 3:
                    switch(columna) {
                        case 1:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) < hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) < Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) < Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
            }

        } else if(tipo[fila][columna] == -1) {
            retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
        }
        return retorno;
    }
    
    private Expresion mayor(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Expresion retorno = null;
        int tipo[][] =  {
                            {-1, -1, -1, -1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, 1, 1, 1, -1},
                            {-1, -1, -1, -1, -1}
                        };

        if(tipo[fila][columna] == 1) {
            switch(fila) {
                case 1:
                    switch(columna) {
                        case 1:
                            if(hijoIzquierdo.valor.toString().charAt(0) > hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(hijoIzquierdo.valor.toString().charAt(0) > Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(hijoIzquierdo.valor.toString().charAt(0) > Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch(columna) {
                        case 1:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) > hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) > Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Integer.parseInt(hijoIzquierdo.valor.toString()) > Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
                case 3:
                    switch(columna) {
                        case 1:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) > hijoDerecho.valor.toString().charAt(0)) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 2:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) > Integer.parseInt(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                        case 3:
                            if(Double.parseDouble(hijoIzquierdo.valor.toString()) > Double.parseDouble(hijoDerecho.valor.toString())) {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "true");
                            } else {
                                retorno = new Expresion(Simbolo.EnumTipo.booleano, "false");
                            }
                            break;
                    }
                    break;
            }

        } else if(tipo[fila][columna] == -1) {
            retorno = new Expresion(Simbolo.EnumTipo.error, "Error");
        }
        return retorno;
    }
    
    private Object potencia(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Object retorno = new Object();
        switch(fila) {
            case 0:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 1:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = (double)Math.pow(hijoIzquierdo.valor.toString().charAt(0), hijoDerecho.valor.toString().charAt(0));
                        break;
                    case 2:
                        retorno = (double)Math.pow(hijoIzquierdo.valor.toString().charAt(0), Integer.parseInt(hijoDerecho.valor.toString()));
                        break;
                    case 3:
                        retorno = (double)Math.pow(hijoIzquierdo.valor.toString().charAt(0), Double.parseDouble(hijoDerecho.valor.toString()));
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 2:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = (double)Math.pow(Integer.parseInt(hijoIzquierdo.valor.toString()), hijoDerecho.valor.toString().charAt(0));
                        break;
                    case 2:
                        retorno = (double)Math.pow(Integer.parseInt(hijoIzquierdo.valor.toString()), Integer.parseInt(hijoDerecho.valor.toString()));
                        break;
                    case 3:
                        retorno = (double)Math.pow(Integer.parseInt(hijoIzquierdo.valor.toString()), Double.parseDouble(hijoDerecho.valor.toString()));
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 3:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = (double)Math.pow(Double.parseDouble(hijoIzquierdo.valor.toString()),hijoDerecho.valor.toString().charAt(0));
                        break;
                    case 2:
                        retorno = (double)Math.pow(Double.parseDouble(hijoIzquierdo.valor.toString()),Integer.parseInt(hijoDerecho.valor.toString()));
                        break;
                    case 3:
                        retorno = (double)Math.pow(Double.parseDouble(hijoIzquierdo.valor.toString()),Double.parseDouble(hijoDerecho.valor.toString()));
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 4:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
        }
        return retorno;
    }
    
    private Object division(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Object retorno = new Object();
        switch(fila) {
            case 0:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 1:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) / hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) / Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) / Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 2:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) / hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) / Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) / Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 3:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) / hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) / Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) / Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 4:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
        }
        return retorno;
    }
    
    private Object multiplicacion(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Object retorno = new Object();
        switch(fila) {
            case 0:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 1:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) * hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) * Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) * Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 2:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) * hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) * Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) * Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 3:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) * hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) * Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) * Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
            case 4:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = "";
                        break;
                }
                break;
        }
        return retorno;
    }
    
    private Object resta(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Object retorno = new Object();
        if(hijoDerecho != null) { 
            switch(fila) {
                case 0:
                    switch(columna) {
                        case 0:
                            retorno = "";
                            break;
                        case 1:
                            retorno = "";
                            break;
                        case 2:
                            retorno = "";
                            break;
                        case 3:
                            retorno = "";
                            break;
                        case 4:
                            retorno = "";
                            break;
                    }
                    break;
                case 1:
                    switch(columna) {
                        case 0:
                            retorno = "";
                            break;
                        case 1:
                            retorno = hijoIzquierdo.valor.toString().charAt(0) - hijoDerecho.valor.toString().charAt(0);
                            break;
                        case 2:
                            retorno = hijoIzquierdo.valor.toString().charAt(0) - Integer.parseInt(hijoDerecho.valor.toString());
                            break;
                        case 3:
                            retorno = hijoIzquierdo.valor.toString().charAt(0) - Double.parseDouble(hijoDerecho.valor.toString());
                            break;
                        case 4:
                            retorno = "";
                            break;
                    }
                    break;
                case 2:
                    switch(columna) {
                        case 0:
                            retorno = "";
                            break;
                        case 1:
                            retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) - hijoDerecho.valor.toString().charAt(0);
                            break;
                        case 2:
                            retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) - Integer.parseInt(hijoDerecho.valor.toString());
                            break;
                        case 3:
                            retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) - Double.parseDouble(hijoDerecho.valor.toString());
                            break;
                        case 4:
                            retorno = "";
                            break;
                    }
                    break;
                case 3:
                    switch(columna) {
                        case 0:
                            retorno = "";
                            break;
                        case 1:
                            retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) - hijoDerecho.valor.toString().charAt(0);
                            break;
                        case 2:
                            retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) - Integer.parseInt(hijoDerecho.valor.toString());
                            break;
                        case 3:
                            retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) - Double.parseDouble(hijoDerecho.valor.toString());
                            break;
                        case 4:
                            retorno = "";
                            break;
                    }
                    break;
                case 4:
                    switch(columna) {
                        case 0:
                            retorno = "";
                            break;
                        case 1:
                            retorno = "";
                            break;
                        case 2:
                            retorno = "";
                            break;
                        case 3:
                            retorno = "";
                            break;
                        case 4:
                            retorno = "";
                            break;
                    }
                    break;
            }
        } else if(hijoDerecho == null){
        
        }
        return retorno;
    }
    
    private Object suma(int fila, int columna, Expresion hijoIzquierdo, Expresion hijoDerecho) {
        Object retorno = new Object();
        switch(fila) {
            case 0:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = "";
                        break;
                    case 2:
                        retorno = "";
                        break;
                    case 3:
                        retorno = "";
                        break;
                    case 4:
                        retorno = Boolean.parseBoolean(hijoIzquierdo.valor.toString()) + (String)hijoDerecho.valor;
                        break;
                }
                break;
            case 1:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) + hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) + Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) + Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = hijoIzquierdo.valor.toString().charAt(0) + (String)hijoDerecho.valor;
                        break;
                }
                break;
            case 2:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) + hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) + Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) + Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = Integer.parseInt(hijoIzquierdo.valor.toString()) + (String)hijoDerecho.valor;
                        break;
                }
                break;
            case 3:
                switch(columna) {
                    case 0:
                        retorno = "";
                        break;
                    case 1:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) + hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) + Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) + Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = Double.parseDouble(hijoIzquierdo.valor.toString()) + (String)hijoDerecho.valor;
                        break;
                }
                break;
            case 4:
                switch(columna) {
                    case 0:
                        retorno = (String)hijoIzquierdo.valor + Boolean.parseBoolean(hijoDerecho.valor.toString());
                        break;
                    case 1:
                        retorno = (String)hijoIzquierdo.valor + hijoDerecho.valor.toString().charAt(0);
                        break;
                    case 2:
                        retorno = (String)hijoIzquierdo.valor + Integer.parseInt(hijoDerecho.valor.toString());
                        break;
                    case 3:
                        retorno = (String)hijoIzquierdo.valor + Double.parseDouble(hijoDerecho.valor.toString());
                        break;
                    case 4:
                        retorno = (String)hijoIzquierdo.valor + (String)hijoDerecho.valor;
                        break;
                }
                break;
        }
        
        return retorno;
    }
    
}
