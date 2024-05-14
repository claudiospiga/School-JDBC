package Scuola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentApp extends JFrame {
    // Dichiarazioni dei componenti GUI
    private JLabel firstNameLabel, lastNameLabel;
    private JTextField firstNameField, lastNameField;
    private JButton saveButton, fetchButton, updateButton, deleteButton, backButton;

    // Dichiarazioni per la connessione al database MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/SchoolDatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "marina97!";

    public StudentApp() {
        // Impostazioni della finestra
        setTitle("Gestione Studenti");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));
        // Inizializzazione dei componenti
        firstNameLabel = new JLabel("Nome:");
        lastNameLabel = new JLabel("Cognome:");
       
        firstNameField = new JTextField();
        lastNameField = new JTextField();
       
        saveButton = new JButton("Salva");
        fetchButton = new JButton("Visualizza Studenti");
        updateButton = new JButton("Aggiorna Studente");
        deleteButton = new JButton("Elimina Studente");
        backButton = new JButton("Torna al Menu");

        // Aggiunta dei componenti alla finestra
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
       
        add(saveButton);
        add(fetchButton);
        add(updateButton);
        add(deleteButton);
        add(backButton);

        // Aggiunta dei listener per i pulsanti
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchStudents();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiudi la finestra corrente
                MainMenu mainMenu = new MainMenu(); // Apre il menu principale
                mainMenu.setVisible(true);
            }
        });
    }

    private void saveStudent() {
        // Recupera i dati dalla GUI
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
       

        // Connessione al database e inserimento dello studente
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO Students (FirstName, LastName) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
               
                preparedStatement.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Studente salvato nel database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio dello studente nel database.");
        }
    }

    private void fetchStudents() {
        // Connessione al database e recupero degli studenti
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM Students";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                StringBuilder studentsInfo = new StringBuilder();
                while (resultSet.next()) {
                    int studentID = resultSet.getInt("StudentID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                   
                    studentsInfo.append("ID: ").append(studentID).append(", Nome: ").append(firstName).append(", Cognome: ").append(lastName).append("\n");
                }
                JOptionPane.showMessageDialog(this, "Elenco degli studenti:\n" + studentsInfo.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il recupero degli studenti dal database.");
        }
    }

    private void updateStudent() {
        // Recupera i dati dalla GUI
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
  

        // Recupera l'ID dello studente dalla GUI
        String studentIDString = JOptionPane.showInputDialog(this, "Inserisci l'ID dello studente da aggiornare:");
        if (studentIDString != null && !studentIDString.isEmpty()) {
            try {
                int studentID = Integer.parseInt(studentIDString);

                // Connessione al database e aggiornamento dello studente
                try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String updateQuery = "UPDATE Students SET FirstName = ?, LastName = ? WHERE StudentID = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, firstName);
                        preparedStatement.setString(2, lastName);
                      
                        preparedStatement.setInt(3, studentID);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Informazioni dello studente aggiornate nel database.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Nessuno studente trovato con l'ID specificato.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID studente non valido.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento delle informazioni dello studente nel database.");
            }
        }
    }


    private void deleteStudent() {
        // Recupera l'ID dello studente dalla GUI
        String studentIDString = JOptionPane.showInputDialog(this, "Inserisci l'ID dello studente da eliminare:");
        if (studentIDString != null && !studentIDString.isEmpty()) {
            try {
                int studentID = Integer.parseInt(studentIDString);

                // Connessione al database e eliminazione dello studente
                try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String deleteQuery = "DELETE FROM Students WHERE StudentID = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                        preparedStatement.setInt(1, studentID);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Studente eliminato dal database.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Nessuno studente trovato con l'ID specificato.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID studente non valido.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione dello studente dal database.");
            }
        }
    }


    public static void main(String[] args) {
        // Creazione e visualizzazione della finestra
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StudentApp app = new StudentApp();
                app.setVisible(true);
            }
        });
    }
}
