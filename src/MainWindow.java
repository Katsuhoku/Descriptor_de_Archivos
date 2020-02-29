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

    Clase: MainWindow.java
    Descripción: En esta clase se encuentran todos los componentes de la interfaz gráfica
    junto con su respectiva inicialización y actualización. Se encarga de mostrar las
    opciones de uso y los resultados al usuario.
*/

package src;

// Bibliotecas necesarias
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    // Atributos de la ventana
    private final int MIN_WIDTH = 960; // ancho mínimo de la ventana
    private final int MIN_HEIGHT = 540; // alto mínimo de la ventana
    private Query q; // objeto que realiza las consultas y obtiene los resultados paso a paso

    // Componentes
    private JPanel mainPanel; // panel principal

    private JButton processButton; // botón de procesamiento de la consulta
    private JButton viewEmployeeTableButton; // botón de visualización de la tabla completa
    private JButton viewSelectionTableButton; // botón de visualización de la tabla luego de la seleción
    private JButton viewProjectionTableButton; // botón de visualización de la tabla luego de la selección y la proyección

    private JTextField minsalaryField; // campo de texto para recibir el límite inferior del salario
    private JTextField maxsalaryField; // campo de texto para recibir el límite superior del salario

    private JTextArea tableArea; // área de texto donde se mostrará la tabla seleccionada

    // Constructor
    // Inicializa la ventana y sus componentes
    public MainWindow() {
        q = new Query();

        setTitle("BETWEEN Query");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        init();

        setVisible(true);
    }

    // Inicialización de los componentes
    private void init() {
        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Botón de procesamiento de la consulta
        processButton = new JButton("Procesar");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                processQuery();
            }
        });
        processButton.setBackground(Color.WHITE);
        processButton.setEnabled(false);


        // Campos de texto para los límites inferior y superior del salario
        minsalaryField = new JTextField(8);
        minsalaryField.setHorizontalAlignment(JTextField.LEFT);
        minsalaryField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                   e.consume();
                }
                if (maxsalaryField.getText().length() == 0 || (c != '\b' ? minsalaryField.getText() + c : minsalaryField.getText()).length() == 0) {
                    processButton.setEnabled(false);
                }
                else {
                    processButton.setEnabled(true);
                }
             }
        });

        maxsalaryField = new JTextField(8);
        maxsalaryField.setHorizontalAlignment(JTextField.LEFT);
        maxsalaryField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                   e.consume();
                }
                if ((c != '\b' ? maxsalaryField.getText() + c : maxsalaryField.getText()).length() == 0 || minsalaryField.getText().length() == 0) {
                    processButton.setEnabled(false);
                }
                else {
                    processButton.setEnabled(true);
                }
             }
        });

        JPanel pageStartPanel = new JPanel();
        
        // Los campos de texto se agregan entre una etiqueta que muestra cómo se escribiría
        // la consulta completa en el lenguaje SQL
        pageStartPanel.add(new JLabel("SELECT last_name, salary FROM employee WHERE salary BETWEEN "));
        pageStartPanel.add(minsalaryField);
        pageStartPanel.add(new JLabel(" AND "));
        pageStartPanel.add(maxsalaryField);
        pageStartPanel.add(new JLabel(" ; "));
        pageStartPanel.add(processButton);
        pageStartPanel.setBackground(new Color(201, 245, 169));

        mainPanel.add(pageStartPanel, BorderLayout.PAGE_START);

        // Área de texto para mostrar la tabla seleccionada
        tableArea = new JTextArea();
        tableArea.setEditable(false);
        tableArea.setFont(new Font("Consolas", Font.PLAIN, 15));

         // Se introduce en un panel de barra de desplazamiento, para cuando la tabla supera el alto o ancho visible
         // en la pantalla
        JScrollPane scroll = new JScrollPane(tableArea);

        mainPanel.add(scroll, BorderLayout.CENTER);

        // Botones de selección de tabla a mostrar
        viewEmployeeTableButton = new JButton("Employees");
        viewEmployeeTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(0);
            }
        });
        viewEmployeeTableButton.setBackground(Color.WHITE);
        viewEmployeeTableButton.setEnabled(false);

        viewSelectionTableButton = new JButton("Selección");
        viewSelectionTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(1);
            }
        });
        viewSelectionTableButton.setBackground(Color.WHITE);
        viewSelectionTableButton.setEnabled(false);

        viewProjectionTableButton = new JButton("Proyección");
        viewProjectionTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(2);
            }
        });
        viewProjectionTableButton.setBackground(Color.WHITE);
        viewProjectionTableButton.setEnabled(false);

        JPanel pageEndPanel = new JPanel();
        pageEndPanel.add(viewEmployeeTableButton);
        pageEndPanel.add(viewSelectionTableButton);
        pageEndPanel.add(viewProjectionTableButton);
        pageEndPanel.setBackground(new Color(201, 245, 169));

        mainPanel.add(pageEndPanel, BorderLayout.PAGE_END);

        // Paneles auxiliares para dar espacio a los costados del área de la tabla
        JPanel lineStartPanel = new JPanel();
        JPanel lineEndPanel = new JPanel();
        lineStartPanel.setBackground(new Color(201, 245, 169));
        lineEndPanel.setBackground(new Color(201, 245, 169));

        mainPanel.add(lineStartPanel, BorderLayout.LINE_START);
        mainPanel.add(lineEndPanel, BorderLayout.LINE_END);

        // Se añade el panel a la ventana
        add(mainPanel);
    }

    /* Acciones en Eventos */
    // Procesa la consulta con los valores especificados (al presionar el botón de "Procesar")
    // El objeto "q" (de clase Query) genera las tablas correspondientes, solo si los límites son correctos
    // La consulta no se procesa si el límite superior es menor al límite inferior
    private void processQuery() {
        int minsalary = Integer.parseInt(minsalaryField.getText());
        int maxsalary = Integer.parseInt(maxsalaryField.getText());

        if (maxsalary < minsalary) {
            showMessage("\tEl límite superior del salario debe ser mayor al límite inferior.");
        }
        else {
            q.between(minsalary, maxsalary);
            setTable(2); // Inicialmente, muestra la tabla final (selección y proyección)
        }
    }

    // Cambia la tabla que se despliega en el área designada
    private void setTable(int table) {
        String header; // cabecera que muestra los nombres de los atributros que se despliegan en pantalla
        switch (table) {
            case 0: // muestra la tabla completa
                header = "EmployeeID----FirstName--------------LastName------------------Email---------------PhoneNumer---HireDate-------JobID-------Salary-Com-ManID-DeptID";
                tableArea.setText(header + "\n" + q.getEmployees());
                break;
            case 1: // muestra la tabla luego de la selección
                header = "EmployeeID----FirstName--------------LastName------------------Email---------------PhoneNumer---HireDate-------JobID-------Salary-Com-ManID-DeptID";
                tableArea.setText(header + "\n" + q.getSelectionTable());
                break;
            case 2: // muestra la tabla luego de la selección y la proyección
                header = "LastName\t\tSalary";
                tableArea.setText(header + "\n" + q.getProjectionTable());
                break;
        }
        tableArea.setCaretPosition(0);

        viewEmployeeTableButton.setEnabled(true);
        viewSelectionTableButton.setEnabled(true);
        viewProjectionTableButton.setEnabled(true);
    }

    // Muestra un mensaje de error
    private void showMessage(String msg) {
        tableArea.setText(msg);
        viewEmployeeTableButton.setEnabled(false);
        viewSelectionTableButton.setEnabled(false);
        viewProjectionTableButton.setEnabled(false);
    }

    // Método main
    // Ejecuta todo el programa
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}