/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Arizmendi Ramírez Esiel Kevin, 201727950
    Coria Rios Marco Antonio,201737811
    Ruiz Lozano Paulo Cesar, 201734576

    Clase: Query
    Descripción: Clase encargada de procesar la consulta con los parámetros del usuario.
    Almacena una base de datos (clase Database) que contiene todas las tablas necesarias para realizar las consultas.
    Las funciones tratan de seguir la sintaxis del lenguaje SQL.
*/

package src;

// Bibliotecas
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Query {
    // Base de Datos
    private Database db;

    // Constrtuctor
    public Query() {
        db = new Database();
    }

    // Carga una Tabla desde un archivo a la Base de Datos
    public void importTable(String dbfilename) throws IOException {
        db.addTable(dbfilename.replace(".txt", ""), TableReader.readTable(dbfilename));
    }

    // Operación FROM
    // Selecciona y retorna la tabla con el nombre especificado en el argumento
    // Este es el primer paso en cualquier consulta
    public Table from(String dbname) {
        return db.getTable(dbname);
    }

    // Operación Específica de WHERE
    // "salary BETWEEN minsalary AND maxsalary"
    // Selecciona todas las tuplas de la tabla en el argumento que contengan un salario
    // dentro de los límites especificados en los argumentos.
    // Para encadenar acciones, se llama a la función "from()" en el parámetro "table"
    public Table between(Table table, int minsalary, int maxsalary) {
        Table result = new Table();

        // En caso de que no se haya podido leer la tabla
        if (table == null) {
            return null;
        }

        // Itera sobre todas las tuplas de la tabla
        for (int i = 0; i < table.cardinality(); i++) {
            LinkedHashMap<String, String> tuple = table.getTuple(i);
            int salary = Integer.parseInt(tuple.get("salary").trim());
            if (salary >= minsalary && salary <= maxsalary) {
                for (String attribute : table.getAttributeNames()) {
                    result.addValueTo(attribute, tuple.get(attribute));
                }
            }
        }

        return result;
    }

    // Operación SELECT
    // Realiza la proyección sobre la tabla indicada en el argumento
    // Los atributos a mostrar se especifican en un ArrayList de String
    // Para encadenar acciones, se llama a la función "from()" o "between()" en el parámetro "table"
    public Table select(Table table, ArrayList<String> attributes) {
        Table result = new Table();

        for (String attribute : attributes) {
            for (String value : table.getAttributeValues(attribute)) {
                result.addValueTo(attribute, value);
            }
        }

        return result;
    }
}