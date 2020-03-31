/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Baéz González José, 201657079
    Bautista Otero Alexandra, 201640295
    Coria Rios Marco Antonio, 201734576
    Hernández Ramos Ángel, 201653224
    Torres Pérez Daniel, 201733939

    Clase: TableReader
    Descripción: Esta clase se encarga de leer el archivo de la tabla, el cual debe incluir
    el descriptor de archivos. Haciendo uso del descriptor, lee los datos de la tabla
    y construye un objeto de clase "Table", en el cual va depositando los valores encontrados en el archivo.
    El método es estático, por lo que no es necesario crear un objeto de esta clase, y tampoco guarda
    las tablas que son leídas.

    Nota: Esta clase NO se encarga del procesamiento de la consulta. Su propósito es el de
    leer el archivo de la tabla correctamente.
*/

package src;

// Bibliotecas
import java.io.IOException;

public class TableReader {
    // Lee el archivo de la tabla.
    // Convierte cada registro a un objeto de la clase Employee.
    // Guarda cada objeto en el registro de empleados (lista)
    public static Table readTable(String filename) throws IOException {
        // Se abre el archivo de la tabla
        TextFile file = new TextFile(); // Archivo fuente de la tabla
        if (file.openRead(filename) == false) {
            throw new IOException();
        }
        String[] fileDescriptor = file.readLine().split(","); // se obtiene el descriptor de archivos

        // Almacena toda la tabla en un objeto tipo Table
        // Utiliza el descriptor de archivo para realizar la división de la cadena leída del archivo
        // y hacer el mapeo de la Tabla Hash por atributo

        int numAtributes = (int) fileDescriptor.length / 3; // Total de atributos

        String line = null;
        Table newTable = new Table(filename.replace(".txt", ""));
        while ((line = file.readLine()) != null) {
            for (int i = 0; i < numAtributes; i++) {
                // Límites para cortar la cadena de la tupla (obtenidos del descriptor)
                String attributeName = fileDescriptor[i * 3];
                int lowBound = Integer.parseInt(fileDescriptor[i * 3 + 1]);
                int highBound = Integer.parseInt(fileDescriptor[i * 3 + 2]);

                // Añade cada atributo a la tabla
                newTable.addValueTo(attributeName, line.substring(lowBound - 1, highBound));
            }
        }

        return newTable;
    }
}