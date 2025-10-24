package sms;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}

class MainFrame extends JFrame {
    public MainFrame() {
        super("Student Management System (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Seed a few demo courses and students
        DataStore.seed();

        var tabs = new JTabbedPane();
        tabs.addTab("Students", new StudentPanel());
        tabs.addTab("Enrollment", new EnrollmentPanel());
        tabs.addTab("Grades", new GradePanel());

        setJMenuBar(UiUtils.buildMenuBar(this, tabs));
        add(tabs, BorderLayout.CENTER);
    }
}
