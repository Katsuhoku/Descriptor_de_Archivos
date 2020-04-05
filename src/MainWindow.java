/*
    Benemérita Universidad Autónoma de Puebla
    Facultad de Ciencias de la Computación
    Bases de Datos para Ingeniería
    Primavera 2020

    Uso de un descriptor de archivos para la lectura y posterior consulta
    de una tabla de una base de datos.

    Equipo:
    Coria Rios Marco Antonio, 201734576

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
import java.io.IOException;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    // Atributos de la ventana
    private final int MIN_WIDTH = 1280; // ancho mínimo de la ventana
    private final int MIN_HEIGHT = 720; // alto mínimo de la ventana
    private Query q; // objeto que realiza las consultas y obtiene los resultados paso a paso

    // Componentes
    private JPanel mainPanel; // panel principal

    private JButton processButton; // botón de procesamiento de la consulta
    private JButton viewEmployeeTableButton; // botón de visualización de la tabla de empleados
    private JButton viewDepartmentsTableButton; // botón de visualización de la tabla de departamentos
    private JButton viewProductTableButton; // botón de visualización del producto cartesiano de las dos tablas
    private JButton viewSelectionTableButton; // botón de visualización de la tabla luego de la seleción
    private JButton viewProjectionTableButton; // botón de visualización de la tabla luego de la selección y la proyección

    private JTextField empAttribField; // campo de texto para recibir el límite inferior del salario
    private JTextField deptosAttribField; // campo de texto para recibir el límite superior del salario

    private JTextArea tableArea; // área de texto donde se mostrará la tabla seleccionada

    private String attribFromEmp;
    private String attribFromDeptos;

    private final Color rwc = new Color(237, 78, 216);
    private final Color bgc = new Color(47, 52, 56);

    // Constructor
    public MainWindow() {
        q = new Query();
    }

    // Empieza la ejecución del programa
    public void start() {
        try {
            q.importTable("employees.txt");
        }
        catch (IOException ioe) {
            showErrorDialog("No se pudo encontrar la tabla 'employees.txt'", "Error al cargar la Tabla");
            exit();
        }

        try {
            q.importTable("departments.txt");
        }
        catch (IOException ioe) {
            showErrorDialog("No se pudo encontrar la tabla 'departments.txt'", "Error al cargar la Tabla");
            exit();
        }
        

        setTitle("Consulta");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exit();
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
        empAttribField = new JTextField(8);
        empAttribField.setHorizontalAlignment(JTextField.LEFT);
        empAttribField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (deptosAttribField.getText().length() == 0 || (c != '\b' ? empAttribField.getText() + c : empAttribField.getText()).length() == 0) {
                    processButton.setEnabled(false);
                }
                else {
                    processButton.setEnabled(true);
                }
             }
        });

        deptosAttribField = new JTextField(8);
        deptosAttribField.setHorizontalAlignment(JTextField.LEFT);
        deptosAttribField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((c != '\b' ? deptosAttribField.getText() + c : deptosAttribField.getText()).length() == 0 || empAttribField.getText().length() == 0) {
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
        JLabel selectLabel = new JLabel("SELECT ");
        selectLabel.setForeground(rwc);
        JLabel attribLabel = new JLabel("e.last_name, d.department_name ");
        attribLabel.setForeground(Color.WHITE);
        JLabel fromLabel = new JLabel("FROM ");
        fromLabel.setForeground(rwc);
        JLabel tablesLabel = new JLabel("employees e, departments d ");
        tablesLabel.setForeground(Color.WHITE);
        JLabel whereLabel = new JLabel("WHERE ");
        whereLabel.setForeground(rwc);
        JLabel empLabel = new JLabel("e. ");
        empLabel.setForeground(Color.WHITE);
        JLabel equalsLabel = new JLabel("= ");
        equalsLabel.setForeground(rwc);
        JLabel deptosLabel = new JLabel("d. ");
        deptosLabel.setForeground(Color.WHITE);
        JLabel semicolonLabel = new JLabel(";  ");
        semicolonLabel.setForeground(Color.WHITE);
        pageStartPanel.add(selectLabel);
        pageStartPanel.add(attribLabel);
        pageStartPanel.add(fromLabel);
        pageStartPanel.add(tablesLabel);
        pageStartPanel.add(whereLabel);
        pageStartPanel.add(empLabel);
        pageStartPanel.add(empAttribField);
        pageStartPanel.add(equalsLabel);
        pageStartPanel.add(deptosLabel);
        pageStartPanel.add(deptosAttribField);
        pageStartPanel.add(semicolonLabel);
        pageStartPanel.add(processButton);
        pageStartPanel.setBackground(bgc);

        mainPanel.add(pageStartPanel, BorderLayout.PAGE_START);

        // Área de texto para mostrar la tabla seleccionada
        tableArea = new JTextArea();
        tableArea.setEditable(false);
        tableArea.setFont(new Font("Consolas", Font.PLAIN, 13));

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

        viewDepartmentsTableButton = new JButton("Departments");
        viewDepartmentsTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(1);
            }
        });
        viewDepartmentsTableButton.setBackground(Color.WHITE);
        viewDepartmentsTableButton.setEnabled(false);

        viewProductTableButton = new JButton("Producto Cartesiano");
        viewProductTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(2);
            }
        });
        viewProductTableButton.setBackground(Color.WHITE);
        viewProductTableButton.setEnabled(false);

        viewSelectionTableButton = new JButton("Selección");
        viewSelectionTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(3);
            }
        });
        viewSelectionTableButton.setBackground(Color.WHITE);
        viewSelectionTableButton.setEnabled(false);

        viewProjectionTableButton = new JButton("Proyección");
        viewProjectionTableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTable(4);
            }
        });
        viewProjectionTableButton.setBackground(Color.WHITE);
        viewProjectionTableButton.setEnabled(false);

        JPanel pageEndPanel = new JPanel();
        pageEndPanel.add(viewEmployeeTableButton);
        pageEndPanel.add(viewDepartmentsTableButton);
        pageEndPanel.add(viewProductTableButton);
        pageEndPanel.add(viewSelectionTableButton);
        pageEndPanel.add(viewProjectionTableButton);
        pageEndPanel.setBackground(bgc);

        mainPanel.add(pageEndPanel, BorderLayout.PAGE_END);

        // Paneles auxiliares para dar espacio a los costados del área de la tabla
        JPanel lineStartPanel = new JPanel();
        JPanel lineEndPanel = new JPanel();
        lineStartPanel.setBackground(bgc);
        lineEndPanel.setBackground(bgc);

        mainPanel.add(lineStartPanel, BorderLayout.LINE_START);
        mainPanel.add(lineEndPanel, BorderLayout.LINE_END);

        // Se añade el panel a la ventana
        add(mainPanel);
    }

    // Termina la ejecución de todo el programa
    private void exit() {
        setVisible(false);
        dispose();
    
        System.exit(0);
    }

    // Mostrar un mensaje en otra ventana
    public void showErrorDialog(String msg, String windowTitle) {
        JOptionPane.showMessageDialog(this, msg, windowTitle, JOptionPane.ERROR_MESSAGE);
    }

    /* 
        Acciones en Eventos
    */

    // Procesa la consulta con los valores especificados (al presionar el botón de "Procesar")
    // El objeto "q" (de clase Query) genera las tablas correspondientes
    private void processQuery() {
        attribFromEmp = empAttribField.getText();
        attribFromDeptos = deptosAttribField.getText();

        setTable(4);
    }

    // Cambia la tabla que se despliega en el área designada
    private void setTable(int table) {
        Table result = null;
        switch (table) {
            case 0: // SELECT * FROM employees;
                result = q.from("employees");
                break;
            case 1: // SELECT * FROM departments;
                result = q.from("departments");
                break;
            case 2: // SELECT * FROM employees, departments;
                result = q.product(q.from("employees"), "e", q.from("departments"), "d");
                break;
            case 3: // SELECT * FROM employees, departments WHERE employees.department_id = departments.deparment_id
                try {
                    result = q.equals(q.product(q.from("employees"), "e", q.from("departments"), "d"), "e." + attribFromEmp, "d." + attribFromDeptos);
                } catch (AttributeNotFoundException anfe) {
                    showErrorDialog(anfe.getMessage(), "Error en los atributos");
                }
                break;
            case 4: // SELECT employees.last_name, deparments.department_name FROM employees, departments WHERE employees.department_id = departments.department_id
                ArrayList<String> attributes = new ArrayList<>();
                attributes.add("e.last_name");
                attributes.add("d.department_name");
                try {
                    result = q.select(q.equals(q.product(q.from("employees"), "e", q.from("departments"), "d"), "e." + attribFromEmp, "d." + attribFromDeptos), attributes);
                } catch (AttributeNotFoundException anfe) {
                    showErrorDialog(anfe.getMessage(), "Error en los atributos");
                }
                break;
        }
        if (result != null) {
            tableArea.setText(result.header() + "\n" + result.showAll());

            viewEmployeeTableButton.setEnabled(true);
            viewDepartmentsTableButton.setEnabled(true);
            viewProductTableButton.setEnabled(true);
            viewSelectionTableButton.setEnabled(true);
            viewProjectionTableButton.setEnabled(true);
        }
        tableArea.setCaretPosition(0);
    }

    // Main
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.start();
    }

    /*
     * ...Ignore...
     */
    private static final long serialVersionUID = 1L;
}