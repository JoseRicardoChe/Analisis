
package Ventanas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Archivo {
    File f;
    FileReader lectorArchivo;
    FileWriter escritorArchivo;
    
    public void nuevoviejo(String nombre, String texto) {
        String temp = this.read(nombre);
        temp = temp + texto;
        this.newdoc(nombre, temp);
    }

    public void newdoc(String nombre, String texto) {
        File f;
        FileWriter escritorArchivo;
        try {
            f = new File (nombre);
            escritorArchivo = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(escritorArchivo);
            PrintWriter salida =  new PrintWriter(bw);
            
            //salida.write(texto + "\n");
            salida.write(texto);
            
            salida.close();
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public String read(String nombre) {
        /* El parametro nombre indica el nombre del archivo P.E. "prueba.txt"*/
        
        File f;
        FileReader lectorArchivo;
        try {
            f = new File(nombre);
            lectorArchivo = new FileReader(f);
            BufferedReader br = new BufferedReader(lectorArchivo);
            String l = "";
            String aux="";
            while(true){
                aux = br.readLine();
                if(aux != null) 
                    //l = l+aux+"\n";
                    l = l+aux+" ";
                else
                    break;
            }
            br.close();
            lectorArchivo.close();
            return l;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    
    public String leerGrafico() {
        File f;
        javax.swing.JFileChooser j = new javax.swing.JFileChooser();
        j.showOpenDialog(j);
        String path = j.getSelectedFile().getAbsolutePath();
        String lectura="";
        f = new File(path);
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String aux;
            while((aux = br.readLine()) != null) {
                lectura = lectura+aux+"\n";
            }
        } catch (Exception e) {
        }
        
        return lectura;
        
    }
}
