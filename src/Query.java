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

    Clase: Query
    Descripción: Clase encargada de procesar la consulta con los parámetros del usuario.
    Almancena, en tres tablas distintas, cada uno de los resultados del proceso de la 
    consulta paso a paso: obtención de la tabla completa, selección sobre la tabla completa y
    proyección sobre la selección. Permite el retorno de estas tablas para su despliegue
    en pantalla.
*/

package src;

// Bibliotecas
import java.util.ArrayList;

public class Query {
    // Tablas de la consulta, paso a paso
    private ArrayList<Employee> employees; // tabla completa
    private ArrayList<Employee> selectionTable; // tabla de la selección sobre la tabla completa
    private ArrayList<String> projectionTable; // tabla de la proyección sobre la selección

    // Procesa la consulta con condicional BETWEEN, en el rango específicado por los
    // límites en los parámetros
    public void between(int minsalary, int maxsalary) {
        TableReader tableReader = new TableReader(); // Lector de la tabla (utiliza el descriptor de archivos)

        // Inicializa las 3 tablas
        employees = new ArrayList<>();
        selectionTable = new ArrayList<>();
        projectionTable = new ArrayList<>();

        // Verifica que no haya error al abrir el archivo
        try {
            tableReader.readTable("Employees.txt");
            employees = tableReader.getEmployees(); // Paso 1: Selecciona la tabla completa
        }
        catch (Exception e) {
            // En caso de ocurrir algún error, termina la ejecución
            e.printStackTrace();
            System.exit(-1);
        }

        // Proceso de selección y proyección
        // Se busca que el salario esté dentro del intervalo (selección)
        for (Employee employee : employees) {
            if (employee.getSalaryAsInt() >= minsalary && employee.getSalaryAsInt() <= maxsalary) {
                selectionTable.add(employee); // Paso 2: Selección (Operación del Álgebra Relacional)
            }
        }

        // Se obtienen únicamente las columsnas solicitadas (proyección)
        for (Employee employee : selectionTable) {
            projectionTable.add(employee.select()); // Paso 3: Proyección (Operación del Álgebra Relacional)
        }
    }

    // Devuelve la tabla completa, en forma de cadena de texto, para ser desplegada en pantalla
    public String getEmployees() {
        StringBuilder employeeTable = new StringBuilder();
        for (Employee employee : employees) {
            employeeTable.append(employee.toString() + "\n");
        }
        return employeeTable.toString();
    }

    // Devuelve la tabla luego de la selección, en forma de cadena de texto, para ser desplegada en pantalla
    public String getSelectionTable() {
        StringBuilder selectedTable = new StringBuilder();
        for (Employee employee : selectionTable) {
            selectedTable.append(employee.toString() + "\n");
        }
        return selectedTable.toString();
    }

    // Devuelve la tabla luego de la proyección en la selección, en forma de cadena de texto, para ser desplegada en pantalla
    public String getProjectionTable() {
        StringBuilder projectedTable = new StringBuilder();
        for (String projection : projectionTable) {
            projectedTable.append(projection + "\n");
        }
        return projectedTable.toString();
    }
}