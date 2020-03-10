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

    Clase: Database
    Descripción: Esta clase representa toda una Base de Datos, es decir, un conjunto
    de tablas diferentes y quizás relacionadas entre sí.
    Similar a la clase Table, utiliza una Tabla Hash para guardar todas las tablas de la
    base de datos (es decir, objetos de tipo Table). La clave de cada una es el nombre
    de la tabla que se define como primer elemento del Descriptor de Archivos.

    Por ejemplo:
    Clave: employees, departments, ...
    Elemento Tabla Employees, Tabla Departments, ...
*/

package src;

import java.util.LinkedHashMap;

public class Database {
    // Conjunto de tablas de la Base de Datos. Cada una se mapea con nombre en la tabla hash
    private LinkedHashMap<String, Table> tables;

    public Database() {
        tables = new LinkedHashMap<>();
    }

    // Esta función solo permite añadir nuevas tablas (es decir, que la clave no exista antes)
    public boolean addTable(String name, Table table) {
        if (!tables.containsKey(name)) {
            tables.put(name, table);
            return true;
        }
        return false;
    }

    // Esta función solo permite reemplazar tablas (es decir, que la clave ya existía)
    public boolean replaceTable(String name, Table table) {
        if (tables.containsKey(name)) {
            tables.put(name, table);
            return true;
        }
        return false;
    }

    // Esta función retorna una tabla en específico
    public Table getTable(String name) {
        return tables.get(name);
    }
}