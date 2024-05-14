package Scuola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GradeBookApp extends JFrame {
    // Dichiarazioni 
    private JLabel studentLabel, courseLabel, gradeLabel;
    private JTextField studentField, courseField, gradeField;
    private JButton saveButton, fetchButton, updateButton, deleteButton,backButton;

    // Dichiarazioni per la connessione al database MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/SchoolDatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "marina97!";

    public GradeBookApp() {
        // Impostazioni della finestra
        setTitle("Registro Voti Studenti");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inizializzazione dei componenti
        JPanel centerPanel = new JPanel(new GridLayout(6, 2));
        studentLabel = new JLabel("Studente:");
        courseLabel = new JLabel("Corso:");
        gradeLabel = new JLabel("Voto:");
        studentField = new JTextField();
        courseField = new JTextField();
        gradeField = new JTextField();
        saveButton = new JButton("Salva");
        fetchButton = new JButton("Visualizza Voti");
        updateButton = new JButton("Aggiorna Voto");
        deleteButton = new JButton("Elimina Voto");
        backButton= new JButton("torna al menu");

        // Aggiunta dei componenti alla finestra
        centerPanel.add(studentLabel);
        centerPanel.add(studentField);
        centerPanel.add(courseLabel);
        centerPanel.add(courseField);
        centerPanel.add(gradeLabel);
        centerPanel.add(gradeField);
        centerPanel.add(saveButton);
        centerPanel.add(fetchButton);
        centerPanel.add(updateButton);
        centerPanel.add(deleteButton);
        centerPanel.add(backButton);
        // Aggiunta del pannello centrale alla finestra nella regione CENTER
        add(centerPanel, BorderLayout.CENTER);
        
        // Aggiunta del pulsante per tornare al menu principale nella regione SOUTH
        add(backButton, BorderLayout.SOUTH); 


        // Aggiunta dei listener per i pulsanti
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGrade();
            }
        });

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchGrades();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateGrade();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteGrade();
            }
        });
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		MainMenu mainMenu= new MainMenu();
        		mainMenu.setVisible(true);
        	}
        });
    }

    private void saveGrade() {
        // Recupera i dati dalla GUI
        String student = studentField.getText();
        String course = courseField.getText();
        String grade = gradeField.getText();

        // Connessione al database e inserimento del voto
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            int studentID = getStudentID(connection, student);
            int courseID = getCourseID(connection, course);

            String insertQuery = "INSERT INTO Grades (StudentID, CourseID, Grade) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, studentID);
                preparedStatement.setInt(2, courseID);
                preparedStatement.setDouble(3, Double.parseDouble(grade));
                preparedStatement.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Voto salvato nel database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del voto nel database.");
        }
    }

    private void fetchGrades() {
        // Recupera i dati dalla GUI
        String student = studentField.getText();

        // Connessione al database e recupero dei voti dello studente
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            int studentID = getStudentID(connection, student);

            String query = "SELECT CourseName, Grade FROM Courses JOIN Grades ON Courses.CourseID = Grades.CourseID WHERE StudentID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, studentID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    StringBuilder gradesInfo = new StringBuilder();
                    while (resultSet.next()) {
                        String courseName = resultSet.getString("CourseName");
                        double grade = resultSet.getDouble("Grade");
                        gradesInfo.append(courseName).append(": ").append(grade).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, "Voti dello studente:\n" + gradesInfo.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il recupero dei voti dello studente.");
        }
    }

    private void updateGrade() {
        // Recupera i dati dalla GUI
        String student = studentField.getText();
        String course = courseField.getText();
        String grade = gradeField.getText();

        // Connessione al database e aggiornamento del voto
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            int studentID = getStudentID(connection, student);
            int courseID = getCourseID(connection, course);

            String updateQuery = "UPDATE Grades SET Grade = ? WHERE StudentID = ? AND CourseID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setDouble(1, Double.parseDouble(grade));
                preparedStatement.setInt(2, studentID);
                preparedStatement.setInt(3, courseID);
                int rowsUpdated = preparedStatement.executeUpdate();
                
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Voto aggiornato nel database.");
                } else {
                    JOptionPane.showMessageDialog(this, "Nessun voto trovato per lo studente e il corso specificati.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento del voto nel database.");
        }
    }

    private void deleteGrade() {
        // Recupera i dati dalla GUI
        String student = studentField.getText();
        String course = courseField.getText();

        // Connessione al database e eliminazione del voto
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            int studentID = getStudentID(connection, student);
            int courseID = getCourseID(connection, course);

            String deleteQuery = "DELETE FROM Grades WHERE StudentID = ? AND CourseID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, studentID);
                preparedStatement.setInt(2, courseID);
                int rowsDeleted = preparedStatement.executeUpdate();
                
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Voto eliminato dal database.");
                } else {
                    JOptionPane.showMessageDialog(this, "Nessun voto trovato per lo studente e il corso specificati.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione del voto dal database.");
        }
    }

    private int getStudentID(Connection connection, String studentName) throws SQLException {
        String query = "SELECT StudentID FROM Students WHERE CONCAT(FirstName, ' ', LastName) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("StudentID");
                }
            }
        }
        throw new SQLException("Studente non trovato: " + studentName);
    }

    private int getCourseID(Connection connection, String courseName) throws SQLException {
        String query = "SELECT CourseID FROM Courses WHERE CourseName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("CourseID");
                }
            }
        }
        throw new SQLException("Corso non trovato: " + courseName);
    }

    public static void main(String[] args) {
        // Creazione e visualizzazione della finestra
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GradeBookApp app = new GradeBookApp();
                app.setVisible(true);
            }
        });
    }
}
