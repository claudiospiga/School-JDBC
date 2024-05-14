package Scuola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CourseApp extends JFrame {
    // Impostazioni della finestra
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/SchoolDatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "marina97!";

    private JLabel courseNameLabel;
    private JTextField courseNameField;
    private JButton saveButton;
    private JButton fetchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton; // Aggiungiamo il pulsante per tornare al menu principale

    public CourseApp() {
        // Impostazioni della finestra
        setTitle("Gestione Corsi");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Utilizziamo un layout BorderLayout
        
        // Inizializzazione dei componenti
        JPanel centerPanel = new JPanel(new GridLayout(6, 2)); // Utilizziamo un pannello per i componenti centrali
        courseNameLabel = new JLabel("Nome Corso:");
        courseNameField = new JTextField();
        saveButton = new JButton("Salva");
        fetchButton = new JButton("Visualizza Corsi");
        updateButton = new JButton("Aggiorna Corso");
        deleteButton = new JButton("Elimina Corso");
        backButton = new JButton("Torna al Menu"); // Aggiungiamo il pulsante per tornare al menu principale

        // Aggiunta dei componenti al pannello centrale
        centerPanel.add(courseNameLabel);
        centerPanel.add(courseNameField);
        centerPanel.add(saveButton);
        centerPanel.add(fetchButton);
        centerPanel.add(updateButton);
        centerPanel.add(deleteButton);

        // Aggiunta del pannello centrale alla finestra nella regione CENTER
        add(centerPanel, BorderLayout.CENTER);
        
        // Aggiunta del pulsante per tornare al menu principale nella regione SOUTH
        add(backButton, BorderLayout.SOUTH); 

        // Aggiunta dei listener per i pulsanti
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCourse();
            }
        });

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchCourses();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCourse();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Chiudi la finestra corrente e apri il menu principale
                dispose();
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        });
    }

    private void saveCourse() {
        // Recupera i dati dalla GUI
        String courseName = courseNameField.getText();

        // Connessione al database e inserimento del corso
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO Courses (CourseName) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, courseName);
                preparedStatement.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Corso salvato nel database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del corso nel database.");
        }
    }

    private void fetchCourses() {
        // Connessione al database e recupero dei corsi
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM Courses";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                StringBuilder coursesInfo = new StringBuilder();
                while (resultSet.next()) {
                    int courseID = resultSet.getInt("CourseID");
                    String courseName = resultSet.getString("CourseName");
                    coursesInfo.append("ID Corso: ").append(courseID).append(", Nome Corso: ").append(courseName).append("\n");
                }
                JOptionPane.showMessageDialog(this, "Elenco dei corsi:\n" + coursesInfo.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il recupero dei corsi dal database.");
        }
    }

    private void updateCourse() {
        // Recupera i dati dalla GUI
        String courseName = courseNameField.getText();

        // Recupera l'ID del corso dalla GUI
        String courseIDString = JOptionPane.showInputDialog(this, "Inserisci l'ID del corso da aggiornare:");
        if (courseIDString != null && !courseIDString.isEmpty()) {
            try {
                int courseID = Integer.parseInt(courseIDString);

                // Connessione al database e aggiornamento del corso
                try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String updateQuery = "UPDATE Courses SET CourseName = ? WHERE CourseID = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, courseName);
                        preparedStatement.setInt(2, courseID);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Informazioni del corso aggiornate nel database.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Nessun corso trovato con l'ID specificato.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID corso non valido.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento delle informazioni del corso nel database.");
            }
        }
    }


    private void deleteCourse() {
        // Recupera l'ID del corso dalla GUI
        String courseIDString = JOptionPane.showInputDialog(this, "Inserisci l'ID del corso da eliminare:");
        if (courseIDString != null && !courseIDString.isEmpty()) {
            try {
                int courseID = Integer.parseInt(courseIDString);

                // Connessione al database e eliminazione del corso
                try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String deleteQuery = "DELETE FROM Courses WHERE CourseID = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                        preparedStatement.setInt(1, courseID);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Corso eliminato dal database.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Nessun corso trovato con l'ID specificato.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID corso non valido.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione del corso dal database.");
            }
        }
    }

    public static void main(String[] args) {
        // Creazione e visualizzazione della finestra
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CourseApp app = new CourseApp();
                app.setVisible(true);
            }
        });
    }
}
