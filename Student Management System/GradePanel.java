package sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GradePanel extends JPanel {
    private final JComboBox<StudentItem> studentBox = new JComboBox<>();
    private final JComboBox<CourseItem> courseBox = new JComboBox<>();
    private final JComboBox<String> gradeBox = new JComboBox<>(new String[]{"A","A-","B+","B","B-","C+","C","D","F"});
    private final JTable table = new JTable();

    public GradePanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        reloadStudents();

        studentBox.addActionListener(e -> reloadCoursesForStudent());

        var form = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Student:"), gbc);
        gbc.gridy++; form.add(new JLabel("Course:"), gbc);
        gbc.gridy++; form.add(new JLabel("Grade:"), gbc);

        gbc.gridx=1; gbc.gridy=0; gbc.anchor = GridBagConstraints.LINE_START;
        form.add(studentBox, gbc);
        gbc.gridy++; form.add(courseBox, gbc);
        gbc.gridy++; form.add(gradeBox, gbc);

        var btnAssign = new JButton("Assign Grade");
        btnAssign.addActionListener(e -> assignGrade());
        var top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnAssign, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        var tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Student Enrollments & Grades"), BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        refreshTable();
    }

    private void reloadStudents() {
        var sm = new DefaultComboBoxModel<StudentItem>();
        for (var s : DataStore.getStudents()) sm.addElement(new StudentItem(s.getId(), s.getName()));
        studentBox.setModel(sm);
        reloadCoursesForStudent();
    }

    private void reloadCoursesForStudent() {
        var selectedStudent = (StudentItem) studentBox.getSelectedItem();
        var cm = new DefaultComboBoxModel<CourseItem>();
        if (selectedStudent != null) {
            for (var e : DataStore.getEnrollmentsForStudent(selectedStudent.id)) {
                var c = DataStore.findCourse(e.getCourseCode()).orElse(null);
                if (c != null) cm.addElement(new CourseItem(c.getCode(), c.getTitle()));
            }
        }
        courseBox.setModel(cm);
    }

    private void assignGrade() {
        var s = (StudentItem) studentBox.getSelectedItem();
        var c = (CourseItem) courseBox.getSelectedItem();
        var g = (String) gradeBox.getSelectedItem();
        if (s == null || c == null || g == null) {
            UiUtils.error(this, "Select a student, a course, and a grade.");
            return;
        }
        boolean ok = DataStore.assignGrade(s.id, c.code, g);
        if (!ok) {
            UiUtils.error(this, "Failed to assign grade (check enrollment).");
            return;
        }
        UiUtils.info(this, "Assigned " + g + " to " + s.name + " for " + c.code);
        refreshTable();
    }

    private void refreshTable() {
        String[] cols = {"Student", "Course", "Grade"};
        var model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        for (var e : DataStore.getEnrollments()) {
            var s = DataStore.findStudent(e.getStudentId()).orElse(null);
            var c = DataStore.findCourse(e.getCourseCode()).orElse(null);
            model.addRow(new Object[] {
                    s != null ? s.getId()+" - "+s.getName() : e.getStudentId(),
                    c != null ? c.getCode()+" - "+c.getTitle() : e.getCourseCode(),
                    e.getGrade()==null ? "(none)" : e.getGrade()
            });
        }
        table.setModel(model);
    }

    static class StudentItem {
        String id, name;
        StudentItem(String id, String name) { this.id=id; this.name=name; }
        @Override public String toString() { return id + " - " + name; }
    }
    static class CourseItem {
        String code, title;
        CourseItem(String code, String title) { this.code=code; this.title=title; }
        @Override public String toString() { return code + " - " + title; }
    }
}
