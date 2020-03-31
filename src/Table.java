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
    
    Clase: Table
    Descripción: Esta clase representa una tabla completa de una Base de Datos
    Se compone de una Tabla Hash (LinkedHashMap), en donde los elementos son arreglos
    de cadenas, los cuales son los valores de cada atributo de la tabla, y la clave
    es el nombre de dicho atributo.

    Por ejemplo
    Tabla Employees
    Claves: employee_id, first_name, last_name, salary, ...
    Elementos: [100, 101, 102, 103, ...], [Steven, Neena, Lex, Alexander, ...], [King, Kochhar, DeHaan, Hunold, ...], [24000, 17000, 9000, 6000, ...], ...

    * [] representa un arreglo

    De esta forma podemos acceder a los valores de cada atributo utilizando su nombre
    como clave para obtenerlos.
    Es casi literalmente hacer un "SELECT"
    Y con la lista que se obtenga se pueden hacer las condiciones, pero en una clase
    que tenga todas las tablas leídas (esto es, en la clase Database)
*/

package src;

// Bibliotecas
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Table {
    // Nombre de la tabla
    private String name;
    // Tabla hash que representa toda la tabla, String es la clave y ArrayList<String> son los valores para esa clave
    private LinkedHashMap<String, ArrayList<String>> data;
    // Tabla hash que almacena el tamaño de columna para cada tipo de dato (para presentación)
    private LinkedHashMap<String, Integer> sizes;

    public Table(String name) {
        data = new LinkedHashMap<>();
        sizes = new LinkedHashMap<>();

        this.name = name;
    }

    /* 
        FUNCIONES DE MANIPULACION DE DATOS 
        Utilizar para agregar, eliminar, y obtener los datos de la tabla
    */

    // Con esta función, se especifica una clave un valor único a ser añadido (no una lista de valores).
    public void addValueTo(String attribute, String value) {
        // En caso de que aún no haya sido registrado un atributo con ese nombre (attribute), se crea una nueva lista de valores
        if (!data.containsKey(attribute)) {
            ArrayList<String> attributeValues = new ArrayList<>();
            attributeValues.add(value);
            data.put(attribute, attributeValues);

            sizes.put(attribute, value.length() > attribute.length() ? value.length() : attribute.length()); // Añade también el tamaño del tipo de dato
        }
        else data.get(attribute).add(value); // Si el atributo ya existe, solo se añade un nuevo valor
    }

    // Esta función retorna la lista de valores para un determinado atributo (attribute)
    public ArrayList<String> getAttributeValues(String attribute) throws NullPointerException {
        ArrayList<String> values = data.get(attribute);

        if (values == null) throw new NullPointerException();

        return values;
    }

    // Esta función retorna una tupla de la lista disponible para consultar valor por valor a través del nombre del atributo.
    public LinkedHashMap<String, String> getTuple(int index) {
        LinkedHashMap<String, String> tuple = new LinkedHashMap<>();

        for (String attribute : data.keySet()) {
            tuple.put(attribute, data.get(attribute).get(index));
        }

        return tuple;
    }

    // Retorna la cardinalidad de la tabla
    public int cardinality() {
        for (String attribute : data.keySet()) {
            return data.get(attribute).size();
        }
        return 0;
    }

    // Retorna las claves para los atributos
    // (NOTA: Únicamente para iterar sobre él)
    public Set<String> getAttributeNames() {
        return data.keySet();
    }

    // Retorna el nombre de la tabla
    public String getName() {
        return name;
    }


    /* 
        FUNCIONES DE VISUALIZACION
        Utilizar solo para mostrar en pantalla
        (no óptimas para operaciones y manipulación)
    */

    // Esta función retorna los valores de cada atributo para una sola tupla
    public String tuple(int index) {
        StringBuilder tuple = new StringBuilder();
        tuple.append(" | ");
        for (String attribute : data.keySet()) {
            String value = data.get(attribute).get(index);
            StringBuilder spaces = new StringBuilder();
            for (int i = 0; i < sizes.get(attribute) - value.length(); i++) spaces.append(" "); // Añade los espacios faltantes

            tuple.append(spaces + value + " | ");
        }

        return tuple.toString();
    }

    // Esta función retorna todas las tuplas una tras otra
    public String showAll() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < cardinality(); i++) {
            out.append(tuple(i) + "\n");
        }
        return out.toString();
    }

    // Esta función regresa una "cabecera" con todos los nombres de los atributos
    // Los nombres de los atributos se separan según el tamaño máximo del tipo de dato
    public String header() {
        StringBuilder header = new StringBuilder();
        header.append(" | ");
        for (String attribute : data.keySet()) {
            for (int i = 0; i < sizes.get(attribute) - attribute.length(); i++) header.append(" ");
            
            header.append(attribute + " | ");
        }
        StringBuilder lines = new StringBuilder();
        for (int i = 0; i < header.length(); i++) lines.append("-");

        return lines.toString() + "\n" + header.toString() + "\n" + lines.toString();
    }
}