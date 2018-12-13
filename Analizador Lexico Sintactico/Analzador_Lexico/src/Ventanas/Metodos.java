
package Ventanas;

import Ventanas.Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;




public class Metodos {

    public static String desconocido = "", CadenaExaminar = "";
    DefaultTableModel modelo = (DefaultTableModel) Main.tblDatos.getModel();
    Object[] fila = new Object[2];
    int contadorsintactico = 0, contadorid = 0;
    String[] sintactico;
    String[] identificadores;
    String[] datosentrada;

    void proceso() throws IOException {

        //Limpiamos La Tabla Para Poder Almacenar Nuevos Datos Y Que No Se Concatenen
        for (int i = 0; i < Main.tblDatos.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
        //////////////////////////////////////////////////////////////////////////////

        //Carga los tokens desde un archivo de texto externo y lo almacena en un vector llamado Datos
        //El documento de los tokens esta contituido por el token y funcion, "; Fin_linea" si la funcion es de mas de una palbra usar _ 
        //para hacer una separacion. Ya que cuando almacena los tokens los almacena por posicion separandolos por un espacio
        String strDatos;
        FileReader fr;

        fr = new FileReader("C:\\Users\\jose_\\Downloads\\Analizador_Lexico_Sintactico_Semantico\\Analzador_Lexico\\tokens.txt");
        BufferedReader br = new BufferedReader(fr);
        strDatos = br.readLine();

        int numTokens = 0;

        StringTokenizer tokens = new StringTokenizer(strDatos, " ");
        int i = 0;
        int nDatos = tokens.countTokens();
        String[] datos = new String[nDatos];
        while (tokens.hasMoreTokens()) {
            String str = tokens.nextToken();
            datos[i] = str;

            i++;
        }
        ///////////////////////////////////////////////////////////////////////////////////

        ///Los datos que son ingresados como codigo lo almacena en otro vector, cada simbolo almacenado en un espacio o un salto de linea
        StringTokenizer tokencadenaentrada = new StringTokenizer(CadenaExaminar, " \n");
        int nDatosentrada = tokencadenaentrada.countTokens();
        datosentrada = new String[nDatosentrada];
        int j = 0;
        while (tokencadenaentrada.hasMoreTokens()) {
            String str = tokencadenaentrada.nextToken();
            datosentrada[j] = str;
            j++;
        }
        ////////////////////////////////////////////////////////////////////////

        sintactico = new String[nDatosentrada];
        //Comprobacion de los token y sus respectivas funciones, ademas hay llamado a funciones que se encargan
        // de identificar si son numeros, letras o decimales
        for (int k = 0; k < nDatosentrada; k++) {
            int aux = 0;

            for (int l = 0; l < nDatos; l++) {
                if (datosentrada[k].equals(datos[l])) {
                    fila[0] = datos[l + 1]; //identificador
                    fila[1] = datos[l]; //token
                    modelo.addRow(fila);
                    Main.tblDatos.setModel(modelo);
                    aux = 1;
                    sintactico[contadorsintactico] = datos[l + 1];
                    contadorsintactico++;
                    break;
                } else if (numero(datosentrada[k]) == true) {
                    fila[0] = "Tipo_Dato:var: ";
                    fila[1] = datosentrada[k];
                    modelo.addRow(fila);
                    Main.tblDatos.setModel(modelo);
                    aux = 1;
                    sintactico[contadorsintactico] = "Numero";
                    contadorsintactico++;
                    break;
                } else if (decimal(datosentrada[k]) == true) {
                    fila[0] = "Tipo_Dato:Float: ";
                    fila[1] = datosentrada[k];
                    modelo.addRow(fila);
                    Main.tblDatos.setModel(modelo);
                    aux = 1;
                    sintactico[contadorsintactico] = "Decimal";
                    contadorsintactico++;
                    break;
                } else if (cadena(datosentrada[k]) == true) {
                    fila[0] = "Tipo_Dato:Cadena: ";
                    fila[1] = datosentrada[k];
                    // System.out.println("se encontro este elemento entre comillas"+datosentrada[k]);
                    modelo.addRow(fila);
                    Main.tblDatos.setModel(modelo);
                    aux = 1;
                    sintactico[contadorsintactico] = "Cadena";
                    contadorsintactico++;
                    break;

                }

            }
            if (aux == 0) {
                if (ID(datosentrada[k]) == true) {
                    fila[0] = "ID: ";
                    fila[1] = datosentrada[k];
                    modelo.addRow(fila);
                    Main.tblDatos.setModel(modelo);
                    //Main.jButton2.setEnabled(true);
                    sintactico[contadorsintactico] = "ID";
                    contadorsintactico++;
                } else {
                    // System.out.println("Simbolo " + datosentrada[k] + " en la posicion " + k + " desconocido ");
                    fila[0] = "Simbolo Desconocido: ";
                    fila[1] = datosentrada[k] + " en posicion: " + (k + 1);
                    modelo.addRow(fila);

                    Main.tblDatos.setModel(modelo);
                    desconocido = datosentrada[k];
                    //TokenDesconocido a = new TokenDesconocido();
                    //a.setVisible(true);
                    //1a.jTextField1.setText(desconocido);
                    Errores = Errores + "Simbolo Desconocido: " + datosentrada[k] + " en posicion: " + (k + 1) + "\n";
                    if (true) {
                        
                              JOptionPane.showMessageDialog(null, "Respeta los espacios!! \n\n De lo contrario se presenta un Error ");
                    Errores = Errores + "RESPETA LOS ESPACIOS :(\n\n";
                    }
              
                }
            }
        }
        sintaxis();

    }

    ////////////////////////////////////////////////////////////////////////////
    //Comprueba orden de los simbolos para saber si esta correcto.
    int linea = 1;
    String vector[] = new String[5];
    int cv = 0, cc = 0, ccc = 0, cccc = 0, cmain = 0, cfor=0, cif=0;
    String id = "", id2 = "", id3 = "", id4 = "", id5 ="", signo = "";
    
    void sintaxis() {

        int error = 0;

        //Estructuras de control que validan la sintaxis de operaciones con entero-enteros
        //System.out.println("errrerw"+datosentrada[4]);
        try {

            //Evalua declaracion de variables
            for (int i = 0; i < sintactico.length; i++) {
                System.out.println(datosentrada[i+4]);
                System.out.println(sintactico[i+5]);
                System.out.println(datosentrada[i+6]);
                System.out.println(datosentrada[i+7]);
                System.out.println(datosentrada[i+8]);
                /////////////validar cadena en una sola declaracion
               
                //////////// validacion de Float
                if (((datosentrada[i + 4].matches("^Float")) & datosentrada[i + 4].equals("Float"))) {
                    //  System.out.println("Se manejara Floats.....");
                    try {

                        if (sintactico[i].equals("Funcion_Princ")) {
                            if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                                if (sintactico[i + 2].equals("Funcion_Cerramos")) {
                                    if (sintactico[i + 3].equals("LLave_Izquierda")) {
                                        cmain = 1;
                                        linea++;
                                    } else {
                                        Errores = Errores + "Falta {, linea: " + linea + "\n";
                                    }
                                } else {
                                    Errores = Errores + "Falta ), linea: " + linea + "\n";
                                }
                            } else {
                                //Errores=Errores+"Falta (, linea: "+linea+"\n";
                            }
                        } else {
                            //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                        }
                    } catch (Exception e) {
                    }

                    /////////////////////////////////
                    try {

                        if (sintactico[4].matches("^Float") || sintactico[4].equals("Float")) {
                            //System.out.println("PASO AQUI ESTEVALKORADSA:"+ sintactico[4]);
                            // if (sintactico[i].equals("Tipo_DatoDbl")) {

                            if (sintactico[i + 1].equals("ID")) {
                                if (sintactico[i + 2].equals("Fin_linea")) {

                                } else {
                                    //System.err.println("Se espraba =, linea: "+linea);
                                    Errores = Errores + "Se espraba =, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba ID, linea: "+linea);
                                Errores = Errores + "Se espraba ID, linea :): " + linea + "\n";
                            }
                            /// }//tipo de dato double
                        } else {
                            // Errores=Errores+"Errores de declaracion: No bien declarada, simbolo no reconocido.\n" ;
                        }
                    } catch (Exception e) {
                        //System.err.println("No se encontro fin de linea, linea: "+linea);
                        Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                    }

                    try {

                        if (sintactico[i + 7].equals("ID")) {
                            //  System.out.println("este es ID: "+sintactico[i+7]);
                            if (sintactico[i + 8].equals("Funcion_Asignacion")) {
                                //   System.out.println("Fun asignacion "+sintactico[i+8]);
                                if (sintactico[i + 9].equals("Decimal") || sintactico[i + 9].equals("Numero")) {
                                    //    System.out.println("tipo de datos Float "+sintactico[i+9]);
                                    if (sintactico[i + 10].equals("Fin_linea")) {
                                        //      System.out.println("Fin de la line"+sintactico[i+10]);
                                        Errores = Errores + "Ejecucion Exitosa!!! \n ============================================";

                                    } else {
                                        Errores = Errores + "Error de simbolo: no se puede utilizar en una declaracion Boolean!!!\n";
                                    }

                                } else {
                                    Errores = Errores + "Error de tipo de dato: no compatible con Float!\n";

                                }

                            } else {
                                Errores = Errores + "Falta la asignacion\n";

                            }

                        } else {
                            Errores = Errores + "No se reconoce el Identificardor" + sintactico[i + 7];

                        }

                    } catch (Exception E) {

                    }
                } else {
                    //System.out.println("Fallo este...");
                }

                /////////////////////fin de la comprobacion Float
                //inicio de la comprobacion de la comparacion booleana
                if (((datosentrada[i + 4].matches("^Boolean")))) {
                    //  System.out.println("Se hara una comparacion entre dos valores.....");
                    try {

                        if (sintactico[i].equals("Funcion_Princ")) {
                            if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                                if (sintactico[i + 2].equals("Funcion_Cerramos")) {
                                    if (sintactico[i + 3].equals("LLave_Izquierda")) {
                                        cmain = 1;
                                        linea++;
                                    } else {
                                        Errores = Errores + "Falta {, linea: " + linea + "\n";
                                    }
                                } else {
                                    Errores = Errores + "Falta ), linea: " + linea + "\n";
                                }
                            } else {
                                //Errores=Errores+"Falta (, linea: "+linea+"\n";
                            }
                        } else {
                            //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                        }
                    } catch (Exception e) {
                    }

                    /////////////////////////////////
                    try {
                        if (sintactico[i].equals("Tipo_DatoB") & sintactico[i].matches("^Boolean") || sintactico[i].equals("Numero")) {

                            // if (sintactico[i].equals("Tipo_DatoDbl")) {
                            if (sintactico[i + 1].equals("ID")) {
                                if (sintactico[i + 2].equals("Fin_linea")) {

                                } else {
                                    //System.err.println("Se espraba =, linea: "+linea);
                                    Errores = Errores + "Se espraba =, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba ID, linea: "+linea);
                                Errores = Errores + "Se espraba ID, linea :): " + linea + "\n";
                            }
                            /// }//tipo de dato double
                        } else {
                            Errores = Errores + "Errores de declaracion: No bien declarada, simbolo no reconocido.\n";
                        }
                    } catch (Exception e) {
                        //System.err.println("No se encontro fin de linea, linea: "+linea);
                        Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                    }

                    try {

                        if (sintactico[i + 7].equals("ID")) {
                            //  System.out.println("este es ID: "+sintactico[i+7]);
                            if (sintactico[i + 8].equals("Funcion_Asignacion")) {
                                //   System.out.println("Fun asignacion "+sintactico[i+8]);
                                if (sintactico[i + 9].equals("Numero") || sintactico[i + 11].equals("Decimal")) {
                                    //    System.out.println("tipo de datos entero "+sintactico[i+9]);
                                    if (sintactico[i + 10].equals("Funcion_menorque") || sintactico[i + 10].equals("Funcion_mayorQue") || sintactico[i + 10].equals("Funcion_MayorIgualQue") || sintactico[i + 10].equals("Funcion_MenorIgualQue")) {
                                        //      System.out.println("comparacion "+sintactico[i+10]);
                                        if (sintactico[i + 11].equals("Decimal") || sintactico[i + 11].equals("Numero")) {
                                            //        System.out.println("tipo de dato 2"+sintactico[i+11]);
                                            if (sintactico[i + 12].equals("Fin_linea")) {
                                                //          System.out.println("fin de la lina"+sintactico[i+12]);
                                                //         System.out.println("COMPILACION EXITOSA!!!!!");
                                                ResultCadena = ResultCadena + "Todas las variables OK\n" + "Ejecución Exitosa\n============================= \n";
                                                //System.out.println("valor en :"+datosentrada[i+6]);

                                            } else {
                                                Errores = Errores + "Eroor de compilacion:Simbolo desconocido por el Boolean--> " + sintactico[i + 10] + "\n";
                                            }
                                        } else {
                                            Errores = Errores + "No se reconoce cadenas o cualquier otro tipo de var que no sea decimal o entero\n";

                                        }

                                    } else {
                                        Errores = Errores + "Error de simbolo: no se puede utilizar en una declaracion Boolean!!!\n";
                                    }

                                } else {
                                    Errores = Errores + "Error de tipo de dato: no compatible con Boolean!\n";

                                }

                            } else {
                                Errores = Errores + "Falta la asignacion\n";

                            }

                        } else {
                            Errores = Errores + "No se reconoce el Identificardor" + sintactico[i + 7];

                        }

                    } catch (Exception E) {

                    }
                } else {
                    //System.out.println("Fallo este...");
                }
///fin del metodo booleano

                // System.out.println("dasdadas"+datosentrada[i+4]+datosentrada[i+7]);
                // System.out.println("Elementtos en el textArea: "+datosentrada[i+4]);
                if ((datosentrada[i + 4].matches("^Entero")) & (datosentrada[i + 9].matches("^Entero")) & (datosentrada[i + 14].matches("^Entero")) & (datosentrada[7].matches("[0-9]*")) & (datosentrada[12].matches("[0-9]*")) & (datosentrada[17].matches("[0-9]*"))) {
                    //System.out.println("CORECTA DECLAn EN ENTERO y asignacion de valores....");
                }

                ///if para validad que se aga una suma correcta de cadenas
                if ((datosentrada[i + 4].matches("^String")) & (datosentrada[i + 9].matches("^String")) & (datosentrada[i + 14].matches("^String")) & (datosentrada[7].matches("('.*')|(\\\".*\\\")")) & (datosentrada[12].matches("('.*')|(\\\".*\\\")")) & (datosentrada[17].matches("('.*')|(\\\".*\\\")"))) {
                    // System.out.println("Se hara una suma de cadenaas");
                    
                         ResultCadena = ResultCadena + datosentrada[7].replace("\"", "") + " " + datosentrada[12].replace("\"", "")+" "+datosentrada[17].replace("\"","");
                    System.out.println("datos en la picsa:"+datosentrada[17]); 
                  if (datosentrada[i].equals("*") & datosentrada[i].equals("/") & datosentrada[i].equals("-")) {
                        Errores=Errores+"No se acepta el simbolo ingresado";
                        
                    }else{
                     
                    }
                   
  
                }
                //if para validar el error de sumar cadena con intero: cadena+entero+entero
                if ((datosentrada[i + 4].matches("^String")) & (datosentrada[i + 9].matches("^Entero")) & (datosentrada[i + 14].matches("^Entero")) || datosentrada[i + 9].matches("^String")) {

                    //System.out.println("Error esta Sumando y asignacion, una cadena con un entero...");
                    Errores = Errores + "Error y asignacion o de suma ...";

                }
                if ((datosentrada[i + 4].matches("^Double")) & (datosentrada[i + 9].matches("^Double")) & (datosentrada[i + 14].matches("^Double"))) {

                }

                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////       
                // System.out.print("pero paso primero la estrucctura enteros-enteros");
                try {
                    //System.out.println("Accedio en esta estructura");
                    if (sintactico[i].equals("Funcion_Princ")) {
                        if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                            if (sintactico[i + 2].equals("Funcion_Cerramos")) {
                                if (sintactico[i + 3].equals("LLave_Izquierda")) {
                                    cmain = 1;
                                    linea++;
                                } else {
                                    Errores = Errores + "Falta {, linea: " + linea + "\n";
                                }
                            } else {
                                Errores = Errores + "Falta ), linea: " + linea + "\n";
                            }
                        } else {
                            Errores = Errores + "Falta (, linea: " + linea + "\n";
                        }
                    } else {
                        //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                    }
                } catch (Exception e) {
                }

                /////////////////////////////////
                try {
                    if (sintactico[i].equals("Tipo_Dato")) {
                        // if (sintactico[i].equals("Tipo_DatoDbl")) {

                        if (sintactico[i + 1].equals("ID")) {
                            if (sintactico[i + 2].equals("Funcion_Asignacion")) {
                                if (sintactico[i + 3].equals("Numero")) {

                                    if (sintactico[i + 4].equals("Fin_linea")) {
                                        //System.err.println("Sintaxis Correcta");
                                        linea++;
                                        cc++;
                                    } else {
                                        //System.err.println("No se encontro fin de linea, linea: "+linea);
                                        Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                                    }

                                } else {
                                    //System.err.println("Se espraba Inicializar Varible, linea: "+linea);
                                    Errores = Errores + "Se espraba Inicializar Varible, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba =, linea: "+linea);
                                Errores = Errores + "Se espraba =, linea: " + linea + "\n";
                            }
                        } else {
                            //System.err.println("Se espraba ID, linea: "+linea);
                            Errores = Errores + "Se espraba ID, linea :): " + linea + "\n";
                        }
                        /// }//tipo de dato double
                    }//tipo de dato entero
                } catch (Exception e) {
                    //System.err.println("No se encontro fin de linea, linea: "+linea);
                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }

                try {
                    //Evalua sintaxis operacion
                    if (sintactico[i].equals("ID") && sintactico[i + 2].equals("ID") && sintactico[i + 4].equals("ID")) {
                        if (sintactico[i + 1].equals("Funcion_Asignacion")) {
                            if (sintactico[i + 2].equals("ID") && sintactico[i].equals("ID") && sintactico[i + 4].equals("ID")) {
                                if (sintactico[i + 3].equals("Funcion_Division") || sintactico[i + 3].equals("Funcion_Multiplicaion") || sintactico[i + 3].equals("Funcion_Resta") || sintactico[i + 3].equals("Funcion_Suma")) {
                                    if (sintactico[i + 4].equals("ID") && sintactico[i].equals("ID") && sintactico[i + 2].contains("ID")) {
                                        if (sintactico[i + 5].equals("Fin_linea")) {
                                            id = datosentrada[i];
                                            id2 = datosentrada[i + 2];
                                            id3 = datosentrada[i + 4];
                                            signo = sintactico[i + 3];
                                            linea++;
                                            ccc = 1;
                                        } else {
                                            Errores = Errores + "Se espraba Fin Linea, linea: " + linea + "\n";
                                        }
                                    } else {
                                        //System.err.println("Se espraba ID, linea: "+linea);
                                        Errores = Errores + "Se esperaba ID, linea:perro: " + linea + "\n";
                                    }
                                } else {
                                    //System.err.println("Se espraba Operando, linea: "+linea);
                                    Errores = Errores + "Se espraba Operando, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba ID, linea: "+linea);
                            }
                        } else {
                            //System.err.println("Se espraba =, linea" + linea);
                        }
                    }
                } catch (Exception e) {
                    // System.err.println("No se encontro fin de linea, linea: "+linea);

                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }

                //evalua funcion imprimir
                if (sintactico[i].equals("Funcion_Imprimir")) {
                    if (sintactico[i + 1].equals("Comienza_Comentario")) {
                        if (sintactico[i + 2].equals("ID")) {
                            // System.err.println("Impimido");
                            linea++;
                            cccc = 1;
                            id4 = datosentrada[i + 2];
                        } else {
                            Errores = Errores + "Falta ID, linea: " + linea + "\n";
                        }
                    } else {
                        Errores = Errores + "Se espraba \\\\, linea: \"" + linea + "\n";
                    }
                }
            }
            if (cc == 3 && ccc == 1 && cccc == 1 && cmain == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {
                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
        } catch (Exception e) {
        }

        /////////////////////////////fin tipo int
        /////inicio de tipo de dato string
        try {
            //Evalua declaracion de variables
            for (int i = 0; i < sintactico.length; i++) {

                //  System.out.print("pero paso primero la estrucctura enteros-enteros");
                try {
                    if (sintactico[i].equals("Funcion_Princ") ) {
                        if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                            if (sintactico[i + 2].equals("Funcion_Cerramos")) {
                                if (sintactico[i + 3].equals("LLave_Izquierda")) {
                                    cmain = 1;
                                    linea++;
                                } else {
                                    Errores = Errores + "Falta {, linea: " + linea + "\n";
                                }
                            } else {
                                Errores = Errores + "Falta ), linea: " + linea + "\n";
                            }
                        } else {
                            //Errores=Errores+"Falta (, linea: "+linea+"\n";
                        }
                    } else {
                        //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                    }
                } catch (Exception e) {
                }

                /////////////////////////////////
                try {
                    if (sintactico[i].equals("funcion_for") ) {
                        if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                            if(sintactico[i+2].equals("Tipo_Dato")){ 
                                if (sintactico[i+3].equals("ID")&&sintactico[i+7].equals("ID")&&sintactico[i+11].equals("ID")){
                                    if (sintactico[i+4].equals("Funcion_Asignacion")){
                                        if (sintactico[i+5].equals("Numero")){
                                            if (sintactico[i+6].equals("Fin_linea")){
                                                if (sintactico[i+7].equals("ID")&&sintactico[i+3].equals("ID")&&sintactico[i+11].equals("ID")){
                                                    if (sintactico[i+8].equals("Funcion_menorque")){
                                                        if (sintactico[i+9].equals("Numero")){
                                                            if (sintactico[i+10].equals("Fin_linea")){
                                                                if (sintactico[i+11].equals("ID")&&sintactico[i+3].equals("ID")&&sintactico[i+7].equals("ID")){
                                                                    if (sintactico[i+12].equals("Funcion_Incrementa1")){                                                                                                                   
                                                                        if (sintactico[i + 13].equals("Funcion_Cerramos")) {
                                                                            if (sintactico[i + 14].equals("LLave_Izquierda")) {
                                                                            
                                                                                id = datosentrada[i + 3];
                                                                                id2 = datosentrada[i + 7];
                                                                                id3 = datosentrada[i + 11];
                                                                                cfor = 1;
                                                                                linea++;
                                                                            } else {
                                                                            Errores = Errores + "Falta {, linea: " + linea + "\n";
                                                                            }
                                                                        } else {
                                                                              Errores = Errores + "Falta ), linea: " + linea + "\n";
                                                                        }
                                                                    } else{
                                                                         Errores = Errores + "Falta ++, linea: " + linea + "\n";
                                                                    }    
                                                                } else{
                                                                    Errores = Errores + "Falta id, linea: " + linea + "\n"; 
                                                                }
                                                            } else{
                                                                 Errores = Errores + "Falta ;, linea: " + linea + "\n";
                                                            }
                                                        } else{
                                                             Errores = Errores + "Falta var, linea: " + linea + "\n";
                                                        }
                                                    } else{
                                                         Errores = Errores + "Falta <, linea: " + linea + "\n";
                                                    }
                                                } else{
                                                     Errores = Errores + "Falta un ID, linea: " + linea + "\n";
                                                }
                                            } else{
                                                 Errores = Errores + "Falta ; , linea: " + linea + "\n";
                                            }
                                        } else{
                                             Errores = Errores + "Falta Numero, linea: " + linea + "\n";
                                        }       
                                    } else{
                                         Errores = Errores + "Falta =, linea: " + linea + "\n";
                                    }
                                } else{
                                     Errores = Errores + "Falta un id, linea: " + linea + "\n";
                                }
                            } else{
                                 //Errores = Errores + "Falta un val, linea: " + linea + "\n";
                            }
                        } else {
                          //  Errores=Errores+"Falta (, linea: "+linea+"\n";
                        }
                    } else {
                       // Errores=Errores+"No se encontro for, linea: "+linea+"\n";
                    }
                } catch (Exception e) {
                }
//terminacion del for

                /////////////////////////////////
                //incio de if
                try {
                    if (sintactico[i].equals("Tipo_Dato")){
                        if (sintactico[i + 2].equals("ID")&&sintactico[i + 7].equals("ID")){
                            if (sintactico[i + 3].equals("Funcion_Asignacion")){
                                if (sintactico[i + 4].equals("Numero")){
                                
                                    if (sintactico[i + 5].equals("Funcion_if") ) {
                                        if (sintactico[i + 6].equals("Funcion_Abrimos")) {
                                            //if (sintactico[i + 2].equals("Funcion_Variable")){
                                                if (sintactico[i + 7].equals("ID")&& sintactico[i + 2].equals("ID")){
                                                    if (sintactico[i + 8].equals("Funcion_menorque")||sintactico[i + 3].equals("Funcion_mayorQue")||sintactico[i + 3].equals("Funcion_Asignacion")){
                                                        if (sintactico[i + 9].equals("Numero")){
                                                            if (sintactico[i + 10].equals("Funcion_Cerramos")) {
                                                                if (sintactico[i + 11].equals("LLave_Izquierda")) {
                                                                    id3 = datosentrada[i + 2];
                                                                    id4 = datosentrada[i + 7];
                                                                    cif = 1;
                                                                    linea++;
                                                                } else {
                                                                    Errores = Errores + "Falta {, linea: " + linea + "\n";
                                                                }
                                                            } else {
                                                                Errores = Errores + "Falta ), linea: " + linea + "\n";
                                                            }
                                                        }
                                                    }
                                                }
                                            //}
                                        } else {
                                            //Errores=Errores+"Falta (, linea: "+linea+"\n";
                                        }
                                    } else {
                                        //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                                }
                //finalizacion del if
                /////////////////////////////////
                try {
                    if (sintactico[i].equals("Tipo_DatoDbl")) {
                        if (sintactico[i + 1].equals("ID")) {
                            if (sintactico[i + 2].equals("Funcion_Asignacion")) {
                                if (sintactico[i + 3].equals("Decimal")) {
                                    if (sintactico[i + 4].equals("Fin_linea")) {
                                        //System.err.println("Sintaxis Correcta");
                                        linea++;
                                        cc++;
                                    } else {
                                        //System.err.println("No se encontro fin de linea, linea: "+linea);
                                        Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                                    }
                                } else {
                                    //System.err.println("Se espraba Inicializar Varible, linea: "+linea);
                                    Errores = Errores + "Se espraba Inicializar Varible, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba =, linea: "+linea);
                                Errores = Errores + "Se espraba =, linea: " + linea + "\n";
                            }
                        } else {
                            //System.err.println("Se espraba ID, linea: "+linea);
                            Errores = Errores + "Se espraba ID, linea :): " + linea + "\n";
                        }
                    }
                } catch (Exception e) {
                    //System.err.println("No se encontro fin de linea, linea: "+linea);
                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }

                try {
                    //Evalua sintaxis operacion
                    if (sintactico[i].equals("ID")) {
                        if (sintactico[i + 1].equals("Funcion_Asignacion")) {
                            if (sintactico[i + 2].equals("ID")) {
                                if (sintactico[i + 3].equals("Funcion_Division") || sintactico[i + 3].equals("Funcion_Multiplicaion") || sintactico[i + 3].equals("Funcion_Resta") || sintactico[i + 3].equals("Funcion_Suma")) {
                                    if (sintactico[i + 4].equals("ID")) {
                                        if (sintactico[i + 5].equals("Fin_linea")) {
                                            id = datosentrada[i];
                                            id2 = datosentrada[i + 2];
                                            id3 = datosentrada[i + 4];
                                            signo = sintactico[i + 3];
                                            linea++;
                                            ccc = 1;
                                        } else {
                                            Errores = Errores + "Se espraba Fin Linea, linea: " + linea + "\n";
                                        }
                                    } else {
                                        //System.err.println("Se espraba ID, linea: "+linea);
                                        Errores = Errores + "Se esperaba ID, linea: " + linea + "\n";
                                    }
                                } else {
                                    //System.err.println("Se espraba Operando, linea: "+linea);
                                    Errores = Errores + "Se espraba Operando, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba ID, linea: "+linea);
                            }
                        } else {
                            //System.err.println("Se espraba =, linea" + linea);
                        }
                    }
                } catch (Exception e) {
                    // System.err.println("No se encontro fin de linea, linea: "+linea);
                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }
                
                try {
                    if (sintactico[i].equals("Tipo_DatoDbl")) {
                        if (sintactico[i + 1].equals("ID")) {
                            if (sintactico[i + 2].equals("Funcion_Asignacion")) {
                                if (sintactico[i + 3].equals("Decimal")) {
                                    if (sintactico[i + 4].equals("Fin_linea")) {
                                        //System.err.println("Sintaxis Correcta");
                                        linea++;
                                        cc++;
                                    } else {
                                        //System.err.println("No se encontro fin de linea, linea: "+linea);
                                        Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                                    }
                                } else {
                                    //System.err.println("Se espraba Inicializar Varible, linea: "+linea);
                                    Errores = Errores + "Se espraba Inicializar Varible, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba =, linea: "+linea);
                                Errores = Errores + "Se espraba =, linea: " + linea + "\n";
                            }
                        } else {
                            //System.err.println("Se espraba ID, linea: "+linea);
                            Errores = Errores + "Se espraba ID, linea :): " + linea + "\n";
                        }
                    }
                } catch (Exception e) {
                    //System.err.println("No se encontro fin de linea, linea: "+linea);
                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }

                try {
                    //Evalua sintaxis operacion compleja
                    if (sintactico[i].equals("ID")) {
                        if (sintactico[i + 1].equals("Funcion_Asignacion")) {
                            if (sintactico[i + 2].equals("ID")) {
                                if (sintactico[i + 3].equals("Funcion_Division") || sintactico[i + 3].equals("Funcion_Multiplicaion") || sintactico[i + 3].equals("Funcion_Resta") || sintactico[i + 3].equals("Funcion_Suma")) {
                                    if (sintactico[i + 4].equals("Funcion_Abrimos")) {
                                        if (sintactico[i + 5].equals("ID")) {
                                            if (sintactico[i + 6].equals("Funcion_Division") || sintactico[i + 3].equals("Funcion_Multiplicaion") || sintactico[i + 3].equals("Funcion_Resta") || sintactico[i + 3].equals("Funcion_Suma")) {
                                                if (sintactico[i + 7].equals("ID")) {
                                                    if (sintactico[i + 8].equals("Funcion_Cerramos")) {
                                                        if (sintactico[i + 9].equals("Fin_linea")) {
                                                                        id = datosentrada[i];
                                                                        id2 = datosentrada[i + 2];
                                                                        id3 = datosentrada[i + 5];
                                                                        id4 = datosentrada[i + 7];
                                                                        signo = sintactico[i + 3];
                                                                        signo = sintactico[i + 6];
                                                                        linea++;
                                                                        ccc = 1;
                                                        } else {
                                                            Errores = Errores + "Se espraba Fin Linea, linea: " + linea + "\n";
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            //System.err.println("Se espraba ID, linea: "+linea);
                                            Errores = Errores + "Se esperaba ID, linea: " + linea + "\n";
                                        }
                                    }
                                } else {
                                    //System.err.println("Se espraba Operando, linea: "+linea);
                                    Errores = Errores + "Se espraba Operando, linea: " + linea + "\n";
                                }
                            } else {
                                //System.err.println("Se espraba ID, linea: "+linea);
                            }
                        } else {
                            //System.err.println("Se espraba =, linea" + linea);
                        }
                    }
                } catch (Exception e) {
                    // System.err.println("No se encontro fin de linea, linea: "+linea);
                    Errores = Errores + "No se encontro fin de linea, linea: " + linea + "\n";
                }

                //evalua funcion imprimir
                if (sintactico[i].equals("Funcion_Imprimir")) {
                    if (sintactico[i + 1].equals("Comienza_Comentario")) {
                        if (sintactico[i + 2].equals("ID")) {
                            
                             System.err.println("Impimido....."+datosentrada[i+2]);
                            linea++;
                            cccc = 1;
                            id4 = datosentrada[i + 2];
                        } else {
                            Errores = Errores + "Falta ID, linea: " + linea + "\n";
                        }
                    } else {
                        Errores = Errores + "Se espraba \\\\, linea: \"" + linea + "\n";
                    }
                }
            }
            
            if (cif == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {

                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
            
            if (cfor == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {

                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
            
            if (cc == 3 && ccc == 1 && cccc == 1 && cmain == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {

                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
        } catch (Exception e) {
        }
        
        ////////fin de tipo dato cadena
        //////////////////////////////////////////7
        //comienzo del for
       /* try {
            //Evalua declaracion de variables
            for (int i = 0; i < sintactico.length; i++) {

                //  System.out.print("pero paso primero la estrucctura enteros-enteros");
                try {
                    if (sintactico[i].equals("Funcion_Princ") ) {
                        if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                            if (sintactico[i + 2].equals("Funcion_Cerramos")) {
                                if (sintactico[i + 3].equals("LLave_Izquierda")) {
                                    cmain = 1;
                                    linea++;
                                } else {
                                    Errores = Errores + "Falta {, linea: " + linea + "\n";
                                }
                            } else {
                                Errores = Errores + "Falta ), linea: " + linea + "\n";
                            }
                        } else {
                           // Errores=Errores+"Falta (, linea: "+linea+"\n";
                        }
                    } else {
                        //Errores=Errores+"No se encontro MAIN, linea: "+linea+"\n";
                    }
                } catch (Exception e) {
                }

                /////////////////////////////////
                try {
                    if (sintactico[i].equals("funcion_for") ) {
                        if (sintactico[i + 1].equals("Funcion_Abrimos")) {
                            if(sintactico[i+2].equals("Funcion_Variable")){ 
                                if (sintactico[i+3].equals("ID")&&sintactico[i+7].equals("ID")&&sintactico[i+11].equals("ID")){
                                    if (sintactico[i+4].equals("Funcion_Asignacion")){
                                        if (sintactico[i+5].equals("Numero")){
                                            if (sintactico[i+6].equals("Fin_linea")){
                                                if (sintactico[i+7].equals("ID")&&sintactico[i+3].equals("ID")&&sintactico[i+11].equals("ID")){
                                                    if (sintactico[i+8].equals("Funcion_menorque")){
                                                        if (sintactico[i+9].equals("Numero")){
                                                            if (sintactico[i+10].equals("Fin_linea")){
                                                                if (sintactico[i+11].equals("ID")&&sintactico[i+3].equals("ID")&&sintactico[i+7].equals("ID")){
                                                                    if (sintactico[i+12].equals("Funcion_Incrementa1")){                                                                                                                   
                                                                        if (sintactico[i + 13].equals("Funcion_Cerramos")) {
                                                                            if (sintactico[i + 14].equals("LLave_Izquierda")) {
                                                                            
                                                                                id = datosentrada[i + 3];
                                                                                id2 = datosentrada[i + 7];
                                                                                id3 = datosentrada[i + 11];
                                                                                cfor = 1;
                                                                                linea++;
                                                                            } else {
                                                                            Errores = Errores + "Falta {, linea: " + linea + "\n";
                                                                            }
                                                                        } else {
                                                                              Errores = Errores + "Falta ), linea: " + linea + "\n";
                                                                        }
                                                                    } else{
                                                                         Errores = Errores + "Falta ++, linea: " + linea + "\n";
                                                                    }    
                                                                } else{
                                                                    Errores = Errores + "Falta id, linea: " + linea + "\n"; 
                                                                }
                                                            } else{
                                                                 Errores = Errores + "Falta ;, linea: " + linea + "\n";
                                                            }
                                                        } else{
                                                             Errores = Errores + "Falta var, linea: " + linea + "\n";
                                                        }
                                                    } else{
                                                         Errores = Errores + "Falta <, linea: " + linea + "\n";
                                                    }
                                                } else{
                                                     Errores = Errores + "Falta un ID, linea: " + linea + "\n";
                                                }
                                            } else{
                                                 Errores = Errores + "Falta ; , linea: " + linea + "\n";
                                            }
                                        } else{
                                             Errores = Errores + "Falta Numero, linea: " + linea + "\n";
                                        }       
                                    } else{
                                         Errores = Errores + "Falta =, linea: " + linea + "\n";
                                    }
                                } else{
                                     Errores = Errores + "Falta un id, linea: " + linea + "\n";
                                }
                            } else{
                                 //Errores = Errores + "Falta un val, linea: " + linea + "\n";
                            }
                        } else {
                          //  Errores=Errores+"Falta (, linea: "+linea+"\n";
                        }
                    } else {
                       // Errores=Errores+"No se encontro for, linea: "+linea+"\n";
                    }
                } catch (Exception e) {
                }
//terminacion del for

                //evalua funcion imprimir
                
            }
            
            if (cfor == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {

                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
            
            if (cmain == 1) {
                if (sintactico[sintactico.length - 1].equals("LLave_Derecha")) {
                    comprobar();
                } else {

                    Errores = Errores + "No se encunetra fin: " + linea + "\n";
                }

            }
            
        } catch (Exception e) {
        }*/
        
        //terminacion del for
        //fin de las estructuras de control de entero-entero
    }
    ////////////////////////////////////////////////////////////////////////////
    //FUnciones que identifican si son Letras, Numeros o Flotantes
    public String Errores = "";
    public String ResultCadena = "";
    int numeros[] = new int[3];
    String cadena[] = new String[3];
    String cnCadenas = "", cidns = "";
    String ids[] = new String[3];
    int cnumeros = 0, cids = 0;

    public boolean numero(String cad) {
        try {
            if (cad.matches("[0-9]*")) {
                numeros[cnumeros] = Integer.valueOf(cad);
                cnumeros++;
                // System.out.println("Encontro este entero"+cad);
 
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

    }

    public boolean cadena(String cad) {
        try {
            //"('.*')|(\".*\")"
            //^String
            if (cad.matches("('.*')|(\\\".*\\\")")) {
                //cadena[cnCadenas].equals(cad);
                //System.out.println("Encontro esta cadena declarada: "+ "'"+cad+"'");

                //cnCadenas++;
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

    }

    public boolean decimal(String cad) {
        if (cad.matches("[0-9]+(\\.[0-9][0-9]?)?")) {
            //System.out.println("Encontro este este  Float :"+cad);
            return true;

        } else {
            return false;
        }

    }
    int validar = 0;

    public boolean ID(String cadena) {
        validar++;

        if (cadena.matches("^[a-zA-Z0-9_]*$")) {
            if (validar <= 3) {
                ids[cids] = cadena;
                cids++;
            }
            return true;
        } else {
            return false;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    int po = 0, po2 = 0, po3 = 0, po4 = 0;

    void resultadoFinal() {
        if (po4 == 0) {
            Errores = Errores + "Ejecución Exitosa\n============================= \n" + numeros[0] + "\n";
        }
        if (po4 == 1) {
            Errores = Errores + "Ejecución Exitosa\n============================= \n" + numeros[1] + "\n";
        }
        if (po4 == 2) {
            Errores = Errores + "Ejecución Exitosa\n============================= \n" + numeros[2] + "\n";
        }
    }

    //Realiza las operaciones segun los simbolos +-*/ del codigo las variables y sus cvalores son almacenado en vectore diferentes
    int r = 0;

    void sumaC() {
        for (int i = 0; i < ids.length; i++) {

        }
    }

    void complejo() {
        for (int i = 0; i < ids.length; i++) {

            if (ids[i].equals(id)) {
                po = i;
            }

            if (ids[i].equals(id2)) {
                po2 = i;
            }

            if (ids[i].equals(id3)) {
                po3 = i;
            }

            if (ids[i].equals(id4)) {
                po4 = i;
            }
        }
        numeros[po3] = +numeros[po] + ( numeros[po2] * numeros[po4] );
        resultadoFinal();
    }
    
    void suma() {
        for (int i = 0; i < ids.length; i++) {

            if (ids[i].equals(id)) {
                po = i;
            }

            if (ids[i].equals(id2)) {
                po2 = i;
            }

            if (ids[i].equals(id3)) {
                po3 = i;
            }

            if (ids[i].equals(id4)) {
                po4 = i;
            }
        }
        numeros[po] = +numeros[po2] + numeros[po3];
        resultadoFinal();
    }

    void resta() {
        for (int i = 0; i < 3; i++) {
            if (ids[i].equals(id)) {
                po = i;
            }
            if (ids[i].equals(id2)) {
                po2 = i;
            }

            if (ids[i].equals(id3)) {
                po3 = i;
            }
            if (ids[i].equals(id4)) {
                po4 = i;
            }
        }

        numeros[po] = numeros[po2] - numeros[po3];
        resultadoFinal();
    }

    void multi() {
        for (int i = 0; i < 3; i++) {
            if (ids[i].equals(id)) {
                po = i;
            }
            if (ids[i].equals(id2)) {
                po2 = i;
            }

            if (ids[i].equals(id3)) {
                po3 = i;
            }
            if (ids[i].equals(id4)) {
                po4 = i;
            }
        }
        numeros[po] = numeros[po2] * numeros[po3];
        resultadoFinal();
    }

    void division() {
        for (int i = 0; i < 3; i++) {
            if (ids[i].equals(id)) {
                po = i;
            }
            if (ids[i].equals(id2)) {
                po2 = i;
            }

            if (ids[i].equals(id3)) {
                po3 = i;
            }
            if (ids[i].equals(id4)) {
                po4 = i;
            }
        }
        numeros[po] = numeros[po2] / numeros[po3];
        resultadoFinal();
    }

    int no = 0;
    String variablesnodeclaradas = "";

    void comprobar() {
        for (int i = 0; i < datosentrada.length; i++) {
            for (int j = 0; j < ids.length; j++) {
                if (datosentrada[i].equals(ids[j])) {
                    no++;
                }
            }
        }
        if (no < 7) {
            Errores = Errores + "Hay Variables No declaradas\n";
            no = 0;
        } else {
            Errores = Errores + "Todas las variables OK\n";
            evaluaSignos(signo);
        }
    }
    //////////////////////////////////////////////////////////////////////////////    

    String strDatos;

    void cargardatos() throws IOException {
        FileReader fr;

        fr = new FileReader("C:\\Users\\jose_\\Downloads\\Analizador_Lexico_Sintactico_Semantico\\Analzador_Lexico\\tokens.txt");
        BufferedReader br = new BufferedReader(fr);

        strDatos = br.readLine();
    }

    void evaluaSignos(String sign) {//{}
        switch (sign) {
            case "Funcion_Suma":
                suma();
                break;
            case "Funcion_Resta":
                resta();
                break;
            case "Funcion_Multiplicaion":
                multi();
                break;
            case "Funcion_Division":
                division();
                break;
            case "Funcion_compleja":
                complejo();
                break;
        }
    }

}
