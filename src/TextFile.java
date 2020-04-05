/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Coria Rios Marco Antonio, 201734576

    Clase: TextFile
    Descripción: Clase encargada de abrir y leer un archivo de texto cualquiera.
*/

package src;

//Crean los lectores/escritores en buffer.
import java.io.BufferedReader;
import java.io.BufferedWriter;
//Crean los lectores/escritores de streams de entrada/salida
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//Crean los streams de entrada/salida
import java.io.FileInputStream;
import java.io.FileOutputStream;
//Excepción de Entrada/Salida
import java.io.IOException;

public class TextFile{
  private BufferedReader reader; //Flujo de lectura
  private BufferedWriter writer;

  //Constructor sin apertura de archivo.
  public TextFile() {
    reader = null;
    writer = null;
  }

  //Abrir archivo para Lectura. Recibe el nombre del archivo e intenta abrirlo para lectura.
  //Retorna <true> si lo pudo abrir, <false> si no.
  public boolean openRead(String filename) {
    try{
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
      return true;
    }
    catch(IOException ioe){
      return false;
    }
  }

  //Cierra archivo para Lectura. Cierra el archivo abierto en este objeto.
  //Retorna <true> si lo pudo cerrar, <false> si no.
  public boolean closeRead() {
    try{
      reader.close();
      return true;
    }
    catch(IOException ioe){
      return false;
    }
  }

  //Abrir archivo para Escritura. Recibe el nombre del archivo e intenta abrirlo para Escritura.
  //Retorna <true> si lo pudo abrir, <false> si no.
  public boolean openWrite(String filename) {
    try{
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
      return true;
    }
    catch(IOException ioe){
      return false;
    }
  }

  //Cierra archivo para Escritura. Cierra el archivo abierto en este objeto.
  //Retorna <true> si lo pudo cerrar, <false> si no.
  public boolean closeWrite() {
    try{
      writer.close();
      return true;
    }
    catch(IOException ioe){
      return false;
    }
  }

  //Leer Línea. Lee una línea del archivo de texto. Retorna la cadena o null,
  //según la función readLine(). Lanza la excepción IOException en caso de error.
  public String readLine() throws IOException {
    String s = reader.readLine();
    return s;
  }

  //Escribir Línea. Recibe un String y lo escribe al archivo, seguido de un salto de Línea
  //(line feed). Lanza una excepción IOException en caso de error.
  public void writeLine(String line) throws IOException {
    writer.write(line);
    writer.newLine();
    return;
  }
}
