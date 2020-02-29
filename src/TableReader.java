/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Baéz González José, 201657079
    Bautista Otero Alexandra, 
    Coria RIos Marco Antonio, 201734576
    Hernández Ramos Ángel, 201653224
    Torres Pérez Daniel, 201733939

    Clase: TableReader
    Descripción: Esta clase se encarga de leer el archivo de la tabla, el cual debe incluir
    el descriptor de archivos. Haciendo uso del descriptor, lee los datos de la tabla
    y construye un objteo de la clase Employee (clase que representa los datos de un Empleado)
    por cada registro en la tabla, los cuales guarda en una lista (ArrayList). Permite el 
    retorno de la lista completa de objetos tipo Employee. 

    Nota: Esta clase NO se encarga del procesamiento de la consulta. Su propósito es el de
    leer el archivo de la tabla correctamente.
*/

package src;

// Bibliotecas
import java.io.IOException;
import java.util.ArrayList;

public class TableReader {
    private String[] fileDescriptor; // Descriptor de Archivo
    private ArrayList<Employee> employees; // Registro de empleados

    // Constructor
    // Inicializa el registro de empleados
    public TableReader() {
        employees = new ArrayList<>();
    }

    // Lee el archivo de la tabla.
    // Convierte cada registro a un objeto de la clase Employee.
    // Guarda cada objeto en el registro de empleados (lista)
    public void readTable(String filename) throws IOException {
        // Se abre el archivo de la tabla
        TextFile file = new TextFile(); // Archivo fuente de la tabla
        file.openRead(filename);
        fileDescriptor = file.readLine().split(","); // se obtiene el descriptor de archivos

        String line = null;
        
        // Realiza la conversión de cada registro en la tabla en un objeto Employee
        // Utiliza el descriptor de archivo para realizar la división de la cadena leída del archivo
        while ((line = file.readLine()) != null) {
            ArrayList<String> data = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                // Límites para cortar la cadena de la tupla (obtenidos del descriptor)
                int lowBound = Integer.parseInt(fileDescriptor[i * 3 + 2]);
                int highBound = Integer.parseInt(fileDescriptor[i * 3 + 3]);

                data.add(line.substring(lowBound - 1, highBound)); // Añade cada segmento de cadena en una lista
            }
            employees.add( new Employee(data) ); // Convierte la lista de segmentos en un objeto Employee
        }
    }

    // Retorna la lista de empleados (en objetos Employee)
    public ArrayList<Employee> getEmployees() {
        return employees;
    }
}