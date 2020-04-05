/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Coria Rios Marco Antonio, 201734576

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
    // Sirve para obtener únicamente una sola tabla
    public Table from(String dbname) {
        return db.getTable(dbname);
    }

    // Operación FROM con JOIN
    // Recibe un par de tablas y retorna el producto cartesiano de éstas
    // La condición del JOIN se establece en el WHERE, el cual se representa por la operación
    // BETWEEN o por la operación EQUALS disponibles
    // Los identificadores "id1" y "id2" son los nombres para reconocer la procedencia de cada
    // atributo en la nueva tabla. El nombre  de cada atributo en la nueva tabla se 
    // transforma en "idx.attribute_name"
    public Table product(Table table1, String id1, Table table2, String id2) {
        Table productTable = new Table("Product Result");

        for (int i = 0; i < table1.cardinality(); i++) {
            // Obtiene la i-ésima tupla de la tabla 1
            LinkedHashMap<String, String> tuple1 = table1.getTuple(i);
            for (int j = 0; j < table2.cardinality(); j++) {
                // Obtiene la j-ésima tupla de la tabla 2
                LinkedHashMap<String, String> tuple2 = table2.getTuple(j);

                // Combina ambas tuplas y las agrega a la nueva tabla
                for (String attribute1 : table1.getAttributeNames()) {
                    productTable.addValueTo(id1 + "." + attribute1, tuple1.get(attribute1));
                }

                for (String attribute2 : table2.getAttributeNames()) {
                    productTable.addValueTo(id2 + "." + attribute2, tuple2.get(attribute2));
                }
            }
        }

        return productTable;
    }

    // Operación específica de WHERE: EQUALS
    public Table equals(Table table, String attribute1, String attribute2) throws AttributeNotFoundException {
        Table result = new Table("Equals Result");

        // Encaso de que no se haya podido leer la tabla
        if (table == null) {
            return null;
        }

        // Itera sobre todas las tuplas de la tabla
        for (int i = 0; i < table.cardinality(); i++) {
            LinkedHashMap<String, String> tuple = table.getTuple(i);
            // Compara ambos atributos
            String value1 = null;
            String value2 = null;

            try {
                value1 = tuple.get(attribute1).trim();
            } catch (NullPointerException npe) {
                throw new AttributeNotFoundException("\"" + attribute1 + "\" no se reconoce como un atributo para la tabla \"" + table.getName() + "\"", attribute1);
            }
            
            try {
                value2 = tuple.get(attribute2).trim();
            } catch (NullPointerException npe) {
                throw new AttributeNotFoundException("\"" + attribute2 + "\" no se reconoce como un atributo para la tabla \"" + table.getName() + "\"", attribute2);
            }

            if (value1.equals(value2)) {
                for (String attribute : table.getAttributeNames()) {
                    result.addValueTo(attribute, tuple.get(attribute));
                }
            }
        }

        return result;
    }



    // Operación Específica de WHERE: BETWEEN
    // "salary BETWEEN minsalary AND maxsalary"
    // Selecciona todas las tuplas de la tabla en el argumento que contengan un salario
    // dentro de los límites especificados en los argumentos.
    // Para encadenar acciones, se llama a la función "from()" en el parámetro "table"
    public Table between(Table table, int minsalary, int maxsalary) {
        Table result = new Table("Between Result");

        // En caso de que no se haya podido leer la tabla
        if (table == null) {
            return null;
        }

        // Itera sobre todas las tuplas de la tabla
        for (int i = 0; i < table.cardinality(); i++) {
            LinkedHashMap<String, String> tuple = table.getTuple(i);
            int salary = Integer.parseInt(tuple.get("salary").trim());
            if (salary > minsalary && salary < maxsalary) {
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
    public Table select(Table table, ArrayList<String> attributes) throws AttributeNotFoundException {
        Table result = new Table("Select Result");

        for (String attribute : attributes) {
            try {
                for (String value : table.getAttributeValues(attribute)) {
                    result.addValueTo(attribute, value);
                }
            } catch (NullPointerException npe) {
                throw new AttributeNotFoundException("\"" + attribute + "\" no se reconoce como un atributo para la tabla \"" + table.getName() + "\"", attribute);
            }
        }

        return result;
    }
}