package sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.stream.Collectors;

public class EnrollmentPanel extends JPanel {
    private final JComboBox<StudentItem> studentBox = new JComboBox<>();
    private final JComboBox<CourseItem> courseBox = new JComboBox<>();
    private final JTable table = new JTable();

    public EnrollmentPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        var form = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Course:"), gbc);
        gbc.gridy++; form.add(new JLabel("Student:"), gbc);

        gbc.gridx=1; gbc.gridy=0; gbc.anchor = GridBagConstraints.LINE_START;
        form.add(courseBox, gbc);
        gbc.gridy++; form.add(studentBox, gbc);

        var btnEnroll = new JButton("Enroll Student");
        btnEnroll.addActionListener(e -> enroll());
        var top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnEnroll, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        var tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("All Enrollments"), BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        reloadCombos();
        refreshTable();

        courseBox.addActionListener(e -> filterEligibleStudents());
    }

    private void reloadCombos() {
        var cm = new DefaultComboBoxModel<CourseItem>();
        for (var c : DataStore.getCourses()) cm.addElement(new CourseItem(c.getCode(), c.getTitle()));
        courseBox.setModel(cm);
        filterEligibleStudents();
    }

    private void filterEligibleStudents() {
        var selectedCourse = (CourseItem) courseBox.getSelectedItem();
        var sm = new DefaultComboBoxModel<StudentItem>();
        for (var s : DataStore.getStudents()) {
            boolean already = DataStore.getEnrollmentsForStudent(s.getId())
                    .stream().anyMatch(e -> selectedCourse != null && e.getCourseCode().equals(selectedCourse.code));
            if (!already) sm.addElement(new StudentItem(s.getId(), s.getName()));
        }
        studentBox.setModel(sm);
    }

    private void enroll() {
        var course = (CourseItem) courseBox.getSelectedItem();
        var student = (StudentItem) studentBox.getSelectedItem();
        if (course == null || student == null) {
            UiUtils.error(this, "Select a course and an eligible student.");
            return;
        }
        boolean ok = DataStore.enroll(student.id, course.code);
        if (!ok) {
            UiUtils.error(this, "Enrollment failed (already enrolled or invalid).");
            return;
        }
        UiUtils.info(this, "Enrolled " + student.name + " in " + course.code);
        filterEligibleStudents();
        refreshTable();
    }

    private void refreshTable() {
        String[] cols = {"Student ID", "Student Name", "Course", "Grade"};
        var model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        for (var e : DataStore.getEnrollments()) {
            var s = DataStore.findStudent(e.getStudentId()).orElse(null);
            var c = DataStore.findCourse(e.getCourseCode()).orElse(null);
            model.addRow(new Object[] {
                    e.getStudentId(),
                    s != null ? s.getName() : "?",
                    c != null ? c.getCode()+" - "+c.getTitle() : e.getCourseCode(),
                    e.getGrade() == null ? "(none)" : e.getGrade()
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
