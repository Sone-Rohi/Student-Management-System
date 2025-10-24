package sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UiUtils {
    public static JMenuBar buildMenuBar(JFrame frame, JTabbedPane tabs) {
        var menuBar = new JMenuBar();
        var file = new JMenu("File");
        var exit = new JMenuItem("Exit");
        exit.addActionListener(e -> frame.dispose());
        file.add(exit);

        var nav = new JMenu("Navigate");
        var students = new JMenuItem("Students");
        students.addActionListener(e -> tabs.setSelectedIndex(0));
        var enroll = new JMenuItem("Enrollment");
        enroll.addActionListener(e -> tabs.setSelectedIndex(1));
        var grades = new JMenuItem("Grades");
        grades.addActionListener(e -> tabs.setSelectedIndex(2));
        nav.add(students); nav.add(enroll); nav.add(grades);

        var help = new JMenu("Help");
        var about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Student Management System (Swing)\nDemo app for CRUD, enrollment & grades.",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        menuBar.add(file);
        menuBar.add(nav);
        menuBar.add(help);
        return menuBar;
    }

    public static DefaultTableModel makeStudentTableModel(List<Student> data) {
        String[] cols = {"ID", "Name", "Email"};
        var model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        for (var s : data) model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
        return model;
    }

    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
