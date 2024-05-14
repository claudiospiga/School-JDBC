package Scuola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        // Impostazioni della finestra
        setTitle("Menu Principale");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3)); // Utilizziamo un layout a griglia con una riga e tre colonne

        // Creazione dei pulsanti
        JButton studentButton = new JButton("Gestione Studenti");
        JButton gradeBookButton = new JButton("Registro Voti");
        JButton courseButton = new JButton("Gestione Corsi");

        // Personalizzazione dei pulsanti
        studentButton.setFont(new Font("Arial", Font.BOLD, 18));
        gradeBookButton.setFont(new Font("Arial", Font.BOLD, 18));
        courseButton.setFont(new Font("Arial", Font.BOLD, 18));

        studentButton.setForeground(Color.BLUE);
        gradeBookButton.setForeground(Color.GREEN);
        courseButton.setForeground(Color.RED);

        studentButton.setBackground(Color.WHITE);
        gradeBookButton.setBackground(Color.WHITE);
        courseButton.setBackground(Color.WHITE);

        // Aggiunta dei pulsanti alla finestra
        add(studentButton);
        add(gradeBookButton);
        add(courseButton);

        // Listener per i pulsanti
        studentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openStudentApp();
            }
        });

        gradeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openGradeBookApp();
            }
        });

        courseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCourseApp();
            }
        });
    }

    private void openCourseApp() {
    	  dispose();
        CourseApp courseApp = new CourseApp();
        courseApp.setVisible(true);
    }

    private void openStudentApp() {
    	  dispose();
        StudentApp studentApp = new StudentApp();
        studentApp.setVisible(true);
    }

    private void openGradeBookApp() {
    	  dispose();//per chiudere la vecchia finestra
        GradeBookApp gradeBookApp = new GradeBookApp();
        gradeBookApp.setVisible(true);
    }

    public static void main(String[] args) {
        // Creazione e visualizzazione della finestra principale
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Imposta il Look and Feel del sistema operativo attuale
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        });
    }
}
