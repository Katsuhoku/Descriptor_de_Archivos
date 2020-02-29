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

    Clase: Employee
    Descripción: Esta clase representa los datos de un empleado de la tabla.
    Almacena todos los datos en forma de cadena (String), de tal forma que se conservan
    todos y cada uno de los caracteres que es capaz de almacenar el atributo (el rango es
    establecido por el descriptor), pero les da formato a los elementos que lo requieren
    cuando sean utilizados.
*/

package src;

import java.util.ArrayList;

public class Employee {
    // Atributos
    // Todos son cadenas (String) y contienen el máximo de longitud del atributo
    private String employeeID; // Identificador de Empleado
    private String firstName; // Apellido
    private String lastName; // Nombre de pila
    private String email; // Correo Electrónico
    private String phoneNumber; // Número de teléfono
    private String hireDate; // Fecha de contratación
    private String jobID; // Identificador de empleo
    private String salary; // Salario
    private String commissionPCT; // Porcentaje de comisiones (en decimales, puede no estar especificado)
    private String managerID; // Identificador de administrador (puede no estar especificado)
    private String departmentID; // Identificador de departamento de trabajo

    // Constructor
    // Recibe un conjunto de cadenas de texto ordenado, y de éste toma cada uno de los valores
    // para los atributos del empleado (en total, 11)
    public Employee(ArrayList<String> data) {
        employeeID = data.get(0);
        firstName = data.get(1);
        lastName = data.get(2);
        email = data.get(3);
        phoneNumber = data.get(4);
        hireDate = data.get(5);
        jobID = data.get(6);
        salary = data.get(7);
        commissionPCT = data.get(8);
        managerID = data.get(9);
        departmentID = data.get(10);
    }


    /* Métodos */
    // Retorna TODOS los atributos del empleado en una única cadena
    // Los atributos conservan su extensión con la que fueron introducidos (es decir, el tamaño
    // que es capaz de almacenar el atributo)
    @Override
    public String toString() {
        return employeeID + firstName + lastName + email + phoneNumber + hireDate + "        " + jobID + salary + " " + commissionPCT + managerID + " " + departmentID;
    }

    // Retorna el salario como número
    public int getSalaryAsInt(){
        return Integer.parseInt(salary.replace(" ", ""));
    }

    // Método de selección para la consulta
    // Retorna solo el apellido (lastName) y el salario (salary) en un una cadena.
    public String select() {
        String query = lastName.trim() + (lastName.trim().length() > 7 ? "\t\t" : "\t\t\t") + salary.trim();
        return query;
    }
}